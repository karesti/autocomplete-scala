package com.karesti

import org.scalatest._

class AutocompleteTrieTest extends FunSpec with Matchers with BeforeAndAfter {

  val testWords = List(
    "pandora",
    "pinterest",
    "paypal",
    "pg&e",
    "project free tv priceline",
    "press democrat",
    "progressive",
    "project runway",
    "proactive",
    "programming",
    "progeria",
    "progesterone",
    "progenex",
    "procurable",
    "processor",
    "proud",
    "print",
    "prank",
    "bowl",
    "owl",
    "river",
    "phone",
    "kayak",
    "stamps",
    "reprobe")

  var autocomplete: AutocompleteTrie = _

  before {
    autocomplete = new AutocompleteTrie
    autocomplete.putAll(testWords)
  }

  describe("Autocomplete") {
    it("insert and read all the words sorted asc") {
      autocomplete.all() shouldBe testWords.sorted
    }

    it("autocomplete size and words size should match") {
      autocomplete.size shouldBe testWords.size
    }
    it("exist answers true when word exists and false when not found") {
      testWords.foreach { w =>
        autocomplete.exists(w) shouldBe true
      }
      autocomplete.exists("Pan") shouldBe false
      autocomplete.exists("42") shouldBe false
    }

    it("suggest 4 words") {
      autocomplete.suggestByPrefix("p") shouldBe List("pandora", "paypal", "pg&e", "phone")
      autocomplete.suggestByPrefix("pr") shouldBe List("prank", "press democrat", "print", "proactive")
      autocomplete.suggestByPrefix("pro") shouldBe List("proactive", "processor", "procurable", "progenex")
      autocomplete.suggestByPrefix("prog") shouldBe List("progenex", "progeria", "progesterone", "programming")
    }

    it("one word when just one match") {
      autocomplete.suggestByPrefix("k", 4) shouldBe List("kayak")
    }

    it("empty suggestion when not found") {
      autocomplete.suggestByPrefix("x", 4) shouldBe List()
    }
  }
}
