package it.cristiano.sallo.dao

import it.cristiano.sallo.dao.message.DaoReturnMessage


trait GenericDao {

  def count = 0

  def getAll = List[String]()

  def getByMatch(pattern: String): List[String] = List[String]()

  def addFile(filePath: String): DaoReturnMessage.Value = DaoReturnMessage.NOTHING

  def addLine(line: String): DaoReturnMessage.Value = DaoReturnMessage.NOTHING

  def removeLine(index: String): DaoReturnMessage.Value = DaoReturnMessage.NOTHING

  def insertAttributeAfter(index: String, keyInsert: String, valueInsert: String, keyAttributeAfter: String): DaoReturnMessage.Value = DaoReturnMessage.NOTHING

  def updateAttribute(index: String, key: String, valueUpdate: String): DaoReturnMessage.Value = DaoReturnMessage.NOTHING

  def removeAttribute(index: String, keyRemove: String): DaoReturnMessage.Value = DaoReturnMessage.NOTHING

}

