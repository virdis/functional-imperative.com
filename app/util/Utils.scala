package util

import scala.io.Source

/**
 * Created by svirdi on 1/11/15.
 */
object Utils {

  val userName = Source.fromFile("conf/user.properties") getLines() find(_.startsWith("username")) map(_.replace("username=",""))

}
