package it.cristiano.sallo.dao

/**
  * Created by cristiano on 11/21/16.
  */
trait GenericDao {

  def count  = 0
  def getAll = List[String]()
  def getByMatch(pattern: String) : Option[List[String]] = None

}
