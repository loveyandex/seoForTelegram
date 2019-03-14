package com.example;


import com.example.pojos.Music3;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.ResApi.run;

@RestController
public class ConfigContrller {


    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private DataSource dataSource;


    @PostMapping("/addmusic")
    String index(@RequestBody Music3 music3) {

        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("        create table IF NOT EXISTS   music3" +
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
                    "FileID         varchar(256)    null," +
                    "howmuchsent    int default '0' not null" +
                    ")");

            String token = "700687388:AAHagXfLRpcRV0U8bnoH91Ig3mB2boMXqh4";

            String url = "https://api.telegram.org/bot"
                    + token
                    + "/sendMessage?chat_id=145464749&text="
                    + new Gson().toJson(music3);

            return run(url);


        } catch (SQLException e) {
            return "[SQLException]";
        } catch (IOException e) {
            return "[IOException]";
        }
    }


    @GetMapping("/test")
    public void S(){


    }




}
