Play2 Mini Project
==================

Play2 Mini Project provides REST API on top of `Play2 <https://github.com/playframework/Play20>`_

Scala
-----

In the global package name space::

  object Global extends com.typesafe.play.mini.Setup(com.example.App)

and then in your own package::

  object App extends com.typesafe.play.mini.Application {
     def route  =  {
        case GET(Path("/coco")) & QueryString(qs) =>  Action{
            val o = QueryString(qs,"foo").getOrElse("noh")
            Ok(<h1>It works!, query String {o}</h1>).as("text/html")
        }
      }
  }

The API is based on the always awesome `Unfiltered <http://unfiltered.databinder.net/Unfiltered.html>`_ library.

Other than this, there are many useful utilities available at your fingertip, take a look at the official guide of `Play for Scala Developers <https://github.com/playframework/Play20/wiki/ScalaHome>`_



Java
----

In the global package name space::

  //Global.scala
  object Global extends com.typesafe.play.mini.SetupJavaApplicationFor[com.example.App]

and then in your own package::

  //com/example/App.java
  package com.example;

  public class App extends Controller {
    @URL("/hello")
    public static Result index() {
        return ok("It works as text!");
    }

   @URL("/foo/*/name/*")
    public static Result index2(String foo, String name) {
        response().setContentType("/text/html");
        return ok("It works:"+ foo +" "+name);
   }
 }

Other than this, there are many useful utilities available at your fingertip, take a look at `Play Java Utils <https://github.com/playframework/Play20/tree/master/framework/src/play/src/main/java/play/libs>`_ and `Async processing with Play <https://github.com/playframework/Play20/wiki/JavaAsync>`


See sample apps for more examples.

G8 Project templates
--------------------

`G8 <https://github.com/n8han/giter8>`_ project templates are available as well.


java::

g8 pk11/play-mini-java.g8


scala::

g8 pk11/play-mini-scala.g8



Licence
-------

The code is licensed under Apache 2 license::

  This software is licensed under the Apache 2 license, quoted below.

  Copyright 2011 Typesafe (http://www.typesafe.com).

  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

