# autocomplete-scala
Autocomplete coded in scala implemented as a R-Trie (Ascii extended)

## Going further

R-Trie are very straightforward, but they consume extra espace due to the array allocation.
A better solution for memory usage will be to use a TST (Ternary Search Trie) which is not
very difficult to implement but more complex than R-Trie.

## Additional questions

I'm assuming I can't use Lucene / Elastic Search for the following answers.

##### What would you change if the list of keywords was large (several millions)?

It depends on the keywords. If indexed keywords have very similar prefixes, maybe using the compressed type of 
Trie can be just fine. 

As a simple solution, I would try to keep in memory only the most suggested words. When suggested values are less than 4, 
we will try to find on more words in the external system (database, file system).

Suggested words can be tracked in memory with a LRU cache implemented with a DoubleLinkedList.
The TrieNode will point to the LinkedListNode that tracks words values. 
Delete method should also be implemented in the Trie.

##### What would you change if the requirements were to match any portion of the keywords 

I would start with the brute force solution to implement a "contains" method, but there are other algorithms out
there I should probablu study better in order to implement substring search with regular expressions and 
DFA (Deterministic Finite Automation)(I only know the existence of these algorithms)