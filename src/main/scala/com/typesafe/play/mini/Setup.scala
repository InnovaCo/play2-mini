/**
 *
 * Copyright (C) 2011 Typesafe Inc. <http://www.typesafe.com>
 *
 **/

package com.typesafe.play.mini

import play.api.GlobalSettings
import play.api.mvc._
import play.api.mvc.Results._

/**
 * an interface that a mini application needs to implement
 */
trait Application {
  def route: PartialFunction[RequestHeader, Handler]
}

/**
 * provides a simple way to use play as a simple http layer (using scala)
 *
 * example:
 * in the global package name space
 * {{{
 * object Global extends play.api.mini.Setup[com.example.App]
 * }}}
 * and then in your own package
 * {{{
 * class App extends com.typesafe.play.mini.Application {
 *   def route  =  {
 *      case GET(Path("/coco")) & QueryString(qs) =>  Action{ 
 *          val o = QueryString(qs,"foo").getOrElse("noh");
 *          Ok(<h1>It works!, query String {o}</h1>).as("text/html") 
 *      } 
 *    }
 * }
 * }}}
 */
class Setup[T <: Application](implicit m: Manifest[T]) extends GlobalSettings {
  
  private lazy val dispatch: PartialFunction[RequestHeader, Handler] = {
    val cl = Thread.currentThread().getContextClassLoader()
    try {
      val clazz = m.erasure
      clazz.getMethod("route").invoke(clazz.newInstance()).asInstanceOf[PartialFunction[RequestHeader, Handler]]
    } catch { case (ex: Exception) => throw new Exception("could not find Application:" + ex.toString) }
  }

  override def onRouteRequest(request: RequestHeader): Option[Handler] = try { 
      Some(dispatch(request))
    } catch { case ex:MatchError=> None }

}

/**
 * provides a simple way to use play as a simple http layer (using java)
 *
 * example:
 * in the global package name space
 * {{{
 * object Global extends com.typesafe.play.mini.SetupJava[com.example.App]
 * }}}
 * and then in your own package
 * {{{
 * public class Application extends Controller {
 *   @URL("/hello")
 *   public static Result index() {
 *       return ok("It works!");
 *   }
 *   @URL("/foo/&#42;/name/&#42;")
 *   public static Result index2(String foo, String name) {
 *       response().setContentType("/text/html");
 *       return ok("It works:"+ foo +" "+name);
 *   }
 * }
 * }}}
 */
class SetupJava[T <: play.mvc.Controller](implicit m: Manifest[T]) extends GlobalSettings {

  import collection.JavaConverters._
  import play.mvc.Http.{ Context => JContext, Request => JRequest }

  private def createJavaContext(req: RequestHeader) = {
      new JContext( new JRequest {

        def uri = req.uri
        def method = req.method
        def path = req.method

        def queryString = { 
          req.queryString.mapValues(_.toArray).asJava
        }   

        def urlFormEncoded = { 
          Map[String,Array[String]]().asJava
        }   

        override def toString = req.toString

      },  
      req.session.data.asJava,
      req.flash.data.asJava)
  }

  private def toSimpleResult(javaContext: JContext, r: play.mvc.Result) = r.getWrappedResult match {
      case result @ SimpleResult(_, _) => {
        val wResult = result.withHeaders(javaContext.response.getHeaders.asScala.toSeq: _*)

        if (javaContext.session.isDirty && javaContext.flash.isDirty) {
          wResult.withSession(Session(javaContext.session.asScala.toMap)).flashing(Flash(javaContext.flash.asScala.toMap))
        } else {
          if (javaContext.session.isDirty) {
            wResult.withSession(Session(javaContext.session.asScala.toMap))
          } else {
            if (javaContext.flash.isDirty) {
              wResult.flashing(Flash(javaContext.flash.asScala.toMap))
            } else {
              wResult
            }   
          }   
        }   

      }   
      case other => other
  } 
  
  private def setupRoutes(path: String,ctx: JContext) : Option[Handler] = {
    val methods = m.erasure.getMethods
    methods.filter(m => m.getAnnotation(classOf[URL]) != null).map { m =>
      val pattern = m.getAnnotation(classOf[URL]).value().replaceAll("\\*","(.\\*)").r
      val result = pattern.findAllIn(path)
      if (result.isEmpty == false) {
        val params =  result.matchData.toList.map(m=>m.subgroups).flatten.toArray
          try {
            val simpleResult = toSimpleResult(ctx,m.invoke(null,params:_*).asInstanceOf[play.mvc.Result])
            Some(Action{simpleResult})
          } catch {case ex: Exception => ex.printStackTrace(); Some(Action{InternalServerError})}
      } else None  
    }.flatten.headOption
  }
  
  /**
   * creates java context then setups routes
   */
  override def onRouteRequest(request: RequestHeader): Option[Handler] = request match {
    case _ => {
      val ctx = createJavaContext(request)
      JContext.current.set(ctx) 
      setupRoutes(request.uri,ctx)
    }
  }

}
