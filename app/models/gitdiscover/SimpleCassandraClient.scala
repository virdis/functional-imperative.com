package models.gitdiscover

import play.api.Logger
import com.datastax.driver.core._
import scala.collection.JavaConversions._
import play.api.Logger
/**
  * Created by sandeep on 1/30/16.
  */
case class SimpleCassandraClient(node: String) {


  private val cluster = Cluster.builder()
    .addContactPoint(node)
    .build()

  log(cluster.getMetadata())

  val session = cluster.connect()

  private def log(metadata: Metadata): Unit = {
    Logger.info(s"Connected to cluster: ${metadata.getClusterName}")
    for (host <- metadata.getAllHosts()) {
      Logger.info(s"Datatacenter: ${host.getDatacenter()}; Host: ${host.getAddress()}; Rack: ${host.getRack()}")
    }
  }

  def shutDownCluster = {
    session.close()
    cluster.close()
  }
}


