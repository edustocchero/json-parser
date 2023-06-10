package com.github.edustocchero.jsonparser

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import org.scalatest.matchers.should
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.mutable.ArrayBuffer

class VisitorTests extends AnyWordSpec with should.Matchers {
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
      jsonBoolean() shouldBe true
    }
  }

  "visitor.visit(array)" should {
    "return Json with value ArrayBuffer()" in {
      val parser = parserFromString("[]")

      val arrayBuffer = parser.jsonArray()
      val jsonArray = new Visitor().visit(arrayBuffer)
      jsonArray() shouldBe new ArrayBuffer[Json].empty
    }
    "return Json with value ArrayBuffer[Json...]" in {
      val parser = parserFromString("[42]")

      val arrayBuffer = parser.jsonArray()
      val json = new Visitor().visit(arrayBuffer)
      json() shouldBe ArrayBuffer[Json] {
        JsonNumber(BigDecimal("42"))
      }
    }
  }

  private def parserFromString(s: String): JsonParser = {
    val lexer = new JsonLexer(CharStreams.fromString(s))
    val tokenStream = new CommonTokenStream(lexer)
    new JsonParser(tokenStream)
  }
}
