package meetups.meet02

import org.slf4j.LoggerFactory
import slick.jdbc.SQLiteProfile.api._
import slick.jdbc.JdbcBackend.Database

import java.sql.SQLException
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

/**
 *
 * @author Siarhei Bahdanchuk
 *         Date: 25.11.2021
 *
 */

/**
 * CREATE TABLE "en"(
 * "title" TEXT,
 * "url" TEXT,
 * "abstract" TEXT,
 * "body_text" TEXT,
 * "body_html" TEXT
 * )
 */

case class Book(title: String, url: String, abstrct: String, bodyText: String, bodyHtml: String)

class Books(tag: Tag) extends Table[Book](tag, "en") {
  def title = column[String]("title")

  def url = column[String]("url")

  def abstrct = column[String]("abstract")

  def body_text = column[String]("body_text")

  def body_html = column[String]("body_html")

  override def * = (title, url, abstrct, body_text, body_html) <> (Book.tupled, Book.unapply)
}

object LoadData extends App {
  val log = LoggerFactory.getLogger("LoadData")
  val books = TableQuery[Books]
  val db = Database.forConfig("books")

  val all = books.result
  val filteredQuery = books.filter(_.title startsWith ("Wiki"))

  try {
    log.info("Get something from en books")
    val allFuture = db.run[Seq[Book]](filteredQuery.result)
    allFuture.onComplete {
      case Success(s) => {
        log.info(s"Got ${s.size} books")
      }
      case Failure(cause) => {
        log.info(s"An error has occurred $cause")
      }
    }
    Await.ready(allFuture, Duration.Inf)
    log.info(s"end ${allFuture.isCompleted}")
  } finally db.close
}
