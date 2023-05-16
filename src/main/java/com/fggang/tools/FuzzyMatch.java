package com.fggang.tools;


import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fggang.datasave.items.Item;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;

public class FuzzyMatch {
    // 计算一个字符串的n-gram集合
    public static Set<String> getNGrams(String str, int n) {
        Set<String> nGrams = new HashSet<>();
        for (int i = 0; i <= str.length() - n; i++) {
            nGrams.add(str.substring(i, i + n));
        }
        return nGrams;
    }

    // 计算两个字符串之间的N-Gram相似度
    public static double nGramSimilarity(String str1, String str2, int n) {
        Set<String> nGrams1 = getNGrams(str1, n);
        Set<String> nGrams2 = getNGrams(str2, n);
        Set<String> intersection = new HashSet<>(nGrams1);
        intersection.retainAll(nGrams2);
        int commonNGrams = intersection.size();
        int totalNGrams = nGrams1.size() + nGrams2.size() - commonNGrams;
        return (double) commonNGrams / totalNGrams;
    }

    // 查找与输入字符串最匹配的条目
    public static Item getMatchItem(String input, ArrayList<Item> list, int n) {
        double maxScore = 0.0;
        Item bestMatch = null;

        for (Item item : list) {
            double score = nGramSimilarity(input, item.item, n);
            if (score > maxScore) {
                maxScore = score;
                bestMatch = item;
            }
        }
        return bestMatch;


    }
}

