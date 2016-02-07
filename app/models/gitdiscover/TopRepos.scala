package models.gitdiscover

import com.datastax.driver.core.ResultSet
import database.cdb
import play.api.libs.json._
import scala.collection.JavaConversions._
import scala.collection.mutable

/**
  * Created by sandeep on 1/31/16.
  */
case class TopRepo(date: String, name: String, eventstotal: Long, language: String)


object TopReposFormat {
  implicit val topRepoFmt = Json.format[TopRepo]
}

object TopRepos {


  val JAVASCRIPT = "JavaScript"
  val CSHARP = "C#"
  val PYTHON = "Python"
  val RUBY = "Ruby"
  val JAVA = "Java"
  val HASKELL = "Haskell"
  val PHP = "PHP"
  val CPLUSPLUS = "C++"
  val HTML = "HTML"
  val CSS = "CSS"
  val PERL = "Perl"
  val OBJECTIVEC = "Objective-C"
  val GO = "Go"
  val CLOJURE = "Clojure"
  val ANSIC = "C"

  val ALL_LANGS = List(JAVASCRIPT, CSHARP, PYTHON, RUBY, JAVA, HASKELL, PHP,
    CPLUSPLUS, HTML, CSS, PERL, OBJECTIVEC, GO, CLOJURE, ANSIC)

  def get = {
    process(cdb.client.session.execute("select * from git.toprepos limit 10000;"))
  }

  def process(rs: ResultSet): Map[String, mutable.PriorityQueue[TopRepo]]  = {
    type LANGUAGE = String
    val topLangs = ALL_LANGS.foldLeft(Map.empty[LANGUAGE, mutable.PriorityQueue[TopRepo]]) {
      (b, a) =>
        b + ((a, new mutable.PriorityQueue[TopRepo]()(repoOrder)))
    }

    for(r <- rs){
      if (topLangs.get(r.getString("language")).nonEmpty) {
        val langPQueue = topLangs(r.getString("language"))

        if (langPQueue.size >= 10) {
          val tpRepo = langPQueue.head
          if (tpRepo.eventstotal < r.getLong("eventstotal")) {
            langPQueue.dequeue() // remove top element
            langPQueue.enqueue(TopRepo(r.getString("date"), r.getString("name"),
              r.getLong("eventstotal"), r.getString("language"))) // add new top element
          }
        } else {
          // check if project already exists
          val project = langPQueue.find(_.name == r.getString("name"))
          if (project.nonEmpty) {
            if (project.get.eventstotal < r.getLong("eventstotal")) {
              langPQueue.foreach(p => if(p.name == r.getString("name")) p.copy(eventstotal = r.getLong("eventstotal")))
            }
          } else {
            langPQueue.enqueue(TopRepo(r.getString("date"),
              r.getString("name"), r.getLong("eventstotal"), r.getString("language")))
          }

        }
      }
    }


    topLangs
  }

  object repoOrder extends Ordering[TopRepo] {
    override def compare(r1: TopRepo, r2: TopRepo) = r1.eventstotal compare r2.eventstotal
  }

}