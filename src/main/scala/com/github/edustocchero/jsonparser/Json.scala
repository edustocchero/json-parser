package com.github.edustocchero.jsonparser

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

trait Json {
  def apply(): Any = ()
  def asJsonObject(): JsonObject = this.asInstanceOf[JsonObject]
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

case class JsonArray(value: Option[ArrayBuffer[Json]]) extends Json {
  override def apply(): Option[ArrayBuffer[Json]] = value
}

case class JsonPair(value: (String, Json)) extends Json {
  override def apply(): (String, Json) = value
}

case class JsonObject(value: mutable.Map[String, Json]) extends Json {
  override def apply(): mutable.Map[String, Json] = value
}
