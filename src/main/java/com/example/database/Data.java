package com.example.database;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * created By aMIN on 3/31/2019 11:33 PM
 */
public class Data {

    public static final String create_table_seen = "create table  if not exists seek(id bigint(12) not null primary key auto_increment ,msg_id bigint(12) not null ,cv bigint(12) not null default 0)";


    @Autowired
    Connection connection;

    public String creatdedb() {
        try {
            Statement statement = connection.createStatement();
            int i = statement.executeUpdate("CREATE TABLE IF NOT EXISTS  seek3" +
                    "(" +
                    "id SERIAL PRIMARY KEY,   " +
                    "amount  integer  NOT NULL" +
                    ")");

            return String.valueOf(i);
        } catch (SQLException e) {
            return new Gson().toJson(e.toString());
        }
    }
}
