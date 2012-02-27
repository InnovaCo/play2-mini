/**
 *
 * Copyright (C) 2011 Typesafe Inc. <http://www.typesafe.com>
 *
 **/

package com.typesafe.play.mini

import play.api.mvc._

/**
 * provides chaining for a PartialFunction[RequestHeader, Handler]
 * example: 
 * {{{
 * def route = Routes(
 *   Through("/people/(.*)".r) {groups: List[String] =>
 *     Action{ 
 *       val id :: Nil = groups
 *       Ok(<h1>It works with regex!, id: {id}</h1>).as("text/html") 
 *     }
 *   }, 
 *   {case GET(Path("/coco")) & QueryString(qs) => Action{ request =>
 *       println(request.body)
 *       println(play.api.Play.current)
 *       val result = QueryString(qs,"foo").getOrElse("noh")
 *       Ok(<h1>It works!, query String {result}</h1>).as("text/html") }
 *   },
 *     Through("/flowers/id/") {groups: List[String] =>
 *       Action{ 
 *         val id :: Nil = groups
 *         Ok(<h1>It works with simple startsWith! -  id: {id}</h1>).as("text/html") 
 *       }
 *     }
 * )
 * }}}
 */
object Routes{
  def apply(funcList: PartialFunction[RequestHeader, Handler]*) = {
    funcList.toList.reduceLeft { (functions,f) => functions orElse f }
  }
}