package org.wikibooks
package meetups.meet01.actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{Behaviors, LoggerOps}

/**
 *
 * @author Siarhei Bahdanchuk
 *         Date: 03.11.2021
 *
 */
object HelloWorldBot {
  def apply(max: Int): Behavior[HelloWorld.Greeted] = {
    bot(0, max)
  }

  private def bot(greetingCounter: Int, max: Int): Behavior[HelloWorld.Greeted] =
    Behaviors.receive { (context, message) =>
      val n = greetingCounter + 1
      context.log.info2("Greeting {} for {}", n, message.whom)
      if (n == max) {
        Behaviors.stopped
      } else {
        message.from ! HelloWorld.Greet(message.whom, context.self)
        bot(n, max)
      }
    }

}
