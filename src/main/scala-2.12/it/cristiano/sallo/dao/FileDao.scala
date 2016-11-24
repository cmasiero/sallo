package it.cristiano.sallo.dao

import it.cristiano.sallo.util.CryptoUtils

import scala.io.Source
import scala.util.control.Exception._
/**
  * Created by cristiano on 11/21/16.
  */
class FileDao (key: String, fileName: String) extends GenericDao{

  /**
    * Check if file exists.
    */
  Source.fromFile(fileName)

  override def count: Int = {
    val dc = CryptoUtils.decrypt(key,fileName)
    dc.size
  }

  override def getAll: List[String] =
    CryptoUtils.decrypt(key,fileName)

  override def getByMatch(str: String): List[String] = {
    val r = CryptoUtils.decrypt(key,fileName)
    r.filter(l => l.contains(str))
  }

}


