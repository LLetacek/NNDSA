package nndsa.sem.b.tree;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import javafx.util.Pair;

/**
 *
 * @author ludek
 */
public class SuffixTrie<K extends CharSequence, V> implements ITrie<K, V> {

    private Node root;
    private int counter;

    public SuffixTrie() {
        this.root = new Node(null, null);
        counter = 0;
    }

    @Override
    public void clear() {
        counter = 0;
        root.children.clear();
    }

    @Override
    public void add(K key, V value) {
        if(key == null || key.length()==0)
            throw new IllegalArgumentException("Can't add empty word!");

        int lastIndex = key.length() - 1;

        Pair<Integer, Node> findLastKnownSubstring = findLastNodeWithIndex(key);
        Node node = findLastKnownSubstring.getValue();
        int suffixCounter = findLastKnownSubstring.getKey();

        if (suffixCounter == key.length()) {
            if (SuffixTrie.this.isWord(node)) {
                throw new IllegalArgumentException("Already have " + key);
            } else {
                node.value = value;
                ++counter;
            }
            return;
        }

        Node parent = node;
        while (suffixCounter != key.length()) {
            CharSequence characterSequence = key.subSequence(lastIndex - suffixCounter, key.length());
            Node newNode = new Node(characterSequence, (suffixCounter == lastIndex) ? value : null, parent);
            parent.children.put(Character.toLowerCase(characterSequence.charAt(0)), newNode);
            parent = newNode;
            ++suffixCounter;
        }
        ++counter;
    }

    @Override
    public V remove(K key) {
        Node toRemove = findWord(key);
        if (!toRemove.children.isEmpty()) {
            V value = toRemove.value;
            toRemove.value = null;
            --counter;
            return value;
        }

        Node parent = toRemove.parent;
        Character childrenKey = toRemove.getFirstCharOfKey();

        while (parent != null) {
            parent.children.remove(childrenKey);
            if (!parent.children.isEmpty() || SuffixTrie.this.isWord(parent) || parent == root) {
                break;
            }
            childrenKey = parent.getFirstCharOfKey();
            parent = parent.parent;
        }
        --counter;
        return toRemove.value;
    }

    @Override
    public boolean isEmpty() {
        return root.children.isEmpty();
    }

    @Override
    public V getValue(K word) {
        return findWord(word).value;
    }

    private Node findWord(K word) {
        Pair<Integer, Node> nodeWithIndex = findLastNodeWithIndex(word);
        if (nodeWithIndex.getKey() == word.length()) {
            if (!SuffixTrie.this.isWord(nodeWithIndex.getValue())) {
                throw new IllegalArgumentException("It is not word!");
            }
            return nodeWithIndex.getValue();
        } else {
            throw new IndexOutOfBoundsException("I don't know this word!");
        }
    }

    private Pair<Integer, Node> findLastNodeWithIndex(K key) {
        Node node = root;
        int suffixCounter = 0;
        if(key.length()==0) 
            return new Pair<>(suffixCounter, node);
        
        int lastIndex = key.length() - 1;

        Node result;
        do {
            result = node;
            node = node.children.get(Character.toLowerCase(key.charAt(lastIndex - suffixCounter)));
            ++suffixCounter;

            if (node != null && suffixCounter == key.length()) {
                result = node;
                break;
            } else if (node == null) {
                --suffixCounter;
            }
        } while (node != null);

        return new Pair<>(suffixCounter, result);
    }

    @Override
    public List<V> getValuesEndingWithKey(K key) {
        Pair<Integer, Node> lastNode = findLastNodeWithIndex(key);
        if(key != null && key.length() != lastNode.getKey())
            return new LinkedList<>();
        
        Node node = (key == null || key.length() == 0) ? root : lastNode.getValue();
        Set<V> list = new LinkedHashSet<>();

        Stack<Node> stack = new Stack<>();
        stack.add(node);
        while (!stack.empty()) {
            node = stack.pop();
            for (Node item : node.children.values()) {
                stack.add(item);
            }

            if (isWord(node)) {
                list.add(node.value);
            }
        }

        return new LinkedList<>(list);
    }

    private boolean isWord(Node node) {
        return node.value != null && node != root;
    }

    @Override
    public int size() {
        return counter;
    }

    private class Node {

        Node parent;
        CharSequence key;
        V value;
        HashMap<Character, Node> children;

        public Node(CharSequence uniqKeyForTree, V value) {
            this(uniqKeyForTree, value, null);
        }

        public Node(CharSequence uniqKeyForTree, V value, Node parent) {
            this.key = uniqKeyForTree;
            this.value = value;
            this.children = new HashMap<>();
            this.parent = parent;
        }

        private boolean isNullOrEmpty(CharSequence charSequence) {
            return charSequence == null || charSequence.toString().isEmpty();
        }

        public Character getFirstCharOfKey() {
            return Character.toLowerCase(key.charAt(0));
        }
    }
}
