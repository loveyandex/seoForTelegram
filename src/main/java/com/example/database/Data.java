package com.example.database;

import org.springframework.stereotype.Component;

/**
 * created By aMIN on 3/31/2019 11:33 PM
 */
@Component
public class Data {

    public static final String create_table_seen = "create table  if not exists seen(id bigint(12) not null primary key auto_increment ,msg_id bigint(12) not null ,cv bigint(12) not null default 0)";
}
