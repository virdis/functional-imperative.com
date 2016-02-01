package services

import models.gitdiscover.SimpleCassandraClient

/**
  * Created by sandeep on 1/31/16.
  */
object SimpleCConnector extends SimpleCassandraClient(node = "54.68.122.113")
