/**
 *
 * Copyright (C) 2011 Typesafe Inc. <http://www.typesafe.com>
 *
 **/

package com.typesafe.play.mini

import play.api.mvc._
import util.control.Exception._
import org.jboss.netty.handler.codec.http.QueryStringDecoder

/**
 * extractors slightly changed for Play but otherwise taken 
 * from the awesome unfiltered library http://unfiltered.databinder.net/Unfiltered.html
 */
object Path {
  def unapply(req: RequestHeader) = Some(req.path)
  def apply(req: RequestHeader) = req.path
}

/**
 * query string
 */ 
object QueryString {
  def unapply(req: RequestHeader) = req.uri.split('?') match {
    case Array(path) => None
    case Array(path, query) => Some(query)
  }
  def apply(qs: String, param: String) = { 
   val decoder = new QueryStringDecoder("?"+qs)
   Option(decoder.getParameters().get(param))
  }  
}

/**
  * provides alternative routing mechanism where 
  **/
object Through {

 
 /**
  * for incomig url: /people/25 
  * {{{ 
  * def route = Through("/people/(.*)".r) { (groups: List[String]) => 
  *     Action{
  *       val id :: Nil = groups
  *       Ok("current id:"+id)
  *     }
  *   } orElse {
  *       case GET(Path("/internalnotgoinganywhere")) => Action{Ok("yay")}
  *   }
  * }
  * }}}
*/
  def apply(regex: scala.util.matching.Regex)(rf: List[String] => Handler): PartialFunction[RequestHeader, Handler] = {
   case rh: RequestHeader if regex.findFirstMatchIn(Path(rh)).isDefined => 
    rf(regex.unapplySeq(Path(rh)).getOrElse(Nil))
  }

 /**
  * for incomig url: /people/my/current/25
  * {{{
  * def route = Through("/people/"){ (groups: List[String]) => 
  *    Action{
  *       val "my" :: "current" :: id = groups
  *       Ok("current id:"+id)
  *     }
  *   } orElse {
  *       case GET(Path("/internalnotgoinganywhere")) => Action{Ok("yay")}
  *   }
  * }
  * }}}
*/
 def apply(baseUrl: String)(rf: List[String] => Handler): PartialFunction[RequestHeader, Handler] = {
      case rh: RequestHeader if Path(rh).startsWith(baseUrl) => 
       val groups = Path(rh).split(baseUrl)(1).split("/").toList
       rf(groups)
  }
 
}
/**
 * path segment
 */ 
object Seg {
  def unapply(path: String): Option[List[String]] = path.split("/").toList match {
    case "" :: rest => Some(rest) // skip a leading slash
    case all => Some(all)
  }
}

/*
 * method
 */
class Method(method: String) {
  def unapply(req: RequestHeader) =
    if (req.method.equalsIgnoreCase(method)) Some(req)
    else None
}

object GET extends Method("GET")
object POST extends Method("POST")
object PUT extends Method("PUT")
object DELETE extends Method("DELETE")
object HEAD extends Method("HEAD")
object CONNECT extends Method("CONNECT")
object OPTIONS extends Method("OPTIONS")
object TRACE extends Method("TRACE")

object & { def unapply[A](a: A) = Some(a, a) }


