package com.example.bot.linkbot;


import com.example.Meths;
import com.example.bot.linkbot.model.Gune;
import com.example.bot.linkbot.model.Routes;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
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


    @Bean
    public void setDbs() {
        try {
            connection.createStatement().execute("create table if not exists Muser(id serial primary key)");
            connection.createStatement().execute(
                    "create table if not exists Link(" +
                            "id serial primary key" +
                            " ,user_id bigint not null, " +
                            "name varchar(100) null ," +
                            "dscrpt varchar(1000) null ," +
                            "photo_id varchar(200) null ," +
                            "link_src varchar(500) null ," +
                            "gune varchar(50) null ," +
                            "status varchar(25) null )");
            ResultSet resultSet2 = connection.createStatement().executeQuery("select * from Link;");
            JSONArray objects = convertToJSON(resultSet2);
            Meths.sendToBot(new Gson().toJson(objects));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Autowired
    private Connection connection;

    @Autowired
    private List<String> routes;

    @Autowired
    private int routeNumber;

    public void onReplyKey(@MyAnnotation("df") Update update) {

        Response response = new Response(update);
        System.out.println(LocalTime.now().toString());
        List<String> statues = new ArrayList<>();

        String text1 = update.getMessage().getText();


        if (routes.contains(text1)) {
            try {
                int indexOf = routes.indexOf(text1);
                String name = "gune" + indexOf;
                sendMsg(name + " first running...");
                response.getClass().getMethod(name).invoke(response, null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {

            sendMsg("after container");

            StatusOfAdding[] statusOfAddings = StatusOfAdding.values();
            for (StatusOfAdding statusOfAdding : statusOfAddings) {
                statues.add(statusOfAdding.name());
            }


            try {


                Integer id = update.getMessage().getFrom().getId();
                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where user_id='" + id + "'");

                while (resultSet2.next()) {
                    String status = resultSet2.getString(8);
                    if (!status.equals(StatusOfAdding.ADDED.name())) {
                        if (statues.contains(status)) {
                            int indexOf = statues.indexOf(status) + routeNumber;

                            String mn = "gune" + indexOf;
                            sendMsg(mn + " is running...");

                            response.getClass().getMethod(mn).invoke(response, null);
                            return;
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
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().isCommand()) {
            if (update.getMessage().getText().equals("/users")) {
                try {
                    ResultSet resultSet2 = connection.createStatement().executeQuery("select * from Muser;");
                    while (resultSet2.next()) {
                        execute(new SendMessage(update.getMessage().getChatId(), String.valueOf(resultSet2.getInt(1))));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }
        }

        if (update.getMessage().hasText()) {
            if (update.getMessage().getText().equals("بکن")) {
                setDbs();
            }
            if (update.getMessage().getText().equals("drop")) {
                try {
                    connection.createStatement().execute("drop table if exists Link");
                } catch (SQLException e) {
                    sendMsg(e.toString());

                }
            }
            if (update.getMessage().getText().equals("دل")) {
                try {
                    connection.createStatement().execute("delete  from Link");
                } catch (SQLException e) {
                    Meths.sendToBot(e.toString());

                }

            }
        }

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

        public Response gune() throws TelegramApiException {
            execute(new SendMessage(update.getMessage().getChatId()
                    , update.getMessage().getText()));


            return this;
        }

        public Response gune0() throws TelegramApiException {
            try {
                ResultSet re = connection.createStatement().executeQuery("select * from Link where gune like '%" + update.getMessage().getText() + "%';");
                while (re.next()) {
                    execute(new SendMessage(update.getMessage().getChatId()
                            , re.getString("link_src")));

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


            return this;
        }

        public Response gune12() throws TelegramApiException {
            execute(new SendMessage(update.getMessage().getChatId()
                    , update.getMessage().getText()));


            return this;
        }

        public Response gune13() throws TelegramApiException {

            try {


                Integer id = update.getMessage().getFrom().getId();
                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where status!='ADDED' and user_id='" + id + "'");

                while (resultSet2.next()) {
                    sendMsg(resultSet2.getString(3));
                    PreparedStatement preparedStatement = connection.prepareStatement("delete  from Link where  user_id=? and id=?");
                    preparedStatement.setLong(1, resultSet2.getLong(2));
                    long aLong = resultSet2.getLong(1);
                    preparedStatement.setLong(2, aLong);

                    preparedStatement.execute();
                    sendMsg("deleted by id " + aLong);
                    execute(new SendMessage(update.getMessage().getChatId(),
                            "اوکی...")
                            .setReplyMarkup(start()));
                }


            } catch (SQLException e) {
                sendMsg(e.toString());
            }

            return this;
        }

        public Response gune14() throws TelegramApiException {
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
                    String status = resultSet2.getString(8);

                    if (name == null
                            || dscrpt == null
                            || photo_id == null
                            || link_src == null) {
                        never = false;
                        Meths.sendToBot("gune 14 " + ":" +
                                user_id +
                                name +
                                dscrpt +
                                photo_id +
                                link_src
                                + status);
                        if (name == null) {
                            execute(new SendMessage(update.getMessage().getChatId(),
                                    "خب حالا اسم گروه یا کانالی که میخوای اد کنیو وارد کن ")
                                    .setReplyMarkup(cancelAdding())
                            );
                            resultSet2.updateString(8, StatusOfAdding.ADDINGNAME.name());
                            resultSet2.updateRow();
                        }


                    }
                }
                if (never) {
                    sendMsg("in never");
                    PreparedStatement preparedStatement = connection.prepareStatement(
                            "insert into Link (user_id,status)" + " values (?,?)");
                    preparedStatement.setInt(1, id);
                    preparedStatement.setString(2, StatusOfAdding.ADDINGNAME.name());
                    preparedStatement.execute();

                    execute(new SendMessage(update.getMessage().getChatId(),
                            "خب حالا:((( اسم گروه یا کانالی که میخوای اد کنیو وارد کن "
                    ).setReplyMarkup(cancelAdding()));

                }

            } catch (SQLException e) {
                sendMsg(e.toString());
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
                    String status = resultSet2.getString(8);

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
                            resultSet2.updateString(3, update.getMessage().getText());
                            resultSet2.updateString(8, StatusOfAdding.ADDINGDSCRP.name());
                            resultSet2.updateRow();

                            execute(new SendMessage(update.getMessage().getChatId(), "حالا توضیحات کانال یا گروهتو بنویس"));
                        }


                    }
                }

            } catch (SQLException e) {
                sendMsg(e.toString());
            }


            return this;
        }

        public Response gune16() throws TelegramApiException {
            User from = update.getMessage().getFrom();
            Integer id = from.getId();
            try {

                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where user_id='" + id + "'");

                while (resultSet2.next()) {
                    String status = resultSet2.getString(8);

                    if (StatusOfAdding.ADDINGDSCRP.name().equals(status)) {
                        resultSet2.updateString(4, update.getMessage().getText());
                        resultSet2.updateString(8, StatusOfAdding.ADDINGPHOTHO.name());
                        resultSet2.updateRow();
                        execute(new SendMessage(update.getMessage().getChatId(), "حالا عکس کاور گروهت یا کانالتو بفرس"));
                    }

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


            return this;
        }

        public Response gune17() throws TelegramApiException {
            boolean b = update.getMessage().hasPhoto();
            if (!b) {

                execute(new SendMessage(update.getMessage().getChatId(),
                        "معتبر نیستش...لظفا عکس بفرتستید"));

                return this;

            }
            User from = update.getMessage().getFrom();
            Integer id = from.getId();
            try {

                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where user_id='" + id + "'");

                while (resultSet2.next()) {
                    String status = resultSet2.getString(8);

                    if (StatusOfAdding.ADDINGPHOTHO.name().equals(status)) {
                        List<PhotoSize> photos = update.getMessage().getPhoto();
                        resultSet2.updateString(5, photos.get(0).getFileId());
                        resultSet2.updateString(8, StatusOfAdding.ADDINGLINK.name());
                        resultSet2.updateRow();
                        execute(new SendMessage(update.getMessage().getChatId(), "خب لینک معتبر گروهت یا کانالتم بفرس"));

                    }
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }


            return this;
        }

        public Response gune18() throws TelegramApiException {
            User from = update.getMessage().getFrom();
            Integer id = from.getId();
            try {
                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where user_id='" + id + "'");

                while (resultSet2.next()) {
                    String status = resultSet2.getString(8);
                    if (StatusOfAdding.ADDINGLINK.name().equals(status)) {
                        String linkPath = update.getMessage().getText();
                        resultSet2.updateString(6, linkPath);
                        resultSet2.updateString(8, StatusOfAdding.ADDINGGUNE.name());
                        resultSet2.updateRow();
                        execute(new SendMessage(update.getMessage().getChatId()
                                , " اخریشه... حالا نوع گروه یا کانالتو از بین گونه های ارائه شده انتخاب کن")
                                .setReplyMarkup(setType())
                        );

                    }
                }
            } catch (SQLException e) {
                execute(new SendMessage(update.getMessage().getChatId(), e.toString()));
            }
            return this;
        }

        public Response gune19() throws TelegramApiException {
            User from = update.getMessage().getFrom();
            Integer id = from.getId();
            try {
                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where user_id='" + id + "'");

                while (resultSet2.next()) {
                    String name = resultSet2.getString(3);
                    String dscrpt = resultSet2.getString(4);
                    String photo_id = resultSet2.getString(5);
                    String link_src = resultSet2.getString(6);
                    String status = resultSet2.getString(8);

                    if (StatusOfAdding.ADDINGGUNE.name().equals(status)) {
                        String gune = update.getMessage().getText();
                        resultSet2.updateString(7, gune);
                        resultSet2.updateString(8, StatusOfAdding.ADDED.name());
                        resultSet2.updateRow();
                        execute(new SendMessage(update.getMessage().getChatId(), "تمومه").setReplyMarkup(start()));

                        SendPhoto output = new SendPhoto();
                        output.setChatId(String.valueOf(id));
                        output.setPhoto(photo_id);
                        output.setCaption(" ✔️ " + name + "\n" + "   ✔️ " + dscrpt + "\n" + "  ✔️ " + link_src);
                        execute(output);

                    }
                }
            } catch (SQLException e) {
                execute(new SendMessage(update.getMessage().getChatId(), e.toString()));
            }
            return this;
        }

    }

    private ReplyKeyboard cancelAdding() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardButton button = new KeyboardButton(Routes.CANCELMAKEINGLINK.name);

        KeyboardRow k1 = new KeyboardRow();
        k1.add(button);
        keyboardRows.add(k1);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }

    ReplyKeyboardMarkup setType() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        Gune[] values = Gune.values();


        int k = 1;
        for (int j = 0; j <= values.length / k; j++) {
            KeyboardRow keyboardRow = new KeyboardRow();

            for (int i = 0; i < k && (k * (j) + i) < values.length; i++) {
                String name = values[k * (j) + i].name + "/";
                KeyboardButton button = new KeyboardButton(name);
                keyboardRow.add(button);
            }
            keyboardRows.add(keyboardRow);

        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);

        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }


    ReplyKeyboardMarkup rr() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        Gune[] values = Gune.values();


        int k = 4;
        r(keyboardRows, values, k);
        KeyboardButton button = new KeyboardButton("اضافه کردن لینک");
        keyboardRows.get(keyboardRows.size() - 1).add(button);

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }


    ReplyKeyboardMarkup start() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardButton button = new KeyboardButton(Vars.GROUPSANDCHANNELS);
        KeyboardButton button2 = new KeyboardButton(Vars.ADDINGLINKTO);

        KeyboardRow k1 = new KeyboardRow();
        KeyboardRow k2 = new KeyboardRow();
        k1.add(button);
        k2.add(button2);
        keyboardRows.add(k1);
        keyboardRows.add(k2);
        replyKeyboardMarkup.setKeyboard(keyboardRows);

        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }


    private void r(ArrayList<KeyboardRow> keyboardRows, Gune[] values, int k) {
        for (int j = 0; j <= values.length / k; j++) {
            KeyboardRow keyboardRow = new KeyboardRow();

            for (int i = 0; i < k && (k * (j) + i) < values.length; i++) {
                String name = values[k * (j) + i].name;
                KeyboardButton button = new KeyboardButton(name);
                keyboardRow.add(button);
            }
            keyboardRows.add(keyboardRow);

        }
    }


    public void sendMsg(String msg) {
        try {
            execute(new SendMessage(145464749L, msg));
        } catch (TelegramApiException e) {
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
            return execute;

        } catch (SQLException e) {
            sendMsg(e.toString());
        }
        return (true);

    }


    @Override
    public void onClosing() {
        super.onClosing();
    }


}
