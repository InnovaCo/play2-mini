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

Java
----

In the global package name space::

  object Global extends com.typesafe.play.SetupJava[com.example.App]

and then in your own package::

  public class Application extends Controller {
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

See sample apps for more examples.

Building
--------

First check out and build Play2.0::

  git clone git://github.com/playframework/Play20.git
  cd Play20/framework
  ./build publish-local
  cd ..
  cp -r repository/local/play ~/.ivy2/local

Then check out and build Play2 Mini project::

  git clone git@github.com:pk11/play2-mini.git
  cd play-mini
  sbt publish-local


Licence
-------

The code is licensed under Apache 2 license::

  This software is licensed under the Apache 2 license, quoted below.

  Copyright 2011 Typesafe (http://www.typesafe.com).

  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

