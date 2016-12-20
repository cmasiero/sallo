package it.cristiano.sallo.dao

import it.cristiano.sallo.dao.message.DaoReturnMessage

/**
  * Created by cristiano on 12/7/16.
  */
class ElementValidation[T <: Validation](t: T) {
  def execute : DaoReturnMessage.message = t.check
}

sealed trait Validation {
  def check : DaoReturnMessage.message
}

class AttributeValidation(elem: String) extends Validation {
  override def check : DaoReturnMessage.message = {
    elem.split("=").lift(0).get match {
      case "index" => DaoReturnMessage.KEY_INDEX_IS_RESERVED
      case _       => DaoReturnMessage.SUCCESS
    }
  }
}

class LineValidation(elem: String) extends Validation {
  override def check : DaoReturnMessage.message = {
    val r = elem.split(",").filter(attr => attr.split("=").lift(0).get == "index")
    r.length match {
      case 0 => DaoReturnMessage.SUCCESS
      case _ => DaoReturnMessage.KEY_INDEX_IS_RESERVED
    }
  }
}

// TODO: if result is > 1 is better throws an exception
class IndexValidation (indexValue: String, lines: List[String]) extends Validation {
  override def check : DaoReturnMessage.message  = {
    val result = for {
      l <- lines
      elem = l.split(",").lift(0).get.split("=")
      if elem.lift(0).get == "index" && elem.lift(1).get == indexValue
    } yield l

    if (result.size == 1)
      DaoReturnMessage.SUCCESS
    else
      DaoReturnMessage.NO_LINE
  }
}
