package com.gvolpe.phantom.example

import com.gvolpe.phantom.Connector
import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl._

import scala.concurrent.Future

case class Person(id: Int, name: String)

class Persons extends CassandraTable[PersonsDao, Person] {
  object id extends IntColumn(this) with PartitionKey[Int]
  object name extends StringColumn(this) with PartitionKey[String]

  override def fromRow(row: Row): Person = {
    Person(id(row), name(row))
  }
}

abstract class PersonsDao extends Persons with Connector {
  def find(id: Int): Future[List[Person]] = {
    select.where(_.id eqs id).fetch()
  }
}
