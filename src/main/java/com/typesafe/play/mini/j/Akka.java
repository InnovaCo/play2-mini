package com.typesafe.mini.j;

import static akka.pattern.Patterns.ask;
import akka.util.Timeout;
import akka.actor.ActorRef;
import static com.typesafe.mini.j.Sneak.sneakyThrow;

public class Akka {

 /**
   * provides a way to map an actor ask call onto a java AsyncResult 
   * example usage:
   * public static Result index(String coco, String name) {
   *    Akka.async(myActor,new Message, system.settings().ActorTimeout()), new Callable1<Object,play.mvc.Result> {
   *       protected abstract play.mvc.Result operateOn(Object arg) {
   *          return ok(arg.toString());
   *       }
   *    }); 
   * }
   * @param actorRef actor
   * @param msg message to send
   * @param timeout defaults to 5 sec
   * @param f to be executed upon replying
   * @return a play.mvc.Results.AsyncResult that wraps the akka result 
   */
  public static play.mvc.Results.AsyncResult async(ActorRef actorRef, java.lang.Object msg, Timeout timeout, final Callable1<java.lang.Object, play.mvc.Result> f ) {

    return play.mvc.Results.async( 
      play.libs.Akka.asPromise(ask(actorRef,msg, timeout)).map(
        new play.libs.F.Function<java.lang.Object, play.mvc.Result>() {
          public play.mvc.Result apply(java.lang.Object response){
            try {
              return f.pass(response).call();
            } catch (Exception ex) {
              throw sneakyThrow(ex);
            }
          }
        }
      )
    );
  }
  
}    
