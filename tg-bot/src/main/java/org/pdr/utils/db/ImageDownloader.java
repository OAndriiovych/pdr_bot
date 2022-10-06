package org.pdr.utils.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class ImageDownloader {

    public LinkedList<String> getUrl() throws SQLException {
        LinkedList<String> urls = new LinkedList<>();
        Connection connection = DBManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT url FROM questions WHERE url <> 'null image'");
        while (resultSet.next()) {
            urls.add(resultSet.getString(1));
        }
        resultSet.close();
        statement.close();
        connection.close();
        return urls;
    }

    public static void main(String[] args) throws SQLException {
        int counter = 0;
        for (String s : new ImageDownloader().getUrl()) {
            downloadFiles(s,
                    "C:\\Users\\Володимир\\Desktop\\SavedPhoto\\" + counter++);
        }
    }

    public static void downloadFiles(String strURL, String strPath) {

        try {
            URL connection = new URL(strURL);
            HttpURLConnection urlconn;
            urlconn = (HttpURLConnection) connection.openConnection();
            urlconn.setRequestMethod("GET");
            urlconn.connect();
            InputStream in ;
            in = urlconn.getInputStream();
            String myRandomName = "test.jpg";
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
        } catch (Exception e) {
            System.out.println(strPath);
            e.printStackTrace();
        }
    }


}
