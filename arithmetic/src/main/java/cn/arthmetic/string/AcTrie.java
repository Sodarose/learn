package cn.arthmetic.string;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author 26016
 * @description AC自动机
 * @date 2023-12-20 11:36
 **/
public class AcTrie {

    /**
     * 字典文件
     */
    private File file;

    /**
     * 根节点
     */
    private TrieNode root;

    /**
     * 字典单词总数
     */
    private int wordCount;

    public AcTrie(File file) {
        this.file = file;
        this.root = new TrieNode();
    }

    public static void main(String[] args) throws Throwable {
        URI sensitiveUri = AcTrie.class.getClassLoader().getResource("sensitive_words_lines.txt").toURI();
        URI bookUri = AcTrie.class.getClassLoader().getResource("凡人修真传.txt").toURI();
        AcTrie trie = new AcTrie(Paths.get(sensitiveUri).toFile());
        trie.init();
        FileReader fileReader = new FileReader(Paths.get(bookUri).toFile(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);
        while (reader.ready()) {
            String word = reader.readLine();
            trie.patternMatch(word);
        }
        reader.close();
    }

    /**
     * 初始化
     */
    public void init() throws Throwable {
        if (file == null || !Files.exists(file.toPath())) {
            throw new FileNotFoundException("文件未找到!");
        }
        scanFileAndBuildTree();
        buildFailNode();
    }

    /**
     * 扫描文件并构建字典树
     */
    private void scanFileAndBuildTree() throws IOException {
        FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(fileReader);
        while (reader.ready()) {
            String word = reader.readLine();
            TrieNode trieNode = root;
            char[] chars = word.toCharArray();
            for (char c : chars) {
                if (trieNode.childMap.get(c) == null) {
                    trieNode.childMap.put(c, new TrieNode(trieNode, c));
                }
                trieNode = trieNode.childMap.get(c);
                trieNode.prefix++;
            }
            trieNode.count++;
            wordCount++;
        }
        reader.close();
    }

    /**
     * 根据字典树构建失配节点
     */
    private void buildFailNode() {
        if (root == null || root.childMap.isEmpty()) {
            return;
        }
        LinkedList<TrieNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            // 弹出队首
            TrieNode trieNode = queue.pollFirst();
            trieNode.childMap.forEach((key, child) -> {
                // 如果是父节点是root,则子节点的fail指向root
                if (trieNode == root) {
                    child.fail = root;
                } else {
                    // 从下往上找
                    TrieNode temp = trieNode.fail;
                    while (temp != null) {
                        if (temp.childMap.containsKey(key)) {
                            child.fail = temp;
                            break;
                        } else {
                            temp = temp.fail;
                        }
                    }
                    // 未找到失配节点,则指向root
                    if (temp == null) {
                        child.fail = root;
                    }
                }
                queue.add(child);
            });
        }
    }

    /**
     * 模式匹配
     *
     * @param word 单词
     * @return 返回符合模式匹配的单词
     */
    public void patternMatch(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }
        char[] chars = word.toCharArray();
        Set<TrieNode> sets = new HashSet<>();
        TrieNode p = root;
        for (char c : chars) {
            while (!p.childMap.containsKey(c) && p != root) {
                p = p.fail;
            }
            if (p.childMap.containsKey(c)) {
                p = p.childMap.get(c);
            } else {
                p = root;
            }
            TrieNode temp = p;
            while (temp != root) {
                if (temp.count > 0) {
                    sets.add(temp);
                }
                temp = temp.fail;
            }
        }
        if (sets.isEmpty()) {
            return;
        }
        AtomicReference<String> atomicWord = new AtomicReference<>(word);
        sets.forEach(trieNode -> {
            String w = transformWord(trieNode);
            String temp = atomicWord.get().replaceAll(w, "**");
            atomicWord.set(temp);
        });
        System.err.println(atomicWord.get() + "\t");
    }

    private String transformWord(TrieNode trieNode) {
        if (trieNode.count <= 0) {
            throw new RuntimeException("not end node!");
        }
        StringBuilder stringBuilder = new StringBuilder();
        TrieNode temp = trieNode;
        while (temp != root) {
            stringBuilder.insert(0, temp.c);
            temp = temp.parent;
        }
        return stringBuilder.toString();
    }


    /**
     * 字典节点
     */
    private static class TrieNode {

        /**
         * 父节点
         */
        private TrieNode parent;

        /**
         * 总数
         */
        private int count;

        /**
         * 前缀总数
         */
        private int prefix;

        /**
         * 失配节点
         */
        private TrieNode fail;

        /**
         * 单词
         */
        private char c;

        /**
         * 字典子节点
         */
        private Map<Character, TrieNode> childMap = new HashMap<>(16);

        public TrieNode() {

        }

        public TrieNode(char c) {
            this.c = c;
        }

        public TrieNode(TrieNode parent, char c) {
            this.parent = parent;
            this.c = c;
        }
    }
}
