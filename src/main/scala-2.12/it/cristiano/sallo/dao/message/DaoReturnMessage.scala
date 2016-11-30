package it.cristiano.sallo.dao.message

/**
  * Created by cristiano on 11/21/16.
  */

object DaoReturnMessage extends Enumeration {
  type message = Value
  val INSERTED, UPDATED, CHANGE, DELETED, ERROR, NOTHING = Value
}
