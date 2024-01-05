package cn.arthmetic.string;

/**
 * @author 26016
 * @description
 * @date 2023-12-20 17:03
 **/
public class KMP {


    public static void main(String[] args) {
        System.err.println(kmp("ababcabcba", "abc"));
    }

    /**
     * kmp 求子串数量
     *
     * @param str 字符串
     * @param ptr 子串
     */
    public static int kmp(String str, String ptr) {
        if (str == null || ptr == null || str.length() < ptr.length()) {
            return 0;
        }
        int mInx = 0;
        int cInx = 0;
        char[] mStr = str.toCharArray();
        char[] pStr = ptr.toCharArray();
        int[] next = buildNextArray(ptr);
        int sum = 0;
        while (mInx < mStr.length) {
            if (mStr[mInx] == pStr[cInx]) {
                mInx++;
                cInx++;
            } else {
                if (cInx > 0) {
                    cInx = next[cInx - 1];
                } else {
                    mInx++;
                }
            }
            if (cInx == pStr.length) {
                sum++;
                cInx = next[cInx - 1];
            }
        }
        return sum;
    }

    /**
     * 构建next数组
     *
     * @param str 字符串
     */
    private static int[] buildNextArray(String str) {
        char[] chars = str.toCharArray();
        int[] next = new int[chars.length];
        int p = 0;
        for (int i = 1; i < chars.length; i++) {
            while (p > 0 && chars[p] != chars[i]) {
                p = next[p - 1];
            }
            if (chars[p] == chars[i]) {
                p++;
            }
            next[i] = p;
        }
        return next;
    }
}
