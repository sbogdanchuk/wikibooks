package org.wikibooks
package meetups.meet01.akkaspark

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
 *
 * @author Siarhei Bahdanchuk
 *         Date: 03.11.2021
 *
 */
object SparkBot {
  sealed trait Command
  sealed trait Result
  final case class Aggregate(replyTo: ActorRef[AggrRes]) extends Command
  final case class ListCmd(replyTo: ActorRef[ListRes]) extends Command
  final case class Feed(replyTo: ActorRef[ListRes]) extends Command
  final case class AggrRes(res: Int, from: ActorRef[Aggregate]) extends Result
  final case class ListRes(res: List[Int], from: ActorRef[Aggregate]) extends Result

  def apply(): Behavior[Command] = {

    val conf = new SparkConf()
      .setMaster("local[4]")
      .setAppName("Wikibooks")
      .set("spark.cores.max", "7")
    val sc = new SparkContext(conf)
    val data: List[Int] = Source.fromResource("data.dat").getLines.toList.map(e => e.toInt)
    val rdd = sc.parallelize(data)

    Behaviors.receive { (ctx, msg) =>
      msg match {
        case Aggregate(replyTo) =>
          ctx.log.info("===>Recieved {}!", msg)
          val sum = rdd.reduce(_ + _)
          replyTo ! AggrRes(sum, ctx.self)
          Behaviors.same
        case ListCmd(replyTo)=>
        ctx.log.info("===>Recieved {}!", msg)
          val res = rdd.collect().toList
          replyTo ! ListRes(res, ctx.self)
          Behaviors.same
      }
    }
  }
}
