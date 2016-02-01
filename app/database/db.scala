package database

import models.gitdiscover.SimpleCassandraClient

/**
 * Created by svirdi on 12/31/14.
 */
object db extends DBHelper

object cdb {
  lazy val client = new SimpleCassandraClient("52.89.176.195")
}
