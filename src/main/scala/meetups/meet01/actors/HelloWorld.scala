package org.wikibooks
package meetups.meet01.actors

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

/**
 *
 * @author Siarhei Bahdanchuk
 *         Date: 30.10.2021
 *
 */
object HelloWorld {
  final case class Greet(whom:String, replyTo: ActorRef[Greeted])
  final case class Greeted(whom: String, from: ActorRef[Greet])

  def apply(): Behavior[Greet]=Behaviors.receive{(context,message)=>
    context.log.info("Hello {}!", message.whom)
    message.replyTo ! Greeted(message.whom, context.self)
    Behaviors.same
  }
}
