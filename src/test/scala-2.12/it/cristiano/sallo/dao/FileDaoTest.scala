package it.cristiano.sallo.dao

import java.io.{File, FileNotFoundException}

import org.scalatest.FlatSpec

/**
  * Created by cristiano on 11/21/16.
  */
class FileDaoTest extends FlatSpec{

  val fdao = new FileDao("./src/test/resources/file.csv")

  /**
    * Check if file exists.
    */
  it must "produce FileNotFoundException when file does not exits!" in {
    assertThrows[FileNotFoundException] {
      new FileDao("./src/test/resources/noFile.csv")
    }
  }

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

  /**
    * When there is no match getByMatch return None.
    */
  "File in file.csv" must " not contain records with pattern 'no_match'" in {
    assert(fdao.getByMatch("no_match") == None)
  }

  "File in file.csv" must " contain 2 records with pattern 'password2'" in {
    assert(fdao.getByMatch("password2").get.size == 2)
  }




}
