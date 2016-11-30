package it.cristiano.sallo.dao

import java.io._

import it.cristiano.sallo.dao.message.DaoReturnMessage
import it.cristiano.sallo.util.CryptoUtils

import scala.io.Source
import scala.util.control.Exception._
/**
  * Created by cristiano on 11/21/16.
  */
class FileDao (key: String, encryptFile: String) extends GenericDao{

  init

  def init: Unit ={

    //Check if file exists.
    Source.fromFile(encryptFile)

    // Backup input file
    val src = encryptFile
    val dest = encryptFile + ".bac"
    val inputChannel = new FileInputStream(src).getChannel()
    val outputChannel = new FileOutputStream(dest).getChannel()
    outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
    inputChannel.close()
    outputChannel.close()

    val message = fixIndex
    println("[FileDao:init] : fix index message " + message)

  }

  /**
    *  Adds index on records if not exists or changes
    *  index's value when isn't at right position.
    * @return
    */
  private def fixIndex: DaoReturnMessage.Value = {

    var message = DaoReturnMessage.NOTHING
    val lines = CryptoUtils.decrypt(key, encryptFile)
    val list = lines.map(l => l.concat(System.getProperty("line.separator")))
    val listLinesWithIndex = list.zipWithIndex.map(lineWithIndex => {
      val lineDao = new LineDao(lineWithIndex._1)
      val listIndexInLine = lineDao.get("index")
      if (listIndexInLine.lift(0) == None || listIndexInLine.lift(0).get._2 != 0) {
        message = DaoReturnMessage.CHANGE
        "index=".concat(lineWithIndex._2.toString()).concat(",").concat(lineWithIndex._1)
      }
      else if (listIndexInLine.lift(0).get._2 == 0 && lineWithIndex._2 != listIndexInLine.lift(0).get._1){
        //println( "pos in line:" + listIndexInLine.lift(0).get._2  + ",real index:" + lineWithIndex._2 + ",in file index:" + listIndexInLine.lift(0).get._1)
        message = DaoReturnMessage.CHANGE
        lineDao.update("index", lineWithIndex._2.toString, 0)._1
      }
      else lineWithIndex._1
    })
    CryptoUtils.encryptList(key,listLinesWithIndex.toList,encryptFile)
    message
  }

  override def count: Int = {
    val dc = CryptoUtils.decrypt(key,encryptFile)
    dc.size
  }

  override def getAll: List[String] =
    CryptoUtils.decrypt(key,encryptFile)

  override def getByMatch(str: String): List[String] = {
    val r = CryptoUtils.decrypt(key,encryptFile)
    r.filter(l => l.contains(str))
  }


  override def addFile(filePath: String): DaoReturnMessage.Value = {
    val lines = CryptoUtils.decrypt(key, encryptFile)
    val draft = Source.fromFile(filePath).getLines
    val bufLines = lines.toBuffer
    for (line <- draft)
      bufLines += line

    val list = for {
      line <- bufLines
    } yield (line + System.getProperty("line.separator"))

    CryptoUtils.encryptList(key, list.toList, encryptFile)
    DaoReturnMessage.INSERTED
  }


  override def addLine(line: String): DaoReturnMessage.Value = {
    val lines = CryptoUtils.decrypt(key,encryptFile)
    val bufLines = lines.toBuffer
    bufLines += line

    val list = for {
        line <- bufLines
    } yield (line + System.getProperty("line.separator"))

    CryptoUtils.encryptList(key,list.toList,encryptFile)

    DaoReturnMessage.INSERTED
  }

  override def removeAttribute(index: String, keyRemove: String): _root_.it.cristiano.sallo.dao.message.DaoReturnMessage.Value = super.removeAttribute(index, keyRemove)

  override def insertAttributeAfter(index: String, keyInsert: String, valueInsert: String, keyAttributeAfter: String): _root_.it.cristiano.sallo.dao.message.DaoReturnMessage.Value = super.insertAttributeAfter(index, keyInsert, valueInsert, keyAttributeAfter)

  override def updateAttribute(index: String, key: String, valueUpdate: String): _root_.it.cristiano.sallo.dao.message.DaoReturnMessage.Value = super.updateAttribute(index, key, valueUpdate)

  override def removeLine(index: String): _root_.it.cristiano.sallo.dao.message.DaoReturnMessage.Value = super.removeLine(index)

}


