package com.github.edustocchero.jsonparser

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Try

sealed trait Json {
  def apply(): Any = ()

  def asJsonObject(): Option[JsonObject] =
    Try(this.asInstanceOf[JsonObject]).toOption
}

case class JsonNull() extends Json

case class JsonString(value: String) extends Json {
   override def apply(): String = value
}

case class JsonNumber(value: BigDecimal) extends Json {
  override def apply(): BigDecimal = value
}

case class JsonBoolean(value: Boolean) extends Json {
  override def apply(): Boolean = value
}

case class JsonArray(value: ArrayBuffer[Json]) extends Json {
  override def apply(): ArrayBuffer[Json] = value
}
object JsonArray {
  def empty: JsonArray = JsonArray(ArrayBuffer.empty)
}

case class JsonPair(value: (String, Json)) extends Json {
  override def apply(): (String, Json) = value
}

case class JsonObject(value: mutable.Map[String, Json]) extends Json {
  override def apply(): mutable.Map[String, Json] = value
}
object JsonObject {
  def empty: JsonObject = JsonObject(mutable.Map.empty)
}
