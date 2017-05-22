package controllers

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.reflect.runtime.{universe => ru}

case class Student(id:String,name:String,address:String){
  println("Student Instance Created")
}
case class Teacher(name:String,address:String){
  println("Teacher Instance Created")
}

object Application extends Controller{
  def studentDetails = Action.async { request =>
    val jsonRequest = request.body.asJson.get
    val classHolders = (jsonRequest \ "data").as[List[ClassHolder]]
    classHolders.foreach(data=>{
      ClassInst(data.classType,data.params)
    })
    Future(Ok(Json.toJson(classHolders)))
  }
}

object ClassInst {
  val runtimeMirror = ru.runtimeMirror(getClass.getClassLoader)
  def apply(clsName: String, params: Map[String, String]) = {
    val clsNm = Class.forName(clsName)

    val classSymbol = runtimeMirror.classSymbol(clsNm)
    val classMirror = runtimeMirror.reflectClass(classSymbol)

    val theType2: ru.Type = getType(clsNm)
    val methodSymbol = theType2.declaration(ru.nme.CONSTRUCTOR).asMethod
    val methodMirror = classMirror.reflectConstructor(methodSymbol)

    if(params.get("id").isEmpty){
      val mirror= methodMirror("Name","Address")
      val nameField=fieldSymbol(mirror,"name")
      nameField.set(params("name"))
      val addressField=fieldSymbol(mirror,"address")
      addressField.set(params("address"))
      println(mirror)
    }
    else {
      val mirror= methodMirror("Id","Name","Address")
      val idField = fieldSymbol(mirror, "id")
      idField.set(params("id"))
      val nameField=fieldSymbol(mirror,"name")
      nameField.set(params("name"))
      val addressField=fieldSymbol(mirror,"address")
      addressField.set(params("address"))
      println(mirror)
    }
  }

  def fieldSymbol(mirror: Any, fieldName:String):_root_.scala.reflect.runtime.universe.FieldMirror  =
  {
    val instanceMirror = runtimeMirror.reflect(mirror)
    val theType: ru.Type = getType(mirror.getClass)
    val termSymbol = theType.declaration(ru.newTermName(fieldName)).asTerm.accessed.asTerm
    instanceMirror.reflectField(termSymbol)
  }
    def getType[T](clazz: Class[T]): ru.Type = {
      val runtimeMirror = ru.runtimeMirror(clazz.getClassLoader)
      runtimeMirror.classSymbol(clazz).toType
    }

}

