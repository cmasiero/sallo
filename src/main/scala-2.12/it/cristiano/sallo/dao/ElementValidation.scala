package it.cristiano.sallo.dao

import it.cristiano.sallo.dao.message.DaoReturnMessage

/**
  * Created by cristiano on 12/7/16.
  */
class ElementValidation[T <: Validation](t: T) {
  def execute(elem: String): DaoReturnMessage.message = t.check(elem)
}

object ElementValidation {
//  def validation(valType: Validation, value: String): Boolean = {
//    val v = new ElementValidation(valType).execute(value)
//    if (v.result == DaoReturnMessage.ERROR) {
//      println(v.description)
//      false
//    } else true
//  }
}


//case class ValidationMessage(message: DaoReturnMessage.message, description: String)

sealed trait Validation {
  def check(elem: String): DaoReturnMessage.message
}

object AttributeValidation extends Validation {
  override def check(elem: String): DaoReturnMessage.message = {
    elem.split("=").lift(0).get match {
      case "index" => DaoReturnMessage.INVALID_KEY_INDEX
      case _       => DaoReturnMessage.SUCCESS
    }
  }
}

object LineValidation extends Validation {
  override def check(elem: String): DaoReturnMessage.message = {
    val r = elem.split(",").filter(attr => attr.split("=").lift(0).get == "index")
    r.size match {
      case 0 => DaoReturnMessage.SUCCESS
      case _ => DaoReturnMessage.INVALID_KEY_INDEX
    }
  }
}
