package com.example 

import com.typesafe.play.mini._
import play.api.mvc._
import play.api.mvc.Results._

/**
 * this application is registered via Global
 */
object App extends Application { 
  def route = Routes(
  	Through("/people/(.*)".r) {groups: List[String] =>
      Action{ 
      	val id :: Nil = groups
      	Ok(<h1>It works with regex!, id: {id}</h1>).as("text/html")	
      }
  	}, 
    {case GET(Path("/coco")) & QueryString(qs) => Action{ request =>
        println(request.body)
        println(play.api.Play.current)
        val result = QueryString(qs,"foo").getOrElse("noh")
        Ok(<h1>It works!, query String {result}</h1>).as("text/html") }
  	},
      Through("/flowers/id/") {groups: List[String] =>
        Action{ 
          val id :: Nil = groups
          Ok(<h1>It works with simple startsWith! -  id: {id}</h1>).as("text/html") 
        }
      }
  )
}
