/**
 *
 * Copyright (C) 2011 Typesafe Inc. <http://www.typesafe.com>
 *
 **/

package com.typesafe.play.mini

import play.api.mvc._

/**
  * provides extra routing mechanism
  **/
object Through {

 
 /**
  * @param matches regex and extract the groups into groups
  * @param block block of code to be executed on matching regex
  * @return PartialFunction to be executed
  * for incomig url: /people/25 
  * {{{ 
  * def route = Through("/people/(.*)".r) { (groups: List[String]) => 
  *     Action{
  *       val id :: Nil = groups
  *       Ok("current id:"+id)
  *     }
  *   } 
  *
  * Additionally,
  * {{{
  * Routes(Through(...), Through(...)) 
  * }}} 
  * can be used for multiple PartialFunction  
  *
*/
  def apply(regex: scala.util.matching.Regex)(block: List[String] => Handler): PartialFunction[RequestHeader, Handler] = {
   case rh: RequestHeader if regex.findFirstMatchIn(Path(rh)).isDefined => 
    block(regex.unapplySeq(Path(rh)).getOrElse(Nil))
  }

 /**
  * @param baseUrl matches the baseUrl from the left and returning and returning the result split by "/"
  * @param block to be executed on matching regex
  * example
  * for incomig url: /people/my/current/25
  * {{{
  * def route = Through("/people/) {groups: List[String] =>
  *     Action{ 
  *       val id :: Nil = groups
  *       Ok(<h1>It works with startsWith!, id: {id}</h1>).as("text/html") 
  *     }
  *   }
  * }}}
  * 
  * Additionally,
  * {{{
  * Routes(Through(...), Through(...)) 
  * }}} 
  * can be used for multiple PartialFunction
  *
*/
 def apply(baseUrl: String)(block: List[String] => Handler): PartialFunction[RequestHeader, Handler] = {
      case rh: RequestHeader if Path(rh).startsWith(baseUrl) => 
       val groups = Path(rh).split(baseUrl)(1).split("/").toList
       block(groups)
  }
 
}
