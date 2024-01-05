package cn.arthmetic.string;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 26016
 * @description 字典树
 * @date 2023-12-20 11:36
 **/
public class Trie {

    /**
     * 字典单词总数
     */
    private int wordCount;

    /**
     * 根节点
     */
    private TrieNode root;

    public Trie() {
        wordCount = 0;
        root = new TrieNode();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static void main(String[] args) {
        Trie trie = Trie.newBuilder()
                .scanFile("C:\\Users\\26016\\IdeaProjects\\learn\\arithmetic\\src\\main\\java\\org\\arthmetic\\string\\sensitive_words_lines.txt")
                .build();
        System.err.println("总数:" + trie.getWordCount());
        System.err.println("数量:" + trie.search("习近平"));
        trie.delete("习近平");
        System.err.println("总数:" + trie.getWordCount());
        System.err.println("数量:" + trie.search("习近平"));
        System.err.println("前缀数量:" + trie.searchPrefix("胡锦"));
        trie.deletePrefix("胡锦");
        System.err.println("总数:" + trie.getWordCount());
        System.err.println("前缀数量:" + trie.searchPrefix("胡锦"));
    }

    /**
     * 插入一个单词
     *
     * @param word 单词
     */
    public void insert(String word) {
        if (word == null || word.length() == 0) {
            return;
        }
        TrieNode trieNode = root;
        char[] chars = word.toCharArray();
        for (char c : chars) {
            int index = transFromIndex(c);
            if (trieNode.childMap.get(index) == null) {
                trieNode.childMap.put(index, new TrieNode());
            }
            trieNode = trieNode.childMap.get(index);
            trieNode.prefix++;
        }
        trieNode.count++;
        wordCount++;
    }

    public void delete(String word) {
        delete(word, false);
    }

    public void deletePrefix(String world) {
        delete(world, true);
    }

    /**
     * 删除一个单词
     *
     * @param word          单词
     * @param includePrefix 是否包括以这个单词为前缀的
     */
    protected void delete(String word, boolean includePrefix) {
        if (word == null || word.length() == 0) {
            return;
        }
        // 单词链
        TrieNode trieNode = root;
        char[] chars = word.toCharArray();
        TrieNode[] links = new TrieNode[chars.length];
        for (int i = 0; i < chars.length; i++) {
            int index = transFromIndex(chars[i]);
            if (trieNode.childMap.get(index) == null) {
                return;
            }
            trieNode = trieNode.childMap.get(index);
            links[i] = trieNode;
        }
        int deleteNum = includePrefix ? trieNode.prefix : trieNode.count;
        trieNode.count = 0;
        // 从后往前依次遍历单词链,当i=0时,其父节点为root
        for (int i = links.length - 1; i >= 0; i--) {
            TrieNode node = links[i];
            node.prefix -= deleteNum;
            if (node.prefix <= 0) {
                int index = transFromIndex(chars[i]);
                TrieNode preNode = i == 0 ? root : links[i - 1];
                preNode.childMap.remove(index);
            }
        }
        wordCount -= deleteNum;
    }

    /**
     * 搜索字符串
     *
     * @param word 单词
     * @return 返回字符串个数 -1表示不存在
     */
    public int search(String word) {
        if (word == null || word.length() == 0) {
            return -1;
        }
        TrieNode trieNode = root;
        char[] chars = word.toCharArray();
        for (char c : chars) {
            int index = transFromIndex(c);
            if (trieNode.childMap.get(index) == null) {
                return -1;
            }
            trieNode = trieNode.childMap.get(index);
        }
        return trieNode.count <= 0 ? -1 : trieNode.count;
    }

    /**
     * 搜索以该单词为前缀的字符串个数
     *
     * @param word 单词
     */
    public int searchPrefix(String word) {
        if (word == null || word.length() == 0) {
            return -1;
        }
        TrieNode trieNode = root;
        char[] chars = word.toCharArray();
        for (char c : chars) {
            int index = transFromIndex(c);
            if (trieNode.childMap.get(index) == null) {
                return -1;
            }
            trieNode = trieNode.childMap.get(index);
        }
        if (trieNode.prefix == 0) {
            return -1;
        }
        return trieNode.prefix;
    }


    public int getWordCount() {
        return wordCount;
    }

    /**
     * 字符转换索引Index
     *
     * @param c 字符
     */
    private int transFromIndex(char c) {
        return c;
    }

    /**
     * 字典节点
     */
    private static class TrieNode {

        /**
         * 当前单词总数
         */
        private int count = 0;

        /**
         * 已当前单词前缀总数
         */
        private int prefix = 0;

        /**
         * 字典子节点,采用map方式,避免数组过大
         */
        private Map<Integer, TrieNode> childMap = new HashMap<>();

    }

    public static class Builder {

        /**
         * 文件
         */
        private File file;

        public Builder scanFile(String pathStr) {
            Path path = Paths.get(pathStr);
            return scanFile(path);
        }

        public Builder scanFile(Path path) {
            if (!Files.exists(path)) {
                throw new RuntimeException("file not exits !");
            }
            this.file = path.toFile();
            return this;
        }

        public Trie build() {
            try {
                Trie trie = new Trie();
                FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(fileReader);
                while (reader.ready()) {
                    String word = reader.readLine();
                    trie.insert(word);
                }
                reader.close();
                return trie;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

}
