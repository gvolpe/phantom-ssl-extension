package com.gvolpe.phantom

import java.security.SecureRandom
import javax.net.ssl.{SSLContext, TrustManager}

import com.datastax.driver.core.Cluster.Builder
import com.datastax.driver.core.{Cluster, PlainTextAuthProvider, SSLOptions, Session}
import com.typesafe.config.ConfigFactory
import com.websudos.phantom.connectors.{Cache, KeySpaceDef, SessionProvider}
import com.websudos.phantom.dsl._

import scala.collection.JavaConverters._

class CustomSessionProvider(builder: Builder) extends SessionProvider {
  private val sessionCache = new Cache[String, Session]

  override lazy val cluster: Cluster = builder.build()

  protected[this] def createSession(keySpace: String): Session = cluster.connect

  override def getSession(keySpace: String): Session = {
    sessionCache.getOrElseUpdate(keySpace, createSession(keySpace))
  }
}

trait Connector {
  implicit def space: KeySpace
  implicit def session: Session
}

trait AppConfiguration {
  val config = ConfigFactory.load()
}

object CassandraConnection extends AppConfiguration {
  val keyspace = config.getString("cassandra.keyspace")
  val username = config.getString("cassandra.username")
  val password = config.getString("cassandra.password")
  val hosts = config.getStringList("cassandra.hosts").asScala

  val provider = {
    val builder = Cluster.builder().addContactPoints(hosts)
      .withAuthProvider(new PlainTextAuthProvider(username, password))
      .withSSL(trustAllWithoutCertOptions)
    new CustomSessionProvider(builder)
  }

  val manager = new KeySpaceDef(keyspace, provider)

  private def trustAllWithoutCertOptions: SSLOptions = {
    val context = SSLContext.getInstance("TLS")
    context.init(null, Array[TrustManager](new TrustAllManager()), new SecureRandom)
    new SSLOptions(context, SSLOptions.DEFAULT_SSL_CIPHER_SUITES)
  }
}
