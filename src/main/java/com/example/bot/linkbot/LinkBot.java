package com.example.bot.linkbot;


import com.example.Meths;
import com.example.bot.bitMelBot.pojos.Status;
import com.example.bot.linkbot.model.Gune;
import com.example.bot.linkbot.model.StatusOfAdding;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * created By aMIN on 4/4/2019 12:27 PM
 */
@Component
public class LinkBot extends TelegramLongPollingBot {


    public LinkBot() {
        Meths.sendToBot("constructor is working .................. ");

    }

    @Autowired
    private Connection connection;

    @Bean
    public void setDbs() {
        try {
            connection.createStatement().execute("create table if not exists Muser(id serial primary key)");
            connection.createStatement().execute(
                    "create table if not exists Link(id serial primary key ,user_id bigint not null, " +
                            "name varchar(100) null ,dscrpt varchar(1000) null ,photo_id varchar(200) null ,link_src varchar(500) null ,status varchar(25) null )");

            connection.createStatement().execute("delete  from Muser  where id=878712");
//            connection.createStatement().execute("delete  from Link");
            connection.createStatement().execute("insert into Muser (id) values (878712);");

            ResultSet resultSet = connection.createStatement().executeQuery("select * from Muser;");
            ResultSet resultSet2 = connection.createStatement().executeQuery("select * from Link;");


            JSONArray objects = convertToJSON(resultSet2);
            Meths.sendToBot(new Gson().toJson(objects));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static JSONArray convertToJSON(ResultSet resultSet)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_columns; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }

    private boolean addUser(int idj) {
        String sql = "INSERT INTO Muser (id)" +
                "    SELECT ?" +
                "WHERE NOT EXISTS (" +
                "    SELECT id FROM Muser WHERE id=? " +
                ");";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idj);
            preparedStatement.setInt(2, idj);
            boolean execute = preparedStatement.execute();
//            Meths.sendToBot(String.valueOf(idj));
            return execute;

        } catch (SQLException e) {
            Meths.sendToBot(e.toString());
        }
        return (true);

    }


    @Override
    public void onClosing() {
        super.onClosing();
    }

    public void onReplyKey(@MyAnnotation("df") Update update) {

        Response response = new Response(update);
        System.out.println(LocalTime.now().toString());
        Gune[] values = Gune.values();
        List<String> list = new ArrayList<>();
        List<String> statues = new ArrayList<>();

        int k = 3;
        for (int j = 0; j <= values.length / k; j++) {
            for (int i = 0; i < k && (k * (j) + i) < values.length; i++) {
                String name = values[k * (j) + i].name;
                list.add(name);
            }
        }

        list.add("اضافه کردن لینک");
        String text1 = update.getMessage().getText();


        if (list.contains(text1)) {
            try {
                int indexOf = list.indexOf(text1);
                String name = "gune" + indexOf;
                Meths.sendToBot(name + " is running...");

                System.out.println(name);
                response.getClass().getMethod(name).invoke(response, null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        StatusOfAdding[] statusOfAddings = StatusOfAdding.values();
        for (StatusOfAdding statusOfAdding : statusOfAddings) {
            statues.add(statusOfAdding.name());
        }


        try {


            Integer id = update.getMessage().getFrom().getId();
            ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where user_id='" + id + "'");

            boolean userNeedsNewLinkGenerate = true;

            while (resultSet2.next()) {
                String status = resultSet2.getString(7);

                if (!status.equals(StatusOfAdding.ADDED.name())) {
                    userNeedsNewLinkGenerate = false;
                    if (statues.contains(status)) {
                        int indexOf = statues.indexOf(status) + 12;
                        String mn = "gune" + indexOf;
                        response.getClass().getMethod(mn).invoke(response, null);

                    }


                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            response.startMsg();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        addUser(update.getMessage().getFrom().getId());
        onReplyKey(update);

    }

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return "810311785:AAFrHm_StwMR6A52NeUpxNRxsIra5De1cKo";
    }


    public class Response {
        Update update;

        public Response(Update update) {
            this.update = update;
        }

        public Response startMsg() throws TelegramApiException {
            if (update.hasMessage() && update.getMessage().hasText()) {
                if (update.getMessage().getText().equals("/start")) {
                    SendMessage method = new SendMessage(update.getMessage().getChatId()
                            , "می تونی موضوعی رو که میخوای تایپ کنی برامون یا از تنوع ما استفاده کنین");
                    method.setReplyMarkup(start());
                    execute(method);
                }
            }


            return this;
        }


        public Response gune0() throws TelegramApiException {
            execute(new SendMessage(update.getMessage().getChatId()
                    , update.getMessage().getText()));


            return this;
        }

        public Response gune1() throws TelegramApiException {
            execute(new SendMessage(update.getMessage().getChatId()
                    , update.getMessage().getText()));


            return this;
        }

        public Response gune11() throws TelegramApiException {
            User from = update.getMessage().getFrom();
            Integer id = from.getId();
            try {

                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where user_id='" + id + "'");
                boolean never = true;

                while (resultSet2.next()) {


                    int anInt = resultSet2.getInt(1);
                    int user_id = resultSet2.getInt(2);
                    String name = resultSet2.getString(3);
                    String dscrpt = resultSet2.getString(4);
                    String photo_id = resultSet2.getString(5);
                    String link_src = resultSet2.getString(6);
                    String status = resultSet2.getString(7);

                    if (name == null
                            || dscrpt == null
                            || photo_id == null
                            || link_src == null) {
                        never = false;
                        Meths.sendToBot(String.valueOf(anInt) + ":" +
                                user_id +
                                name +
                                dscrpt +
                                photo_id +
                                link_src
                                + status);
                        if (name == null) {
                            execute(new SendMessage(update.getMessage().getChatId(), "خب حالا اسم گروه یا کانالی که میخوای اد کنیو وارد کن "));
                            resultSet2.updateString(7, StatusOfAdding.ADDINGNAME.name());
                            resultSet2.updateRow();
                        }


                    }
                }
                if (never) {
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "insert into link (user_id)" + " values (?)");
                    preparedStatement.setInt(1, id);
                    preparedStatement.execute();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


            return this;
        }


        public Response gune12() throws TelegramApiException {
            User from = update.getMessage().getFrom();
            Integer id = from.getId();
            try {

                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where user_id='" + id + "'");

                while (resultSet2.next()) {
                    int anInt = resultSet2.getInt(1);
                    int user_id = resultSet2.getInt(2);
                    String name = resultSet2.getString(3);
                    String dscrpt = resultSet2.getString(4);
                    String photo_id = resultSet2.getString(5);
                    String link_src = resultSet2.getString(6);
                    String status = resultSet2.getString(7);

                    if (name == null
                            || dscrpt == null
                            || photo_id == null
                            || link_src == null) {
                        Meths.sendToBot(String.valueOf(anInt) + ":" +
                                user_id +
                                name +
                                dscrpt +
                                photo_id +
                                link_src
                                + status);
                        if (StatusOfAdding.ADDINGNAME.name().equals(status)) {
                            resultSet2.updateString(7, StatusOfAdding.ADDINGDSCRP.name());
                            resultSet2.updateString(3, update.getMessage().getText());
                            resultSet2.updateRow();
                            execute(new SendMessage(update.getMessage().getChatId(), "حالا توضیحات کانال یا گروهتو بنویس"));
                        }


                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


            return this;
        }

        public Response gune13() throws TelegramApiException {
            User from = update.getMessage().getFrom();
            Integer id = from.getId();
            try {

                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where user_id='" + id + "'");

                while (resultSet2.next()) {

                    int anInt = resultSet2.getInt(1);
                    int user_id = resultSet2.getInt(2);
                    String name = resultSet2.getString(3);
                    String dscrpt = resultSet2.getString(4);
                    String photo_id = resultSet2.getString(5);
                    String link_src = resultSet2.getString(6);
                    String status = resultSet2.getString(7);

                    if (name == null
                            || dscrpt == null
                            || photo_id == null
                            || link_src == null) {
                        Meths.sendToBot(anInt + ":" +
                                user_id +
                                name +
                                dscrpt +
                                photo_id +
                                link_src
                                + status);
                        if (StatusOfAdding.ADDINGDSCRP.name().equals(status)) {
                            resultSet2.updateString(7, StatusOfAdding.ADDINGPHOTHO.name());
                            resultSet2.updateString(4, update.getMessage().getText());
                            resultSet2.updateRow();
                            execute(new SendMessage(update.getMessage().getChatId(), "حالا عکس کاور گروهت یا کانالتو بفرس"));
                        }


                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


            return this;
        }

        public Response gune14() throws TelegramApiException {
            User from = update.getMessage().getFrom();
            Integer id = from.getId();
            try {

                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where user_id='" + id + "'");

                while (resultSet2.next()) {

                    int anInt = resultSet2.getInt(1);
                    int user_id = resultSet2.getInt(2);
                    String name = resultSet2.getString(3);
                    String dscrpt = resultSet2.getString(4);
                    String photo_id = resultSet2.getString(5);
                    String link_src = resultSet2.getString(6);
                    String status = resultSet2.getString(7);

                    if (name == null
                            || dscrpt == null
                            || photo_id == null
                            || link_src == null) {
                        Meths.sendToBot(anInt + " : " +
                                user_id + " : " +
                                name + " : " +
                                dscrpt + " : " +
                                photo_id + " : " +
                                link_src + " : "
                                + status);
                        if (StatusOfAdding.ADDINGPHOTHO.name().equals(status)) {
                            boolean b = update.getMessage().hasPhoto();
                            if (b) {
                                List<PhotoSize> photos = update.getMessage().getPhoto();
                                Meths.sendToBot(new Gson().toJson(photos));
                                Meths.sendToBot(("in boolean"));
                                resultSet2.updateString(5, photos.get(0).getFileId());
                                resultSet2.updateString(7, StatusOfAdding.ADDINGLINK.name());
                                resultSet2.updateRow();
                                execute(new SendMessage(update.getMessage().getChatId(), "اخریشه.. لینک معتبر گروهت یا کانالتو بفرس"));

                            }

                        }


                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


            return this;
        }

        public Response gune15() throws TelegramApiException {
            User from = update.getMessage().getFrom();
            Integer id = from.getId();
            try {

                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where user_id='" + id + "'");

                while (resultSet2.next()) {

                    int anInt = resultSet2.getInt(1);
                    int user_id = resultSet2.getInt(2);
                    String name = resultSet2.getString(3);
                    String dscrpt = resultSet2.getString(4);
                    String photo_id = resultSet2.getString(5);
                    String link_src = resultSet2.getString(6);
                    String status = resultSet2.getString(7);

                    if (name == null
                            || dscrpt == null
                            || photo_id == null
                            || link_src == null) {
                        Meths.sendToBot(anInt + ":" +
                                user_id +
                                name +
                                dscrpt +
                                photo_id +
                                link_src
                                + status);
                        if (StatusOfAdding.ADDINGLINK.name().equals(status)) {
                            resultSet2.updateString(6, update.getMessage().getText());
                            resultSet2.updateString(7, StatusOfAdding.ADDED.name());
                            resultSet2.updateRow();

                            execute(new SendMessage(update.getMessage().getChatId(), "تمومه"));
                        }


                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


            return this;
        }


    }


    public static InlineKeyboardMarkup tryAgain() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        Gune[] values = Gune.values();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();

        int i1 = 3;
        for (int j = 0; j <= values.length / i1; j++) {
            List<InlineKeyboardButton> list = new ArrayList<>();
            for (int i = 0; i < i1 && (i1 * (j) + i) < values.length; i++) {
                String name = values[i1 * (j) + i].name;
                InlineKeyboardButton button = new InlineKeyboardButton(name)
                        .setCallbackData(name);
                list.add(button);
            }
            lists.add(list);
        }
        markup.setKeyboard(lists);
        return markup;
    }


    ReplyKeyboardMarkup start() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        Gune[] values = Gune.values();


        int k = 4;
        for (int j = 0; j <= values.length / k; j++) {
            KeyboardRow keyboardRow = new KeyboardRow();

            for (int i = 0; i < k && (k * (j) + i) < values.length; i++) {
                String name = values[k * (j) + i].name;
                KeyboardButton button = new KeyboardButton(name);
                keyboardRow.add(button);
            }


            keyboardRows.add(keyboardRow);


        }
        KeyboardButton button = new KeyboardButton("اضافه کردن لینک");
        keyboardRows.get(keyboardRows.size() - 1).add(button);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }


    ReplyKeyboardMarkup addingLink() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        Gune[] values = Gune.values();


        int k = 4;
        for (int j = 0; j <= values.length / k; j++) {
            KeyboardRow keyboardRow = new KeyboardRow();

            for (int i = 0; i < k && (k * (j) + i) < values.length; i++) {
                String name = values[k * (j) + i].name;
                KeyboardButton button = new KeyboardButton(name);
                keyboardRow.add(button);
            }


            keyboardRows.add(keyboardRow);


        }
        KeyboardButton button = new KeyboardButton("اضافه کردن لینک");
        keyboardRows.get(keyboardRows.size() - 1).add(button);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }


}
