package it.cristiano.sallo.dao

import scala.io.Source
import scala.util.control.Exception._
/**
  * Created by cristiano on 11/21/16.
  */
class FileDao (fileName: String) extends GenericDao{

  /**
    * Check if file exists.
    */
  Source.fromFile(fileName)


  override def count: Int =
    Source.fromFile(fileName).getLines().size


  override def getAll: List[String] =
    Source.fromFile(fileName).getLines.toList


  override def getByMatch(str: String): Option[List[String]] = {
    val result = Source.fromFile(fileName).getLines.toList.filter(p => p.contains(str))
    result match {
      case List() => None
      case _      => Option(result)
    }
  }

}
