package com.karesti

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.StringBuilder

/**
  * Automplete R-Trie that suggests a number of words
  **/
class AutocompleteTrie(R: Int = 256) {

  private class TrieNode {
    var isWord: Boolean = false
    val childs = Array.fill[Option[TrieNode]](R)(None)
  }

  private var wordCount = 0
  private val words: TrieNode = new TrieNode

  def put(word: String) = {
    @tailrec def put(node: TrieNode, word: String, index: Int): Unit = {
      if (index == word.length && !node.isWord) {
        node.isWord = true
        wordCount += 1
      } else if (index < word.length) {
        val nodePosition = word.charAt(index).toInt

        if (node.childs(nodePosition).isEmpty)
          node.childs.update(nodePosition, Some(new TrieNode))
        put(node.childs(nodePosition).get, word, index + 1)
      }
    }

    put(words, word.toLowerCase, 0)
  }

  def putAll(words: List[String]) = words.foreach(put(_))

  def exists(word: String): Boolean = {
    get(Some(words), word.toLowerCase, 0).map(_.isWord).getOrElse(false)
  }

  def size = wordCount

  def all(): List[String] = {
    val results = new mutable.Queue[String]()
    collect(Some(words), new StringBuilder(""), results, wordCount)
    results.toList
  }

  def suggestByPrefix(prefix: String, max: Int): List[String] = {
    val lowerCasePrefix = prefix.toLowerCase
    val prefixRoot = get(Some(words), lowerCasePrefix, 0)
    val results = new mutable.Queue[String]()
    collect(prefixRoot, new StringBuilder(lowerCasePrefix), results, max)
    results.toList
  }

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

    if (max == results.length) return

    maybeNode match {
      case Some(node) =>
        if (node.isWord) results.enqueue(prefix.toString())
        for (c <- 0 to R - 1) {
          prefix.append(c.toChar)
          collect(node.childs(c), prefix, results, max)
          prefix.deleteCharAt(prefix.length - 1)
        }
      case _ =>
    }
  }
}
