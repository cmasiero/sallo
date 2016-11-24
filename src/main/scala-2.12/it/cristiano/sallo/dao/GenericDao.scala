package it.cristiano.sallo.dao

/**
  * Created by cristiano on 11/21/16.
  */

object DaoReturnMessage extends Enumeration {
  type message = Value
  val INSERTED, UPDATED, DELETED, ERROR, NOTHING = Value
}

trait GenericDao {

  def count  = 0
  def getAll = List[String]()
  def getByMatch(pattern: String) : List[String] = List[String]()

  def addLine(line: String) : DaoReturnMessage.Value = DaoReturnMessage.NOTHING

}

