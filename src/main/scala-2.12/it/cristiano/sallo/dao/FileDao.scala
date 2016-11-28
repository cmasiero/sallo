package it.cristiano.sallo.dao

import java.io._

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
    var inputChannel = new FileInputStream(src).getChannel()
    var outputChannel = new FileOutputStream(dest).getChannel()
    outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
    inputChannel.close()
    outputChannel.close()

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

    val decFileTmp = encryptFile + ".tmp"

    val lines = CryptoUtils.decrypt(key,encryptFile)
    val bufLines = lines.toBuffer
    bufLines += line

    val fileTmp = new File(decFileTmp)
    val bw = new BufferedWriter(new FileWriter(fileTmp))
    for (line <- bufLines)
      bw.write(line + System.getProperty("line.separator"))
    bw.close()

    CryptoUtils.encrypt(key,decFileTmp,encryptFile)

    new File(decFileTmp).delete()

    DaoReturnMessage.INSERTED

  }

}


