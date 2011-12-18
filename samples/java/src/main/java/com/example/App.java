package com.example;

import play.*;
import play.mvc.*;
import com.typesafe.play.mini.*;
/**
 * this app is configured through Global.scala
 */
public class App extends Controller {

   @URL("/coco/*/name/*")
   public static Result index(String coco, String name) {
     response().setContentType("text/html");
     return ok("It works:"+coco+" "+name);
   }
   @URL("/coco/index")
   public static Result index() {
     response().setContentType("text/html");
     try {
      System.out.println(request().body());
     } catch (Exception ex) {ex.printStackTrace();}
     return ok("It works!");
   }
}
