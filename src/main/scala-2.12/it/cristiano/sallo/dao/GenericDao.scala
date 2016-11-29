package it.cristiano.sallo.dao

import it.cristiano.sallo.dao.message.DaoReturnMessage


trait GenericDao {

  def count  = 0
  def getAll = List[String]()
  def getByMatch(pattern: String) : List[String] = List[String]()

  def addLine(line: String) : DaoReturnMessage.Value = DaoReturnMessage.NOTHING

}

