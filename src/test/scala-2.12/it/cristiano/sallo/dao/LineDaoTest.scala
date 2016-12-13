package it.cristiano.sallo.dao

import it.cristiano.sallo.common.BaseTest
import it.cristiano.sallo.dao.message.DaoReturnMessage
import org.scalatest.BeforeAndAfter

/**
  * Created by cristiano on 11/29/16.
  */
class LineDaoTest extends BaseTest with BeforeAndAfter{


  /**
    * Test for key not available
    */
  "line" must "be without values" in {
    val line = new LineDao ("key0=value0,key1=value1,key0=value2,key3=value3")
    assert(line.get("key_not_available").size == 0)
  }

  /**
    * Test for get key in line
    */
  "line" must "contain 2 keys 'key0' with index 0 and 2" in {
    val lineDao = new LineDao ("key0=value0,key1=value1,key0=value2,key3=value3")
    val r = lineDao.get("key0")
    assert(r.size == 2)
    val t0 = r.lift(0).get
    assert(t0._1 == "value0" && t0._2 == 0)
    val t1 = r.lift(1).get
    assert(t1._1 == "value2" && t1._2 == 2)
  }

  "line" must "be updated" in {
    val lineDao = new LineDao ("key0=value0,key1=value1,key0=value2,key3=value3")
    val tup = lineDao.update("key0","CHANGE")
    assert(tup._1 == "key0=CHANGE,key1=value1,key0=CHANGE,key3=value3")
    assert(tup._2 == DaoReturnMessage.SUCCESS)
  }

  "line" must "be updated in index 2" in {
    val lineDao = new LineDao ("key0=value0,key1=value1,key0=value2,key3=value3")
    val tup = lineDao.update("key0","CHANGE",2)
    assert(tup._1 == "key0=value0,key1=value1,key0=CHANGE,key3=value3")
    assert(tup._2 == DaoReturnMessage.SUCCESS)
  }

  "line" must "be NOT updated" in {
    val lineDao = new LineDao ("key0=value0,key1=value1,key0=value2,key3=value3")
    val tup = lineDao.update("key1000","CHANGE")
    assert(tup._1 == "key0=value0,key1=value1,key0=value2,key3=value3")
    assert(tup._2 == DaoReturnMessage.NO_ATTRIBUTE_CHANGED)
  }

  "line" must "be NOT updated, key with wrong index" in {
    val lineDao = new LineDao ("key0=value0,key1=value1,key0=value2,key3=value3")
    val tup = lineDao.update("key0","wrongIndex",40)
    assert(tup._1 == "key0=value0,key1=value1,key0=value2,key3=value3")
    assert(tup._2 == DaoReturnMessage.NO_ATTRIBUTE_CHANGED)
  }

  "line" must "be without 2 attributes 'key0=value2' " in {
    val lineDao = new LineDao ("key0=value0,key1=value1,key0=value2,key3=value3")
    val tup = lineDao.removeAttribute("key0")
    assert(tup._1 == "key1=value1,key3=value3")
    assert(tup._2 == DaoReturnMessage.SUCCESS)
  }

}
