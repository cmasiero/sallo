package it.cristiano.sallo.dao

import it.cristiano.sallo.dao.message.DaoReturnMessage


trait GenericDao {

  def count = 0

  def getAll: List[String] = List[String]()

  def getByMatch(pattern: String): List[String] = List[String]()

  def addFile(filePath: String): DaoReturnMessage.Value = DaoReturnMessage.ERROR

  def addLine(line: String): DaoReturnMessage.Value = DaoReturnMessage.ERROR

  def getLine(index: String): (String, DaoReturnMessage.Value) = ("",DaoReturnMessage.ERROR)

  def removeLine(index: String): DaoReturnMessage.Value = DaoReturnMessage.ERROR

  def insertAttribute(index: String, keyInsert: String, valueInsert: String): DaoReturnMessage.Value = DaoReturnMessage.ERROR

  def updateAttribute(index: String, keyUpdate: String, valueUpdate: String): DaoReturnMessage.Value = DaoReturnMessage.ERROR

  def removeAttribute(index: String, keyRemove: String): DaoReturnMessage.Value = DaoReturnMessage.ERROR

}

