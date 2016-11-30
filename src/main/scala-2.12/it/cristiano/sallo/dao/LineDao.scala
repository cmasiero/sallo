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
  def get(key: String) : List[Tuple2[String,Int]] = {
    val s = line.split(",").map(_.trim)
    val sZipped = s zipWithIndex
    val resultTmp = sZipped.filter(tuple => {
      tuple._1.split("=").lift(0).get == key
    }).map(tup => (tup._1.split("=").lift(1).get,tup._2))
    resultTmp.toList
  }

  /**
    * It returns the line updated in values for specified key.
    * If you pass index parameter, value will change
    * only in key at that index.
    * Example:
    * line   = "key0=value0,key1=value1,key0=value2,key3=value3"
    * key    = "key0"
    * value  = "CHANGE"
    * index  = 2
    * result = "key0=value0,key1=value1,key0=CHANGE,key3=value3"
    * In case of not specified index result will be:
    * result = "key0=CHANGE,key1=value1,key0=CHANGE,key3=value3"
    * in other words : function will update values at each keys.
    * @param key
    * @param value
    * @param index
    * @return (line updated, return message)
    */
  def update(key: String, value: String, index: Int = -1): Tuple2[String, DaoReturnMessage.Value] = {
    var message = DaoReturnMessage.NOTHING
    val s = line.split(",").map(_.trim)
    val sZipped = s zipWithIndex
    val tmp = sZipped.map(
      tup =>
        if (tup._1.split("=").lift(0).get == key && index == -1) {
          message = DaoReturnMessage.UPDATED
          (key.concat("=").concat(value), tup._2)
        }
        else if (tup._1.split("=").lift(0).get == key && index == tup._2) {
          message = DaoReturnMessage.UPDATED
          (key.concat("=").concat(value), tup._2)
        }
        else
          tup
    )

    val result = tmp.map(tuple => tuple._1).mkString(",")
    (result, message)
  }

}
