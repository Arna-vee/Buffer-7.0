// dsa/TrieNode.java
package com.intelguard.intelguard.dsa;

import java.util.HashMap;

public class TrieNode {

    HashMap<Character, TrieNode> children = new HashMap<>();

    boolean isEnd = false;
}