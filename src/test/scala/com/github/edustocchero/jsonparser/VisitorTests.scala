package com.github.edustocchero.jsonparser

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ArrayBuffer

class VisitorTests extends AnyWordSpec with should.Matchers {
  "visitor.visit(null)" should {
    "return Json with value ()" in {
      val content = "null"
      val parser = parserFromString(content)

      val jsonNull = parser.jsonNull()
      val json = new Visitor().visit(jsonNull)

      assert(json.isInstanceOf[JsonNull])
      json() shouldBe ()
    }
  }
  "visitor.visit(string)" should {
    "return Json with value foo" in {
      val content = "\"foo\""
      val parser = parserFromString(content)

      val string = parser.jsonString()
      val jsonString = new Visitor().visit(string)
      jsonString() shouldBe "foo"
    }
  }

  "visitor.visit(number)" should {
    "return Json with value BigDecimal(0)" in {
      val parser = parserFromString("0")

      val number = parser.jsonNumber()
      val jsonNumber = new Visitor().visit(number)
      jsonNumber() shouldBe BigDecimal("0")
    }
    "return Json with value BigDecimal(-1)" in {
      val parser = parserFromString("-1")

      val number = parser.jsonNumber()
      val jsonNumber = new Visitor().visit(number)
      jsonNumber() shouldBe BigDecimal("-1")
    }
    "return Json with value BigDecimal(3.1415)" in {
      val parser = parserFromString("3.1415")

      val number = parser.jsonNumber()
      val jsonNumber = new Visitor().visit(number)
      jsonNumber() shouldBe BigDecimal("3.1415")
    }
    "return Json with value BigDecimal(1.23e2)" in {
      val parser = parserFromString("1.23e2")

      val number = parser.jsonNumber()
      val jsonNumber = new Visitor().visit(number)
      jsonNumber() shouldBe BigDecimal("1.23e2")
    }
    "return Json with value BigDecimal(-12.3E-2)" in {
      val parser = parserFromString("-12.3E-2")

      val number = parser.jsonNumber()
      val jsonNumber = new Visitor().visit(number)
      jsonNumber() shouldBe BigDecimal("-12.3E-2")
    }
  }

  "visitor.visit(boolean)" should {
    "return Json with value false" in {
      val parser = parserFromString("false")

      val boolean = parser.jsonBoolean()
      val jsonBoolean = new Visitor().visit(boolean)
      jsonBoolean() shouldBe false
    }
    "return Json with value true" in {
      val parser = parserFromString("true")

      val boolean = parser.jsonBoolean()
      val jsonBoolean = new Visitor().visit(boolean)

      assert(jsonBoolean.isInstanceOf[JsonBoolean])
      jsonBoolean() shouldBe true
    }
  }

  "visitor.visit(array)" should {
    "return Json with value ArrayBuffer.empty" in {
      val parser = parserFromString("[]")

      val arrayBuffer = parser.jsonArray()
      val jsonArray = new Visitor().visit(arrayBuffer)
      jsonArray() shouldBe new ArrayBuffer[Json].empty
    }
    "return Json with value ArrayBuffer(JsonNumber)" in {
      val parser = parserFromString("[42]")

      val arrayBuffer = parser.jsonArray()
      val json = new Visitor().visit(arrayBuffer)
      json() shouldBe ArrayBuffer[Json](
        JsonNumber(BigDecimal("42"))
      )
    }
    "return Json with value ArrayBuffer(JsonNumber, JsonString)" in {
      val parser = parserFromString("[42, \"hello\"]")

      val arrayBuffer = parser.jsonArray()
      val json = new Visitor().visit(arrayBuffer)
      json() shouldBe ArrayBuffer[Json](
        JsonNumber(BigDecimal("42")),
        JsonString("hello")
      )
    }
    "return Json with value ArrayBuffer(JsonObject.empty)" in {
      val parser = parserFromString("[{}]")

      val arrayBuffer = parser.jsonArray()
      val json = new Visitor().visit(arrayBuffer)
      json() shouldBe ArrayBuffer[Json](JsonObject.empty)
    }
    "return Json with value ArrayBuffer(JsonNull, JsonNull)" in {
      val parser = parserFromString("[null, null]")

      val arrayBuffer = parser.jsonArray()
      val json = new Visitor().visit(arrayBuffer)
      json() shouldBe ArrayBuffer[Json](
        JsonNull(),
        JsonNull()
      )
    }
  }

  "visitor.visit(pair)" should {
    "return Json with pair (x, JsonNumber)" in {
      val parser = parserFromString("\"x\": 42")

      val jsonPair = parser.jsonPair()
      val json = new Visitor().visit(jsonPair)
      json() shouldBe ("x", JsonNumber(BigDecimal("42")))
    }
    "return Json with pair (foo, JsonArray.empty)" in {
      val parser = parserFromString("\"foo\" : []")

      val jsonPair = parser.jsonPair()
      val json = new Visitor().visit(jsonPair)
      json() shouldBe ("foo", JsonArray.empty)
    }
    "return Json with pair (obj, JsonObject.empty)" in {
      val parser = parserFromString("\"obj\" : {}")

      val jsonPair = parser.jsonPair()
      val json = new Visitor().visit(jsonPair)
      json() shouldBe ("obj", JsonObject.empty)
    }
    "return Json with pair (null, JsonNull)" in {
      val parser = parserFromString("\"null\"    :    null")

      val jsonPair = parser.jsonPair()
      val json = new Visitor().visit(jsonPair)
      json() shouldBe ("null", JsonNull())
    }
  }

  private def parserFromString(s: String): JsonParser = {
    val lexer = new JsonLexer(CharStreams.fromString(s))
    val tokenStream = new CommonTokenStream(lexer)
    new JsonParser(tokenStream)
  }
}
