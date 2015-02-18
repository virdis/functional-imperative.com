package util

import scala.io.Source
import play.api.Play
/**
 * Created by svirdi on 1/11/15.
 */
object Utils {

  val userName = if (Play.current.mode == play.api.Mode.Prod)
    Source.fromFile("/home/sandeep/user.properties") getLines() find(_.startsWith("username")) map(_.replace("username=",""))
  else
    Source.fromFile("conf/user.properties") getLines() find(_.startsWith("username")) map(_.replace("username=",""))

}
