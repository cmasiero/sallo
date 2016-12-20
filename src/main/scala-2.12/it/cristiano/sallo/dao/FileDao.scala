package it.cristiano.sallo.dao

import java.io._
import java.nio.file.{Files, Paths}
import java.security.GeneralSecurityException

import it.cristiano.sallo.dao.message.DaoReturnMessage
import it.cristiano.sallo.util.CryptoUtils
import scala.util.control.Breaks._

import scala.io.Source
/**
  * Created by cristiano on 11/21/16.
  */

@throws(classOf[GeneralSecurityException])
@throws(classOf[FileNotFoundException])
class FileDao (key: String, encryptFile: String, boolFixIndex : Boolean = false) extends GenericDao{


  init()
  def init(): Unit ={

    //Check if file exists.
    Source.fromFile(encryptFile)

    // Backup input file
    val src = encryptFile
    val dest = encryptFile + ".bac"
    val inputChannel = new FileInputStream(src).getChannel
    val outputChannel = new FileOutputStream(dest).getChannel
    outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
    inputChannel.close()
    outputChannel.close()

    /*
     * Try to decrypt, in that manner problems throw
     * SecurityException managed by client.
     */
    val listResult = CryptoUtils.decrypt(key, encryptFile)

    if (boolFixIndex)
      CryptoUtils.encryptList(key,listResult,encryptFile)

  }

  /**
    *  Adds index on records if not exists or changes
    *  index's value when isn't at right position.
    * @return The list with index attributes in sequence.
    */
  private def fixIndex (lines: List[String]) : List[String] = {

    val listLinesWithIndex = lines.zipWithIndex.map(lineWithIndex => {
      val lineDao = new LineDao(lineWithIndex._1)
      val listIndexInLine = lineDao.get("index")
      if (listIndexInLine.lift(0).isEmpty || listIndexInLine.lift(0).get._2 != 0) {
        "index=".concat(lineWithIndex._2.toString).concat(",").concat(lineWithIndex._1)
      }
      else if (listIndexInLine.lift(0).get._2 == 0 && lineWithIndex._2 != listIndexInLine.lift(0).get._1.toInt){
        lineDao.update("index", lineWithIndex._2.toString, 0)._1
      }
      else lineWithIndex._1
    })
    listLinesWithIndex

//    val listLinesWithIndex = lines.zipWithIndex.map(lineWithIndex => {
//      val lineDao = new LineDao(lineWithIndex._1)
//      val listIndexInLine = lineDao.get("index")
//      if (listIndexInLine.lift(0) == None || listIndexInLine.lift(0).get._2 != 0) {
//        "index=".concat(lineWithIndex._2.toString).concat(",").concat(lineWithIndex._1)
//      }
//      else if (listIndexInLine.lift(0).get._2 == 0 && lineWithIndex._2 != listIndexInLine.lift(0).get._1.toInt){
//        lineDao.update("index", lineWithIndex._2.toString, 0)._1
//      }
//      else lineWithIndex._1
//    })
//    listLinesWithIndex
  }

  /**
    * Number of records archived.
    * @return Number of record.
    */
  override def count: Int = {
    val dc = CryptoUtils.decrypt(key,encryptFile)
    dc.size
  }

  /**
    * It returns all records
    * @return return all records
    */
  override def getAll: List[String] =
    CryptoUtils.decrypt(key,encryptFile)

  /**
    * It returns all records that match input string,
    * both, key and value, are valued.
    * @param str : Matched string
    * @return    : List of record
    */
  override def getByMatch(str: String): List[String] = {
    val r = CryptoUtils.decrypt(key,encryptFile)
    r.filter(l => l.contains(str))
  }

  /**
    * It concats input file to current archive.
    * @param filePath Input file
    * @return DaoReturnMessage.SUCCESS: successfully completed operation.
    *         DaoReturnMessage.INVALID_KEY_INDEX: There is a line with key named index, it is a reserved key (Error)
    *         DaoReturnMessage.FILE_NOT_EXIST: Input file does not exist (Error)
    */
  override def addFile(filePath: String): DaoReturnMessage.Value = {
    val lines = CryptoUtils.decrypt(key, encryptFile)
    var message = DaoReturnMessage.SUCCESS
    try {
      val draft = Source.fromFile(filePath).getLines
      val bufLines = lines.toBuffer
      for (line <- draft) {
        bufLines += line
        if (new ElementValidation(new LineValidation(line)).execute == DaoReturnMessage.KEY_INDEX_IS_RESERVED)
          return DaoReturnMessage.KEY_INDEX_IS_RESERVED
      }
      CryptoUtils.encryptList(key, fixIndex(bufLines.toList), encryptFile)
    } catch {
      case _: FileNotFoundException => message = DaoReturnMessage.FILE_NOT_EXIST
    }
    message
  }

