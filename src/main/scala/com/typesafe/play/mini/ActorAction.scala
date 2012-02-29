package com.typesafe.mini 

import akka.pattern.Patterns.ask
import akka.util.Timeout
import akka.util.duration._
import akka.actor.ActorRef
import play.api.mvc._
import play.api.libs.concurrent._

/**
 *
 * Provides syntatic sugar for mapping an ActorRef's ask call result onto an AsyncResult
 *
 * usage:
 * {{{
 *  ActorAction[String](myActor,Message) {reply: String =>
 *    Ok(reply) 
 *  }
 * }}}
 *
 */
object ActorAction{
  
  /**
   * @param actorRef actor
   * @param msg message to send
   * @param timeout defaults to 5 sec
   * @param codeBlock to be executed upon replying
   * @return a play Action with AsyncResult
   */
  def apply[A](actorRef: ActorRef, msg: AnyRef, timeout: Timeout = 5 seconds)(codeBlock: A => Result)(implicit m: Manifest[A]): Action[play.api.mvc.AnyContent] = {
      Action {
        AsyncResult {
          ask(actorRef,msg,timeout).mapTo[A].asPromise.map { reply =>
            codeBlock(reply)
          }
        }
      }
  }
}