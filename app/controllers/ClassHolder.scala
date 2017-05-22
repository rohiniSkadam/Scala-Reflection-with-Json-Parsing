package controllers

import play.api.libs.json._

/**
  * Created by synerzip on 16/5/17.
  */
case class ClassHolder(classType: String,params: Map[String,String])

object ClassHolder {
  implicit val classHolderFormat = Json.format[ClassHolder]
}
