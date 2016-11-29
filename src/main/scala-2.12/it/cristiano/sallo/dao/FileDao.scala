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

    //Add index on records if not exists
    addIndex

  }

  private def addIndex : DaoReturnMessage.Value = {
    val lines = CryptoUtils.decrypt(key,encryptFile)
    val list = lines.map(l => l.concat(System.getProperty("line.separator")))
    val listWithIndex = lines.zipWithIndex.map(elem => {
      val listTuple = new LineDao(elem._1).get("index")
      if (listTuple.lift(0) == None) {
        "index=".concat(elem._2.toString()).concat(",").concat(elem._1)
      }
    })

    for (nLine <- listWithIndex)
      println("*" + nLine)

    CryptoUtils.encryptList(key,list.toList,encryptFile)
    DaoReturnMessage.INSERTED
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

}


