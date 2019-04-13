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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

import static com.example.bot.linkbot.Vars.bikhialDeleteNakon;
import static com.example.bot.linkbot.Vars.yesIamSure;

/**
 * created By aMIN on 4/4/2019 12:27 PM
 */
@Component
public class LinkBot extends TelegramLongPollingBot {

    public static Collection<Method> methodinAnnotation(Class<?> classType, Class<? extends Annotation> annotationClass) {

        if (classType == null) throw new NullPointerException("classType must not be null");

        if (annotationClass == null) throw new NullPointerException("annotationClass must not be null");

        Collection<Method> result = new ArrayList<Method>();
        for (Method method : classType.getMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                Annotation annotation = method.getAnnotation(annotationClass);
                result.add(method);
            }
        }
        return result;
    }

//  for (Map.Entry<Method, RoutesMapping> methodAnnotationEntry : methodWithAnnotation.entrySet()) {
//        RoutesMapping annotationEntryValue = methodAnnotationEntry.getValue();
//        Routes value = annotationEntryValue.value();
//        System.out.println(value);
//        sendMsg(value.name + " " + methodAnnotationEntry.getKey().getName());
//    }


    public LinkBot() {
        Meths.sendToBot("constructor is working .................. ");


    }


    @Autowired
    private Connection connection;

    @Autowired
    private List<String> routes;

    @Autowired
    private int routeNumber;
    @Autowired
    private HashMap<Method, RoutesMapping> methodWithAnnotation;

    public void onReplyKey(Update update) {


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
        if (update.hasCallbackQuery()) {
            Message message = update.getCallbackQuery().getMessage();
            String data = update.getCallbackQuery().getData();

            if (data.contains(Vars.EDITID)) {


                String delete = data.replaceAll(Vars.EDITID, "");
                int idOfLink = Integer.parseInt(delete);

                try {
                    execute(new EditMessageReplyMarkup()
                            .setChatId(message.getChatId())
                            .setMessageId(message.getMessageId())
                            .setReplyMarkup(editWhich(delete))
                    );
                } catch (TelegramApiException e) {
                    sendMsg(e.toString());
                }
            }
            else if (data.contains("delete")) {

                String delete = data.replaceAll("delete", "");
                int idOfLink = Integer.parseInt(delete);

                try {
                    execute(new EditMessageReplyMarkup()
                            .setChatId(message.getChatId())
                            .setMessageId(message.getMessageId())
                            .setReplyMarkup(confirmDeleteLink(delete))
                    );
                } catch (TelegramApiException e) {
                    sendMsg(e.toString());
                }

            } else if (data.contains(yesIamSure)) {

                String delete = data.replaceAll(yesIamSure, "");
                int idOfLink = Integer.parseInt(delete);

                try {
                    connection.createStatement().execute("delete from link where id=" + idOfLink);
                    execute(new DeleteMessage(message.getChatId(), message.getMessageId()));

                } catch (SQLException e) {
                    sendMsg(e.toString());

                } catch (TelegramApiException e) {
                    sendMsg(e.toString());
                }
            }

            else if (data.contains(bikhialDeleteNakon)) {
                String delete = data.replaceAll(bikhialDeleteNakon, "");
                try {
                    execute(new EditMessageReplyMarkup()
                            .setChatId(message.getChatId())
                            .setMessageId(message.getMessageId())
                            .setReplyMarkup(candeleteMyLink(delete))
                    );

                } catch (TelegramApiException e) {
                    sendMsg(e.toString());
                }

            }
        }


        if (update.getMessage().isCommand()) {
            if (update.getMessage().getText().equals("/users")) {
                try {
                    ResultSet resultSet2 = connection.createStatement().executeQuery("select * from Muser;");
                    while (resultSet2.next()) {
                        execute(new SendMessage(update.getMessage().getChatId(), String.valueOf(resultSet2.getInt(1))));
                    }
                    return;
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
                    return;
                } catch (SQLException e) {
                    sendMsg(e.toString());

                }
            }
            if (update.getMessage().getText().equals("دل")) {
                try {
                    connection.createStatement().execute("delete  from Link");
                    return;
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
                            , Vars.StartMsg);
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

        @RoutesMapping(Routes.Scientific)
        public Response gune0() throws TelegramApiException {
            return getResponse();
        }

        @RoutesMapping(Routes.Entertainment)
        public Response gune1() throws TelegramApiException {
            return getResponse();
        }

        @RoutesMapping(Routes.Newsly)
        public Response gune2() throws TelegramApiException {
            return getResponse();
        }

        private Response getResponse() throws TelegramApiException {
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

        @RoutesMapping(Routes.GROUPSANDCHANNELSPEOPLE)
        public Response gune12() throws TelegramApiException {
            execute(new SendMessage(
                    update.getMessage().getChatId()
                    , update.getMessage().getText())
                    .setReplyMarkup(rr())

            );

            return this;
        }

        public Response gune13() throws TelegramApiException {
            try {


                Integer id = update.getMessage().getFrom().getId();
                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where  user_id='" + id + "'");

                while (resultSet2.next()) {
                    String name = resultSet2.getString("name");
                    String link = resultSet2.getString("link_src");
                    String idOfLink = resultSet2.getString("id");

                    execute(new SendPhoto()
                            .setPhoto(resultSet2
                                    .getString("photo_id"))
                            .setChatId(String.valueOf(id))
                            .setCaption(name + "\n" + link)
                            .setReplyMarkup(candeleteMyLink(idOfLink))

                    );


                }


            } catch (SQLException e) {
                e.printStackTrace();
            }


            return this;
        }


        @RoutesMapping(Routes.CANCELMAKEINGLINK)
        public Response gune14() {

            try {


                Integer id = update.getMessage().getFrom().getId();
                ResultSet resultSet2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE).executeQuery("select * from Link where status!='ADDED' and user_id='" + id + "'");

                while (resultSet2.next()) {
                    sendMsg("in next()");

                    sendMsg(String.valueOf(resultSet2.getLong(1)));
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

            } catch (TelegramApiException e) {
                sendMsg(e.toString());
            }

            return this;
        }


        @RoutesMapping(Routes.BACKTOSTART)
        public Response gune15() throws TelegramApiException {
            execute(new SendMessage().setReplyMarkup(start()).setChatId(update.getMessage().getChatId()).setText(Vars.StartMsg));
            return this;
        }


        public Response gune16() throws TelegramApiException {
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

        public Response gune17() throws TelegramApiException {
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

        public Response gune18() throws TelegramApiException {
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

        public Response gune19() throws TelegramApiException {
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

        public Response gune20() throws TelegramApiException {
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


        public Response gune21() throws TelegramApiException {
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

    private InlineKeyboardMarkup candeleteMyLink(String idOfLink) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> list = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton("حذف لینک")
                .setCallbackData("delete" + idOfLink);
        InlineKeyboardButton button2 = new InlineKeyboardButton(Vars.EDIT)
                .setCallbackData(Vars.EDITID+ idOfLink);

        list.add(button);
        list.add(button2);

        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(list);
        markup.setKeyboard(lists);
        return markup;
    }


    private InlineKeyboardMarkup editWhich(String idOfLink) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> list = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton("ویرایش لینک")
                .setCallbackData("editLink_src" + idOfLink);
        InlineKeyboardButton button2 = new InlineKeyboardButton("ویرایش نام")
                .setCallbackData("editname"+ idOfLink);
        InlineKeyboardButton button1 = new InlineKeyboardButton("ویرایش توضیحات")
                .setCallbackData("editDescrp"+ idOfLink);
        InlineKeyboardButton button3 = new InlineKeyboardButton("ویرایش کاور")
                .setCallbackData("editCover"+ idOfLink);

        list.add(button);
        list.add(button2);
        list.add(button3);
        list.add(button1);

        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(list);
        markup.setKeyboard(lists);
        return markup;

    }




    private InlineKeyboardMarkup confirmDeleteLink(String idOfLink) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> list = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton("100% مطمئنم")
                .setCallbackData(yesIamSure + idOfLink);
        InlineKeyboardButton button2 = new InlineKeyboardButton("نه بی خیال")
                .setCallbackData(bikhialDeleteNakon + idOfLink);
        list.add(button);
        list.add(button2);

        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(list);
        markup.setKeyboard(lists);
        return markup;
    }

    private ReplyKeyboard cancelAdding() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardButton button = new KeyboardButton(com.example.bot.linkbot.model.Routes.CANCELMAKEINGLINK.name);

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

        KeyboardButton button = new KeyboardButton(Routes.BACKTOSTART.name);
        KeyboardRow k1 = new KeyboardRow();
        k1.add(button);
        keyboardRows.add(k1);
        int k = 1;
        r(keyboardRows, values, k);
        replyKeyboardMarkup.setKeyboard(keyboardRows);

        replyKeyboardMarkup.setResizeKeyboard(true);
        return replyKeyboardMarkup;
    }


    ReplyKeyboardMarkup start() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardButton button = new KeyboardButton(Vars.GROUPSANDCHANNELSPEOPLE);
        KeyboardRow k1 = new KeyboardRow();
        k1.add(button);
        KeyboardButton button2 = new KeyboardButton(Vars.ADDINGLINKTO);
        KeyboardButton button3 = new KeyboardButton(Vars.MYLINKS);

        KeyboardRow k2 = new KeyboardRow();
        KeyboardRow k3 = new KeyboardRow();
        k2.add(button2);
        k3.add(button3);
        keyboardRows.add(k1);
        keyboardRows.add(k3);
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

}
