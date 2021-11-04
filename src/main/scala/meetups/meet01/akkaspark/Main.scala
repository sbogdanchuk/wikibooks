package org.wikibooks
package meetups.meet01.akkaspark

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import org.wikibooks.meetups.meet01.akkaspark.SparkBot.{AggrRes, ListRes, Result}

/**
 *
 * @author Siarhei Bahdanchuk
 *         Date: 03.11.2021
 *
 */
object Main extends App {

  def apply(): Behavior[Result] = Behaviors.receive { (ctx, msg) =>
    ctx.log.info(f"===>Recieved from SparkBot ${msg}!")
    msg match{
      case AggrRes(res, from)=>
        ctx.log.info(f"===>Actual sum is ${res}!")
        Behaviors.same
      case ListRes(res, from)=>
      ctx.log.info(f"===>List is ${res}!")
        Behaviors.same
    }
  }

  val sparkBot:ActorSystem[SparkBot.Command]= {
    ActorSystem(SparkBot(), "spark-bot")
  }
  val main:ActorSystem[SparkBot.Result] = ActorSystem(Main(), "main")
  sparkBot ! SparkBot.Aggregate(main)
  sparkBot ! SparkBot.ListCmd(main)
  sparkBot ! SparkBot.ListCmd(main)
  sparkBot ! SparkBot.Aggregate(main)
}
