package com.gvolpe.phantom.example

import com.gvolpe.phantom.CassandraConnection
import com.websudos.phantom.connectors.KeySpaceDef
import com.websudos.phantom.dsl.Database

object PersonService extends PersonService(CassandraConnection.manager)

abstract class PersonService(val keyspace: KeySpaceDef) extends Database(keyspace) {
  object persons extends PersonsDao with keyspace.Connector
}
