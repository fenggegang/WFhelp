package com.fggang.tools;

import java.io.*;
import java.nio.file.Files;

public class IO {
    public static String getText(String fileName) {
        BufferedReader br = null;
        String s = "";
        try {
            br = new BufferedReader(new InputStreamReader
                    (Files.newInputStream(new File
                            (fileName).toPath()), "GBK"));//UTF-8
            String str = null;
            int i = 0;
            while ((str = br.readLine()) != null) {
                String[] v1 = str.trim().split("\\s+"); //剔除调前、后、中间所有的空格
                s += str + "\n";
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return s;

    }

    public static boolean setText(String fileName, String s) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        String[] writerString = s.split("\n");
        for (int i = 0; i < writerString.length; i++) {
            bw.write(writerString[i]);
            bw.newLine();
        }

        bw.close();

        return true;
    }
}
