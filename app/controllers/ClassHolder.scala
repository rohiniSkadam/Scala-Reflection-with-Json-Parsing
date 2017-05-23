package controllers

import play.api.libs.json._

/**
  * Created by synerzip on 16/5/17.
  */
case class ClassHolder(classType: String, params: List[ParameterHolder])

object ClassHolder {
  implicit var implicitRoverWrites = new Format[ClassHolder] {
    def writes(s: ClassHolder): JsValue = {
      Json.obj("classType" -> JsString(s.classType))
    }

    def reads(json: JsValue): JsResult[ClassHolder] = {
      val classType = (json \ "classType").as[String]
      val params: List[ParameterHolder] = (json \ "params").as[JsArray].value.map(r => r.as[ParameterHolder]).toList
      JsSuccess(ClassHolder(classType, params))
    }
  }
}
