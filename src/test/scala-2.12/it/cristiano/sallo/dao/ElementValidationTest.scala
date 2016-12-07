package it.cristiano.sallo.dao

import it.cristiano.sallo.common.BaseTest
import it.cristiano.sallo.dao.message.DaoReturnMessage

/**
  * Created by cristiano on 12/7/16.
  */
class ElementValidationTest extends BaseTest{


  "Attribute" must "avoids index key" in {
    val attrError1 = "index=34"
    assert(new ElementValidation(AttributeValidation).execute(attrError1) == DaoReturnMessage.INVALID_KEY_INDEX)

    val attrError2 = "index"
    assert(new ElementValidation(AttributeValidation).execute(attrError2) == DaoReturnMessage.INVALID_KEY_INDEX)

    val attrValid1 = "age"
    assert(new ElementValidation(AttributeValidation).execute(attrValid1) == DaoReturnMessage.SUCCESS)

    val attrValid2 = "age=34"
    assert(new ElementValidation(AttributeValidation).execute(attrValid2) == DaoReturnMessage.SUCCESS)
  }

  "Line" must "avoids index key" in {
    val lineError1 = "index=34,index,index,name=john,age=123"
    assert(new ElementValidation(LineValidation).execute(lineError1) == DaoReturnMessage.INVALID_KEY_INDEX)

    val lineError2 = "name=robert,surname=malone,index"
    assert(new ElementValidation(LineValidation).execute(lineError2) == DaoReturnMessage.INVALID_KEY_INDEX)

    val lineValid1 = "name=robert,surname=malone,name=john,surname=index"
    assert(new ElementValidation(LineValidation).execute(lineValid1) == DaoReturnMessage.SUCCESS)
//
//    val attrValid2 = "age=34"
//    assert(new ElementValidation(AttributeValidation).execute(attrValid2).result == ResultType.VALID)
  }


}
