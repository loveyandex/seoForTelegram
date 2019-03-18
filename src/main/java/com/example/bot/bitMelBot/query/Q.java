package com.example.bot.bitMelBot.query;

import com.example.bot.bitMelBot.pojos.Usegh;
import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.*;

public class Q {


    public static boolean isAddedBeforeUser(Connection connection, User from) throws SQLException {
        Integer id = from.getId();
        return D(connection, id);
    }


    public static boolean isAddedBeforeUser(Connection connection, Integer id) throws SQLException {
        return D(connection, id);
    }

    private static boolean D(Connection connection, Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select id from usegh where id=?");
        preparedStatement.setInt(1, id);
        ResultSet execute = preparedStatement.executeQuery();
        while (execute.next()) {
            return true;
        }
        return false;
    }

    public static void addUser(Connection connection, User from) throws SQLException {
       if (isAddedBeforeUser(connection,from))
       {

       }

        PreparedStatement resultSet = connection.prepareStatement(
                "insert into usegh (id, firstName, isBot, lastName, userName, languageCode, numberMsg) values (?,?,?,?,?,?,?)");
        resultSet.setInt(1, from.getId());
        resultSet.setString(2, from.getFirstName());
        resultSet.setBoolean(3, from.getBot());
        resultSet.setString(4, from.getLastName());
        resultSet.setString(5, from.getLanguageCode());
//        resultSet.setString();
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicsbot?useUnicode=true&characterEncoding=utf-8"
                , "root", "");

        System.out.println(isAddedBeforeUser(connection, 121212));

    }
}
