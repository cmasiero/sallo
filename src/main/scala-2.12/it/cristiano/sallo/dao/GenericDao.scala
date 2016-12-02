package it.cristiano.sallo.dao

import it.cristiano.sallo.dao.message.DaoReturnMessage


trait GenericDao {

  def count = 0

  def getAll = List[String]()

  def getByMatch(pattern: String): List[String] = List[String]()

  def addFile(filePath: String): DaoReturnMessage.Value = DaoReturnMessage.FAIL

  def addLine(line: String): DaoReturnMessage.Value = DaoReturnMessage.FAIL

  def getLine(index: String): Option[String] = None

  def removeLine(index: String): DaoReturnMessage.Value = DaoReturnMessage.FAIL

  def insertAttribute(index: String, keyInsert: String, valueInsert: String): DaoReturnMessage.Value = DaoReturnMessage.FAIL

  def updateAttribute(index: String, keyUpdate: String, valueUpdate: String): DaoReturnMessage.Value = DaoReturnMessage.FAIL

  def removeAttribute(index: String, keyRemove: String): DaoReturnMessage.Value = DaoReturnMessage.FAIL

}

