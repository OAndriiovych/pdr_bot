package org.pdr.utils.db;

import org.pdr.entity.Question;
import org.pdr.repository.QuestionCache;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class Uplode {
    public static void main(String[] args) {
        d();
    }

    private static void upTh() {
        try (Connection connection = DBManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO theme (caption) values (?)");
            FileInputStream fis = new FileInputStream("tg-bot/src/main/resources/themes.txt");
            Scanner sc = new Scanner(fis);
            while (sc.hasNextLine()) {
                preparedStatement.setString(1, sc.nextLine());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void upQ() {
        try (Connection connection = DBManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO questions (theme_id,caption,url,countofbutton,correctbutton) values (?,?,?,?,?)");
//            Map<Integer, List<Question>> questionsByTheme = QuestionCache.QUESTIONS_BY_THEME;
            Set<Map.Entry<Integer, List<Question>>> entries = null;
//            Set<Map.Entry<Integer, List<Question>>> entries = questionsByTheme.entrySet();
            for (Map.Entry<Integer, List<Question>> entry : entries) {
                Integer th = entry.getKey();
                for (Question v : entry.getValue()) {
                    preparedStatement.setInt(1, th);
                    preparedStatement.setString(2, v.caption);
//                    preparedStatement.setString(3, v.url);
//                    preparedStatement.setInt(4, v.countOfButton);
//                    preparedStatement.setInt(5, v.correctButton);
                    preparedStatement.addBatch();
                }
            }
            int[] ints = preparedStatement.executeBatch();
            Arrays.stream(ints).forEach(System.out::println);
            System.out.println(ints);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void d() {
        try (Connection connection = DBManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from questions where url <> 'null image'");
            PreparedStatement update = connection.prepareStatement("update questions set url=? where question_id =?");
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = 0;
            while (resultSet.next()) {
                String oldUrl = resultSet.getString("url");
                try {
                    id++;
                    String strPath = id + "";
                    downloadFiles(oldUrl, strPath);
                    update.setString(1, strPath);
                    update.setInt(2, resultSet.getInt("question_id"));
                } catch (Exception e) {
                    System.out.println(oldUrl);
                    e.printStackTrace();
                }
                update.addBatch();
            }
            update.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void downloadFiles(String strURL, String strPath) throws Exception{

        URL connection = new URL(strURL);
        HttpURLConnection urlconn;
        urlconn = (HttpURLConnection) connection.openConnection();
        urlconn.setRequestMethod("GET");
        urlconn.connect();
        InputStream in ;
        in = urlconn.getInputStream();
        String myRandomName = ".jpg";
        File file = new File(strPath + myRandomName);

        if (!file.exists()) {
            file.createNewFile();   // Може цьої строчки і не треба, хз
        }

        OutputStream writer = new FileOutputStream(file);
        byte[] buffer = new byte[24];
        int c = in.read(buffer);
        while (c > 0) {
            writer.write(buffer, 0, c);
            c = in.read(buffer);
        }
        writer.flush();
        writer.close();
        in.close();

    }
}
