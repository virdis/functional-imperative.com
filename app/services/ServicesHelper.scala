package services

import scalaz._
import Scalaz._
import play.api.mvc.Result

object ServicesHelper {

  type Error = String
  type SingleResult[A] = EitherT[Option, Error, A]
  
  object SingleResult {
  
    def createFromOption[A](error: => Error)(oa: Option[A]): SingleResult[A] = {
      EitherT((oa \/> error).point[Option])
    }
  }
  
}
