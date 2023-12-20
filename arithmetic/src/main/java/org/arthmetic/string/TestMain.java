package org.arthmetic.string;

/**
 * @author 26016
 * @description
 * @date 2023-12-20 11:39
 **/
public class TestMain {

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

}
