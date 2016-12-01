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

  }

  /**
    *  Adds index on records if not exists or changes
    *  index's value when isn't at right position.
    * @return
    */
  def fixIndex: DaoReturnMessage.Value = {

    var message = DaoReturnMessage.FAIL
    val lines = CryptoUtils.decrypt(key, encryptFile)

    val listLinesWithIndex = lines.zipWithIndex.map(lineWithIndex => {
      val lineDao = new LineDao(lineWithIndex._1)
      val listIndexInLine = lineDao.get("index")
      if (listIndexInLine.lift(0) == None || listIndexInLine.lift(0).get._2 != 0) {
        message = DaoReturnMessage.CHANGED
        "index=".concat(lineWithIndex._2.toString()).concat(",").concat(lineWithIndex._1)
      }
      else if (listIndexInLine.lift(0).get._2 == 0 && lineWithIndex._2 != listIndexInLine.lift(0).get._1){
        message = DaoReturnMessage.CHANGED
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
    var message = DaoReturnMessage.FAIL
    try {
      val draft = Source.fromFile(filePath).getLines

      val bufLines = lines.toBuffer
      for (line <- draft)
        bufLines += line

      CryptoUtils.encryptList(key, bufLines.toList, encryptFile)
      message = DaoReturnMessage.INSERTED
    } catch {
      case nfe: FileNotFoundException => message = DaoReturnMessage.FILE_NOT_EXIST
    }
    message
  }

  override def addLine(line: String): DaoReturnMessage.Value = {
    val lines = CryptoUtils.decrypt(key,encryptFile)
    val bufLines = lines.toBuffer
    bufLines += line
    CryptoUtils.encryptList(key,bufLines.toList,encryptFile)
    DaoReturnMessage.INSERTED
  }

 /* private def addLineAtIndex(index: String, line: String): Unit ={
    val lines = CryptoUtils.decrypt(key,encryptFile)
    val bufLines = lines.toBuffer
    val lz = bufLines.zipWithIndex
    for (l <- lz){
      println("--------> " + l)
    }


    bufLines += line
    CryptoUtils.encryptList(key,bufLines.toList,encryptFile)
    DaoReturnMessage.INSERTED
  }*/

  override def getLine(index: String): Option[String] = {
    val lines = CryptoUtils.decrypt(key,encryptFile)
    val linesFiltered = lines.filter(l => new LineDao(l).get("index").lift(0).get._1 == index)
    if (linesFiltered.size > 0) Some(linesFiltered.lift(0).get) else None
  }

  override def removeLine(index: String): DaoReturnMessage.Value = {
    val lines = CryptoUtils.decrypt(key,encryptFile)
    val linesFiltered = lines.filterNot(l => new LineDao(l).get("index").lift(0).get._1 == index)
    CryptoUtils.encryptList(key,linesFiltered,encryptFile)
    if (lines.size == linesFiltered.size) DaoReturnMessage.FAIL else DaoReturnMessage.DELETED
  }

  override def insertAttribute(index: String, keyInsert: String, valueInsert: String): DaoReturnMessage.Value = {
    val currentLine = getLine(index)
    if (currentLine == None) {
      DaoReturnMessage.FAIL
    } else {
      val lines = CryptoUtils.decrypt(key,encryptFile)
      val listResult = lines.zipWithIndex.map(lz =>
        if (lz._2.toString == index)
          lz._1.concat(s",$keyInsert=$valueInsert")
        else
          lz._1
      )
      CryptoUtils.encryptList(key,listResult,encryptFile)
      DaoReturnMessage.SUCCESS
    }
  }

  override def updateAttribute(index: String, key: String, valueUpdate: String): DaoReturnMessage.Value = {
    super.updateAttribute(index, key, valueUpdate)
  }

  override def removeAttribute(index: String, keyRemove: String): DaoReturnMessage.Value = {
    super.removeAttribute(index, keyRemove)
  }

}
