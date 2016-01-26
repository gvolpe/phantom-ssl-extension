package com.gvolpe

import java.net.Socket
import java.security.cert.X509Certificate
import javax.net.ssl.{SSLEngine, X509ExtendedTrustManager}

import com.datastax.driver.core.Cluster.Builder

package object phantom {

  implicit class CassandraClusterBuilderOps(builder: Builder) {
    def addContactPoints(nodes: Seq[String]): Builder = {
      nodes foreach { builder.addContactPoint(_) }
      builder
    }
  }

  private[cassandra] class TrustAllManager extends X509ExtendedTrustManager {
    override def checkClientTrusted(x509Certificates: Array[X509Certificate], s: String, socket: Socket): Unit = {}

    override def checkClientTrusted(x509Certificates: Array[X509Certificate], s: String, sslEngine: SSLEngine): Unit = {}

    override def checkServerTrusted(x509Certificates: Array[X509Certificate], s: String, socket: Socket): Unit = {}

    override def checkServerTrusted(x509Certificates: Array[X509Certificate], s: String, sslEngine: SSLEngine): Unit = {}

    override def getAcceptedIssuers: Array[X509Certificate] = null

    override def checkClientTrusted(x509Certificates: Array[X509Certificate], s: String): Unit = {}

    override def checkServerTrusted(x509Certificates: Array[X509Certificate], s: String): Unit = {}
  }

}
