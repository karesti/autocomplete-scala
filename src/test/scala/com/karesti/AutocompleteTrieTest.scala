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
  }

  describe("Autocomplete") {
    it("insert and read all the words sorted asc") {
      autocomplete.putAll(testWords)
      autocomplete.all() shouldBe testWords.sorted
    }

    it("autocomplete size and words size should match") {
      autocomplete.putAll(testWords)
      autocomplete.size shouldBe testWords.size
    }
    it("exist answers true when word exists and false when not found") {
      autocomplete.putAll(testWords)
      testWords.foreach { w =>
        autocomplete.exists(w) shouldBe true
      }
      autocomplete.exists("Pan") shouldBe false
      autocomplete.exists("42") shouldBe false
    }

    it("suggest 4 words") {
      val expectedSuggestion = List("pandora", "paypal", "pg&e", "phone")
      autocomplete.putAll(testWords)
      autocomplete.suggestByPrefix("p", 4) shouldBe expectedSuggestion
    }
  }
}
