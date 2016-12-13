package it.cristiano.sallo.dao.message

/**
  * Created by cristiano on 11/21/16.
  */

object DaoReturnMessage extends Enumeration {
  type message = Value

  val
  SUCCESS,
  FILE_NOT_EXIST,
  NO_ATTRIBUTE_CHANGED,
  NO_LINE,
  KEY_INDEX_IS_RESERVED,
  ERROR = Value
}
