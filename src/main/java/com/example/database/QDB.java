package com.example.database;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.sql.*;

/**
 * is created by aMIN on 1/29/2018 at 08:43 PM
 */
public class QDB {
    private static volatile QDB instance;
    public volatile Connection connection;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private DataSource dataSource;

    private QDB() {

        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get Singleton instance
     *
     * @return instance of the class
     */
    public static QDB getInstance() {
        if (instance == null) {
            instance = new QDB();
        }
        return instance;
    }


    public boolean insertTOmusicTable(String name, String src, String artist, String album, String name_persian, String artist_persian) {
        try {
            Statement statement = connection.createStatement();
            String query = "insert into music (name, src_url,tags, artist, album,name_persian,artist_persian) values (?,?,?,?,?,?,?);";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, name);
            preparedStmt.setString(2, src);
            preparedStmt.setString(3, String.format("%s %s %s", name, artist, album));
            preparedStmt.setString(4, artist);
            preparedStmt.setString(5, album);
            preparedStmt.setString(6, name_persian);
            preparedStmt.setString(7, artist_persian);
            boolean execute = preparedStmt.execute();
            return execute;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("not execute query");
    }


    public boolean insertTOBia2musicTable(String name, String src, String artist, String album, String name_persian, String artist_persian) {
        try {
            Statement statement = connection.createStatement();
            String query = "insert into bia2music (name, src_url,tags, artist, album,name_persian,artist_persian) values (?,?,?,?,?,?,?);";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, name);
            preparedStmt.setString(2, src);
            preparedStmt.setString(3, String.format("%s %s %s", name, artist, album));
            preparedStmt.setString(4, artist);
            preparedStmt.setString(5, album);
            preparedStmt.setString(6, name_persian);
            preparedStmt.setString(7, artist_persian);
            boolean execute = preparedStmt.execute();
            return execute;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("not execute query");
    }

    public boolean insertTOmusic2Table(String name, String src, String artist, String album, String name_persian, String artist_persian) {
        try {
            Statement statement = connection.createStatement();
            String query = "insert into music2 (name, src_url,tags, artist, album,name_persian,artist_persian,tags_persian) values (?,?,?,?,?,?,?,?);";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, name);
            preparedStmt.setString(2, src);
            preparedStmt.setString(3, String.format("%s %s %s", name, artist, album));
            preparedStmt.setString(4, artist);
            preparedStmt.setString(5, album);
            preparedStmt.setString(6, name_persian);
            preparedStmt.setString(7, artist_persian);
            preparedStmt.setString(8, String.format("%s %s", name_persian, artist_persian));
            boolean execute = preparedStmt.execute();
            return execute;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("not execute query");
    }

    public boolean insertTOmusic3Table(String channelUrl, String FileID, String name, String src, String artist, String album, String name_persian, String artist_persian) {
        try {
            Statement statement = connection.createStatement();
            String query = "insert into music3 (name, src_url, tags, artist, album, name_persian, artist_persian, tags_persian, channelUrl, FileID) values (?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, name);
            preparedStmt.setString(2, src);
            preparedStmt.setString(3, String.format("%s %s %s", name, artist, album));
            preparedStmt.setString(4, artist);
            preparedStmt.setString(5, album);
            preparedStmt.setString(6, name_persian);
            preparedStmt.setString(7, artist_persian);
            preparedStmt.setString(8, String.format("%s %s", name_persian, artist_persian));
            preparedStmt.setString(9, channelUrl);
            preparedStmt.setString(10, FileID);
            boolean execute = preparedStmt.execute();
            return execute;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("not execute query");
    }

    public boolean insertTOhrefsTable(String href) {
        try {
            Statement statement = connection.createStatement();
            String query = "insert into hrefs (href) values (?);";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, href);
            boolean execute = preparedStmt.execute();
            return execute;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("not execute query");
    }


    public ResultSet selectAllFromMusic(String where) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            String query = "select distinct * from music WHERE " + where + ";";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public ResultSet selectAllFromMusic2(String where) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            String query = "select * from music2 WHERE " + where + ";";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    public ResultSet selectAllFromhrefs(String where) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT href from hrefs WHERE " + where + ";";
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }


    public static void main(String[] args) {
        QDB.getInstance().insertTOmusicTable("Del Nakan", "http://dl.nex1music.ir/1397/08/04/Behnam%20Bani%20-%20Del%20Nakan.mp3?time=1545322590&filename=/1397/08/04/Behnam%20Bani%20-%20Del%20Nakan.mp3"
                , "Behnam Bani", "nun", "", "");
    }

}
