package com.example;


import com.example.bot.bitMelBot.pojos.Music3;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

import static com.example.ResApi.run;

@RestController
public class ConfigContrller {


    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private DataSource dataSource;


    @PostMapping("/addmusic")
    String index(@RequestBody Music3 music3) throws IOException {

        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("create table IF NOT EXISTS music4" +
                    "(" +
                    "name           varchar(100)    null," +
                    "src_url        varchar(500)    null," +
                    "tags           varchar(200)    null," +
                    "artist         varchar(100)    null," +
                    "album          varchar(100)    null," +
                    "name_persian   varchar(100)    null," +
                    "artist_persian varchar(100)    null," +
                    "tags_persian   varchar(200)    null," +
                    "channelUrl     varchar(100)    null," +
                    "fileId         varchar(256)    null," +
                    "howmuchsent    int default '0' not null" +
                    ")");

            String sql = "select fileId from music4 where fileId='" + music3.getFileId() + "'";
            ResultSet execute = connection.createStatement().executeQuery(sql);
            if (execute.next()) {
                return "bef added:" + music3.getFileId();
            }


            PreparedStatement rs = connection.prepareStatement("insert into music4 " +
                    "(name, src_url, tags, artist, album, name_persian, artist_persian, tags_persian, channelUrl, fileId,howmuchsent)" +
                    " values (?,?,?,?,?,?,?,?,?,?,?)"
            );

            rs.setString(1, music3.getName());
            rs.setString(2, music3.getSrc_url());
            rs.setString(3, music3.getTags());
            rs.setString(4, music3.getArtist());
            rs.setString(5, music3.getAlbum());
            rs.setString(6, music3.getName_persian());
            rs.setString(7, music3.getArtist_persian());
            rs.setString(8, music3.getTags_persian());
            rs.setString(9, music3.getChannelUrl());
            rs.setString(10, music3.getFileId());
            rs.setInt(11, music3.getHowmuchsent());
            rs.execute();
            String token = "700687388:AAHagXfLRpcRV0U8bnoH91Ig3mB2boMXqh4";
            String url = "https://api.telegram.org/bot"
                    + token
                    + "/sendMessage?chat_id=145464749&text="
                    + new Gson().toJson(music3);

            return run(url);


        } catch (Exception e) {
            String token = "700687388:AAHagXfLRpcRV0U8bnoH91Ig3mB2boMXqh4";
            String url = "https://api.telegram.org/bot"
                    + token
                    + "/sendMessage?chat_id=145464749&text="
                    + new Gson().toJson(e.getLocalizedMessage());
            return run(url);
        }
    }


    @GetMapping("/test")
    public String S() {
        boolean resultSet;
        try {
            resultSet = dataSource.getConnection().createStatement().execute("INSERT INTO ticks VALUES (now())");
        } catch (SQLException e) {
            return e.toString();
        }

        return String.valueOf(resultSet);
    }
    @GetMapping("/test2")
    public String S2() {
        boolean resultSet;
        try {
            resultSet = dataSource.getConnection().createStatement()
                    .execute("INSERT INTO seek2  (amount) VALUES (323232323)");


        } catch (SQLException e) {
            return e.toString();
        }

        return String.valueOf(resultSet);
    }

}
