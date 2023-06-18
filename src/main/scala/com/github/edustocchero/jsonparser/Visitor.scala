package com.github.edustocchero.jsonparser

import com.github.edustocchero.jsonparser.Strings.removeQuotes

import java.lang.Boolean.parseBoolean
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class Visitor extends JsonBaseVisitor[Json] {

  override def visitJsonObject(ctx: JsonParser.JsonObjectContext): Json = {
    val contextPairs = ctx.jsonPair
    val map = new mutable.HashMap[String, Json]
    contextPairs.forEach(pairContext => {
      val pair = visitJsonPair(pairContext).asInstanceOf[JsonPair]
      val (key, value) = pair.value
      map.put(key, value)
    })
    JsonObject(map)
  }

  override def visitJsonPair(ctx: JsonParser.JsonPairContext): Json = {
    val key = ctx.key.getText
    val value = visit(ctx.value)

    JsonPair((removeQuotes(key), value))
  }

  override def visitEmptyArray(ctx: JsonParser.EmptyArrayContext): Json =
    JsonArray(ArrayBuffer.empty)

  override def visitSomeArray(ctx: JsonParser.SomeArrayContext): Json = {
    val values = ctx.json()
    val arrayBuffer = new ArrayBuffer[Json]()
    values.forEach(value => {
      arrayBuffer.addOne(visit(value))
    })
    JsonArray(arrayBuffer)
  }

  override def visitJsonString(ctx: JsonParser.JsonStringContext): Json = {
    val string = ctx.STRING().getText
    JsonString(removeQuotes(string))
  }

  override def visitJsonNumber(ctx: JsonParser.JsonNumberContext): Json = {
    val text = ctx.NUMBER().getText
    val number = BigDecimal(text)
    JsonNumber(number)
  }

  override def visitJsonBoolean(ctx: JsonParser.JsonBooleanContext): Json = {
    val booleanText = ctx.getText
    val bool = parseBoolean(booleanText)
    JsonBoolean(bool)
  }

  override def visitJsonNull(_ctx: JsonParser.JsonNullContext): Json = JsonNull()
}
