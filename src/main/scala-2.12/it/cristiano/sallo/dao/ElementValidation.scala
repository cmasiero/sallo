package it.cristiano.sallo.dao

import it.cristiano.sallo.dao.message.DaoReturnMessage

/**
  * Created by cristiano on 12/7/16.
  */
class ElementValidation[T <: Validation](t: T) {
  def execute(): DaoReturnMessage.message = t.check
}

sealed trait Validation {
  def check(): DaoReturnMessage.message
}

class AttributeValidation(elem: String) extends Validation {
  override def check : DaoReturnMessage.message = {
    elem.split("=").lift(0).get match {
      case "index" => DaoReturnMessage.INVALID_KEY_INDEX
      case _       => DaoReturnMessage.SUCCESS
    }
  }
}

class LineValidation(elem: String) extends Validation {
  override def check : DaoReturnMessage.message = {
    val r = elem.split(",").filter(attr => attr.split("=").lift(0).get == "index")
    r.size match {
      case 0 => DaoReturnMessage.SUCCESS
      case _ => DaoReturnMessage.INVALID_KEY_INDEX
    }
  }
}

class IndexValidation (elem: String, lines: List[String]) extends Validation {
  // TODO : this method
  override def check : DaoReturnMessage.message  = {
    val result = lines.iterator.filter(contain(_))
    DaoReturnMessage.NO_LINE
  }

  def contain (line: String) : Boolean = {
    println("----------- " + line)
    true
  }
}