  /**
    * Add line at the end of current archive.
    * @param line The line to add
    * @return DaoReturnMessage.SUCCESS: successfully completed operation.
    *         DaoReturnMessage.INVALID_KEY_INDEX: There is a key named index, it is a reserved key (Error)
    */
  override def addLine(line: String): DaoReturnMessage.Value = {
    val lines = CryptoUtils.decrypt(key,encryptFile)
    val bufLines = lines.toBuffer
    val message = new ElementValidation(new LineValidation(line)).execute
    message match {
      case DaoReturnMessage.SUCCESS =>
        bufLines += line
        CryptoUtils.encryptList(key,fixIndex(bufLines.toList),encryptFile)
      case _ =>
    }
    message
  }

  override def getLine(index: String): (String, DaoReturnMessage.Value) = {
    val lines = CryptoUtils.decrypt(key, encryptFile)
    val message = new ElementValidation(new IndexValidation(index, lines)).execute
    message match {
      case DaoReturnMessage.SUCCESS =>
        val listFiltered = lines.filter(l => new LineDao(l).get("index").lift(0).get._1 == index)
        (listFiltered.lift(0).get, message)
      case _ => ("", message)
    }
  }

  override def removeLine(index: String): DaoReturnMessage.Value = {
    val lines = CryptoUtils.decrypt(key,encryptFile)
    val message = new ElementValidation(new IndexValidation(index, lines)).execute
    message match {
      case DaoReturnMessage.SUCCESS =>
        val linesFiltered = lines.filterNot(l => new LineDao(l).get("index").lift(0).get._1 == index)
        CryptoUtils.encryptList(key,fixIndex(linesFiltered),encryptFile)
      case _ =>
    }
    message
  }

  override def insertAttribute(index: String, keyInsert: String, valueInsert: String): DaoReturnMessage.Value = {
    val currentLine = getLine(index)
    val message = currentLine._2
    message match {
      case DaoReturnMessage.SUCCESS =>
        val lines = CryptoUtils.decrypt(key, encryptFile)
        val listResult = lines.zipWithIndex.map(lz =>
          if (lz._2.toString == index)
            lz._1.concat(s",$keyInsert=$valueInsert")
          else
            lz._1
        )
        CryptoUtils.encryptList(key, listResult, encryptFile)
      case _ =>
    }
    message
  }

  override def updateAttribute(index: String, keyUpdate: String, valueUpdate: String): DaoReturnMessage.Value = {
    val currentLine = getLine(index)
    var message = currentLine._2
    message match {
      case DaoReturnMessage.SUCCESS =>
        val lines = CryptoUtils.decrypt(key, encryptFile)
        val listResult = lines.zipWithIndex.map(lz =>
          if (lz._2.toString == index) {
            val tup = new LineDao(lz._1.toString).update(keyUpdate, valueUpdate)
            message = tup._2
            tup._1
          }
          else {
            lz._1
          }
        )
        CryptoUtils.encryptList(key, listResult, encryptFile)
      case _ =>
    }
    message
  }

  override def removeAttribute(index: String, keyRemove: String): DaoReturnMessage.Value = {
    val currentLine = getLine(index)
    var message = currentLine._2
    message match {
      case DaoReturnMessage.SUCCESS =>
        val lines = CryptoUtils.decrypt(key, encryptFile)
        val listResult = lines.zipWithIndex.map(lz =>
          if (lz._2.toString == index) {
            val tup = new LineDao(lz._1.toString).removeAttribute(keyRemove)
            message = tup._2
            tup._1
          }
          else {
            lz._1
          }
        )
        CryptoUtils.encryptList(key, listResult, encryptFile)
      case _ =>
    }
    message
  }
}
