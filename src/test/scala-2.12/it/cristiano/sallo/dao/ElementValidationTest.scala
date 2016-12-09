package it.cristiano.sallo.dao

import it.cristiano.sallo.common.BaseTest
import it.cristiano.sallo.dao.message.DaoReturnMessage

/**
  * Created by cristiano on 12/7/16.
  */
class ElementValidationTest extends BaseTest{


  "Attribute" must "avoids index key" in {
    val attrError1 = "index=34"
    assert(new ElementValidation(new AttributeValidation(attrError1)).execute == DaoReturnMessage.INVALID_KEY_INDEX)

    val attrError2 = "index"
    assert(new ElementValidation(new AttributeValidation(attrError2)).execute == DaoReturnMessage.INVALID_KEY_INDEX)

    val attrValid1 = "age"
    assert(new ElementValidation(new AttributeValidation(attrValid1)).execute == DaoReturnMessage.SUCCESS)

    val attrValid2 = "age=34"
    assert(new ElementValidation(new AttributeValidation(attrValid2)).execute == DaoReturnMessage.SUCCESS)
  }

  "Line" must "avoids index key" in {
    val lineError1 = "index=34,index,index,name=john,age=123"
    assert(new ElementValidation(new LineValidation(lineError1)).execute == DaoReturnMessage.INVALID_KEY_INDEX)

    val lineError2 = "name=robert,surname=malone,index"
    assert(new ElementValidation(new LineValidation(lineError2)).execute == DaoReturnMessage.INVALID_KEY_INDEX)

    val lineValid1 = "name=robert,surname=malone,name=john,surname=index"
    assert(new ElementValidation(new LineValidation(lineValid1)).execute == DaoReturnMessage.SUCCESS)

    val attrValid2 = "age=34"
    assert(new ElementValidation(new AttributeValidation(attrValid2)).execute == DaoReturnMessage.SUCCESS)
  }

  "Key index" must " evaluate index 3" in{
    val listError = List(
      "index=1,name=robert,surname=malone,name=john,surname=smith",
      "index=2,name=robert,surname=malone,name=john,")
    assert(new ElementValidation(new IndexValidation("3",listError)).execute == DaoReturnMessage.NO_LINE)

//    val listOk = List(
//      "index=1,name=robert,surname=malone,name=john,surname=smith",
//      "index=2,name=robert,surname=malone,name=john",
//      "index=3,name=robert,surname=malone,name=john")
//    assert(new ElementValidation(new IndexValidation("3",listOk)).execute == DaoReturnMessage.SUCCESS)

  }


}
