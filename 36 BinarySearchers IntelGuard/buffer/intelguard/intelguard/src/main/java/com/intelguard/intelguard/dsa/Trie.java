// dsa/Trie.java
package com.intelguard.intelguard.dsa;

public class Trie {

    private TrieNode root = new TrieNode();

    public void insert(String word) {

        TrieNode current = root;

        for(char ch : word.toCharArray()) {

            current.children.putIfAbsent(ch,new TrieNode());

            current = current.children.get(ch);
        }

        current.isEnd = true;
    }

    public boolean search(String word) {

        TrieNode current = root;

        for(char ch : word.toCharArray()) {

            if(!current.children.containsKey(ch))
                return false;

            current = current.children.get(ch);
        }

        return current.isEnd;
    }
}