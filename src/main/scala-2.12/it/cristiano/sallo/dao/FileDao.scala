package it.cristiano.sallo.dao

import scala.io.Source

/**
  * Created by cristiano on 11/21/16.
  */
class FileDao (fileName: String) extends GenericDao{

  override def count: Int =
    Source.fromFile(fileName).getLines().size


  override def getAll: List[String] =
    Source.fromFile(fileName).getLines.toList


  override def getByMatch(pattern: String): Option[List[String]] = {
    super.getByMatch(pattern)
//    val result = Source.fromFile(fileName).getLines match {
//      case _ =>
//    } yield result


  }

}
