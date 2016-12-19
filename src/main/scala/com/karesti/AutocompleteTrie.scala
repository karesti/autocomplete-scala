package com.karesti

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.StringBuilder

/**
  * Automplete R-Trie that suggests a number of words
  * Init R to 256 for Ascii Extended
  **/
class AutocompleteTrie(R: Int = 256) {

  // Each Trie Node
  private class TrieNode {
    var isWord: Boolean = false
    val childs = Array.fill[Option[TrieNode]](R)(None)
  }

  // Keeps track of the word count
  private var wordCount = 0

  // Root node
  private val words: TrieNode = new TrieNode

  def put(word: String) = {
    @tailrec def put(node: TrieNode, word: String, index: Int): Unit = {
      // When we reached the end of the word and the node is not tracked as a word
      if (index == word.length && !node.isWord) {
        node.isWord = true
        wordCount += 1
      } else if (index < word.length) {
        //Get the Node corresponding to te possition
        //TODO Katia: Make possible to keep a smaller caracter list R and convert the char into a valable position
        val nodePosition = word.charAt(index).toInt

        if (node.childs(nodePosition).isEmpty)
        // When the position is None, create and put the new Node
          node.childs.update(nodePosition, Some(new TrieNode))

        //Continue with the next caracter index in the word
        put(node.childs(nodePosition).get, word, index + 1)
      }
    }

    put(words, word, 0)
  }

  def putAll(words: List[String]) = words.foreach(put(_))

  def exists(word: String): Boolean = {
    get(Some(words), word, 0).map(_.isWord).getOrElse(false)
  }

  def size = wordCount

  def all(): List[String] = {
    val results = new mutable.Queue[String]()
    collect(Some(words), new StringBuilder(""), results, wordCount)
    results.toList
  }

  def suggestByPrefix(prefix: String, max: Int): List[String] = {
    val lowerCasePrefix = prefix
    val prefixRoot = get(Some(words), lowerCasePrefix, 0)
    val results = new mutable.Queue[String]()
    collect(prefixRoot, new StringBuilder(lowerCasePrefix), results, max)
    results.toList
  }

  /**
    * Get method points to the TrieNode prefix
    **/
  private def get(maybeNode: Option[TrieNode], prefix: String, d: Int): Option[TrieNode] = {
    maybeNode.map { node =>
      if (prefix.length == d)
        node
      else {
        return get(node.childs(prefix.charAt(d).toInt), prefix, d + 1)
      }
    }
  }

  private def collect(maybeNode: Option[TrieNode],
    prefix: StringBuilder,
    results: mutable.Queue[String],
    max: Int): Unit = {

    maybeNode.map { node =>
      if (node.isWord)
      // When the current node is a word, add it to the results Queue
        results.enqueue(prefix.toString())

      // Stop when we already collected max results
      if (results.length < max) {
        // Collect for every possible child
        for (c <- 0 to R - 1) {
          // Add the caracter corresponding to the Ascii Int position
          prefix.append(c.toChar)
          // recursif call to collect with the next prefix
          collect(node.childs(c), prefix, results, max)
          // back to the previous version of prefix before recursif call
          prefix.deleteCharAt(prefix.length - 1)
        }
      }
    }
  }
}
