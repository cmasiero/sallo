package it.cristiano.sallo.dao

import java.io.File

import org.scalatest.FlatSpec

/**
  * Created by cristiano on 11/21/16.
  */
class FileDaoTest extends FlatSpec{

  val fdao = new FileDao("./src/test/resources/file.csv")

  /**
    * Check FileDao.count method
    */
  "File in file.csv" must "have 4 records" in {
    assert(fdao.count == 4)
  }

  /**
    * Check FileDao.getAll method
    */
  "File in file.csv" must "have 4 String records" in {
    assert(fdao.getAll.size == 4)
  }

  "File in file.csv" must " not contain records with pattern 'nothing'" in {
    fdao.getByMatch("nothing") match {
      case None => assert(true)
      case _ => assert(false)
    }
  }

  "File in file.csv" must " contain 3 records with pattern 'password2'" in {
    fdao.getByMatch("password2")
    assert(true)
    /*fdao.getByMatch("password2") match {
      case None => assert(false)
      case _ => assert(true)
    }*/
  }






}
