package org.wikibooks
package meetups.meet01

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

/**
 *
 * @author Siarhei Bahdanchuk
 *         Date: 30.10.2021
 *
 */
final case class Table(id: Int, name: String, participants: Int)
final case class Tables(tables: List[Table])

object TableRegistry {

  sealed trait Command
  final case class GetTables(replyTo: ActorRef[Tables]) extends Command
  final case class AddTable(table: Table, afterId: Int, replyTo: ActorRef[TableAdded]) extends Command
  final case class UpdateTable(table: Table, replyTo: ActorRef[TableUpdated]) extends Command
  final case class RemoveTable(id: Int, replyTo: ActorRef[TableRemoved]) extends Command
  final case class TableAdded(afterId: Int, table: Table)
  final case class TableUpdated(table: Table)
  final case class TableRemoved(id: Int)

  def apply(): Behavior[Command] = registry(Nil)

  def addTable(tables: List[Table], afterId: Int, table: Table): List[Table] = tables match {
    case _ if (afterId == -1) => table +: tables
    case head :: tail if (head.id == afterId) => head :: table :: tail
    case head :: tail if (head.id > afterId) => table :: head :: tail
    case head :: tail if (head.id < afterId) => head :: addTable(tail, afterId, table)
    case Nil => Nil
  }

  private def registry(tables: List[Table]): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetTables(replyTo) =>
        replyTo ! Tables(tables)
        Behaviors.same
      case AddTable(table, afterId, replyTo) =>
        replyTo ! TableAdded(afterId, table)
        registry(addTable(tables, afterId, table))
      case UpdateTable(table, replyTo) =>
        replyTo ! TableUpdated(table)
        registry(tables.map {
          case Table(table.id, _, _) => table
          case x => x
        })
      case RemoveTable(id, replyTo) =>
        replyTo ! TableRemoved(id)
        registry(tables.filter(t => t.id != id))
    }
}

