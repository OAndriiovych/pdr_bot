package org.pdr.utils.db;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.LinkedList;

public class ImageDownloader {

    public static final String PASS = "1488";
    public static final String USER_NAME = "postgres";
    public static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/PDRQuestions";


    public LinkedList<String> getUrl() throws SQLException {
        LinkedList<String> urls = new LinkedList<>();

        Connection connection;
        Statement statement;
        ResultSet resultSet;


        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        connection = DriverManager.getConnection(DB_URL, USER_NAME, PASS);
        statement = connection.createStatement();


        resultSet = statement.executeQuery("SELECT url FROM questions WHERE url <> 'null image'");


        while (resultSet.next()) {
            urls.add(resultSet.getString(1));
        }

        resultSet.close();
        statement.close();
        connection.close();

        return urls;
    }

    public static void main(String[] args) throws SQLException {
        ImageDownloader imageDownloader = new ImageDownloader();
        int counter = 0;
        for(String s: imageDownloader.getUrl()){
            downloadFiles(s,
                    "C:\\Users\\Володимир\\Desktop\\SavedPhoto\\" + counter++, 24);
        }

    }


    public static void downloadFiles(String strURL, String strPath, int buffSize) {

        try {
            URL connection = new URL(strURL);
            HttpURLConnection urlconn;
            urlconn = (HttpURLConnection) connection.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.connect();
            InputStream in = null;
            in = urlconn.getInputStream();
            String myRandomName = "test.jpg";
            File file = new File(strPath + myRandomName);

            if(!file.exists()) {
                file.createNewFile();
            }

            OutputStream writer = new FileOutputStream(file);
            byte buffer[] = new byte[buffSize];
            int c = in.read(buffer);
            while (c > 0) {
                writer.write(buffer, 0, c);
                c = in.read(buffer);
            }
            writer.flush();
            writer.close();
            in.close();
        } catch (Exception e) {
            System.out.println(strPath);
            System.out.println(e);
        }
    }


}
