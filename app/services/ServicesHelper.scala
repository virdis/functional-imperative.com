package services

import util.Utils

import scala.concurrent.Future
import scalaz._
import Scalaz._
import play.api.mvc.Result

object ServicesHelper {

  type Error = String
  type SingleResult[A] = EitherT[Option, Error, A]
  type CollectionResult[A] = Error \/ List[A]

  object SingleResult {
    def createFromOption[A](error: => Error)(oa: Option[A]): SingleResult[A] = {
      EitherT((oa \/> error).point[Option])
    }
  }

  object CollectionResult {
    def createFromList[A](error: => Error)(la: List[A]): CollectionResult[A] = {
      if (la.isEmpty) -\/(error) else \/-(la)
    }
  }

}
