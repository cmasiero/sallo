package it.cristiano.sallo.dao

import it.cristiano.sallo.dao.message.DaoReturnMessage

/**
  * Created by cristiano on 11/29/16.
  */
class LineDao (line: String) {

  /**
    * It returns a list of tuples containing all
    * values and relative indexes filtered by key.
    * Example :
    * line = "key0=value0,key1=value1,key0=value2,key3=value3"
    * key = "key0"
    * result = List (("value0",0),("value2",2))
    * @param key the key we will get
    * @return List od tuples
    */
  def get(key: String) : List[(String, Int)] = {
    val s = line.split(",").map(_.trim)
    val sZipped = s zipWithIndex
    val resultTmp = sZipped.filter(tuple => {
      tuple._1.split("=").lift(0).get == key
    }).map(tup => (tup._1.split("=").lift(1).get,tup._2))
    resultTmp.toList
  }

  /**
    * It returns the line updated in values for specified key.<br>
    * If you pass index parameter, value will change only in key at that index.<br>
    *
    * <p>Example:<br>
    * line   = "key0=value0,key1=value1,key0=value2,key3=value3"<br>
    * key    = "key0"<br>
    * value  = "CHANGE"<br>
    * index  = 2<br>
    * result = "key0=value0,key1=value1,key0=CHANGE,key3=value3"<br>
    * <p>In case of not specified index result will be:<br>
    * result = "key0=CHANGE,key1=value1,key0=CHANGE,key3=value3"<br>
    * in other words : function will update values at each keys.<br>
    * @param key   key whose value is to be updated.
    * @param value value to be updated
    * @param index index of attribute in record
    * @return (line updated, return message)
    */
  def update(key: String, value: String, index: Int = -1): (String, DaoReturnMessage.Value) = {
    var message = DaoReturnMessage.NO_ATTRIBUTE_CHANGED
    val s = line.split(",").map(_.trim)
    val sZipped = s zipWithIndex
    val tmp = sZipped.map(
      tup =>
        if (tup._1.split("=").lift(0).get == key && index == -1) {
          message = DaoReturnMessage.SUCCESS
          (key.concat("=").concat(value), tup._2)
        }
        else if (tup._1.split("=").lift(0).get == key && index == tup._2) {
          message = DaoReturnMessage.SUCCESS
          (key.concat("=").concat(value), tup._2)
        }
        else
          tup
    )

    val result = tmp.map(tuple => tuple._1).mkString(",")
    (result, message)
  }

  def removeAttribute(key: String): (String, DaoReturnMessage.Value) = {
    var message = DaoReturnMessage.NO_ATTRIBUTE_CHANGED
    val lineSplit = line.split(",").map(_.trim)
    val resultSplit = lineSplit.filterNot(attr => attr.split("=").lift(0).get == key)
    if (resultSplit.length != lineSplit.length) message = DaoReturnMessage.SUCCESS
    (resultSplit.mkString(","),message)
  }



}
