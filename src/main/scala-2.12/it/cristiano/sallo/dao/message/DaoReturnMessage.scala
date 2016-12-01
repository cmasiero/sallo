package it.cristiano.sallo.dao.message

/**
  * Created by cristiano on 11/21/16.
  */

object DaoReturnMessage extends Enumeration {
  type message = Value

  val
  INSERTED,
  UPDATED,
  CHANGED,
  DELETED,
  FILE_NOT_EXIST,
  SUCCESS,
  FAIL,
  ERROR = Value
}
