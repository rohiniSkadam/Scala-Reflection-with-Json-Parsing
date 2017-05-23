package controllers

import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._

/**
  * Created by synerzip on 23/5/17.
  */
case class ParameterHolder(map: Map[String, String])

object ParameterHolder {

  implicit var mapFormat = new Format[ParameterHolder] {
    def reads(jv: JsValue): JsResult[ParameterHolder] = {
      val map = jv.as[Map[String, String]].map { case (k, v) =>
        k -> v.asInstanceOf[String]
      }
      JsSuccess(ParameterHolder(map))
    }

    def writes(s: ParameterHolder): JsValue = {
      Json.obj(s.map.map { case (k, v) =>
        val ret: (String, JsValueWrapper) = k.toString -> JsString(v)
        ret
      }.toSeq: _*)
    }
  }
}
