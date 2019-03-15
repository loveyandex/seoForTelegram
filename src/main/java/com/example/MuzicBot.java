package com.example;


import com.example.pojos.Cons;
import com.example.pojos.MusicForSave;
import com.example.pojos.Status;
import com.google.gson.Gson;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputContactMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultAudio;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import javax.sql.DataSource;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * is created by aMIN on 1/25/2018 at 11:52 PM
 */
@Component
public class MuzicBot extends TelegramLongPollingBot {

    private Map<Integer, MusicForSave> userMusicForSave = new HashMap<>();


    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Autowired
    private Connection connection;

    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            String substring = data.substring(0, 4);
            if (substring.equalsIgnoreCase("song")) {
                try {
                    String fileId = data.substring(4);
                    execute(new SendAudio()
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setCaption(update.getCallbackQuery().getFrom().getFirstName())
                            .setAudio(fileId));

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }


        }

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM music4");
            while (rs.next())
                execute(new SendMessage(update.getMessage().getChatId(), String.valueOf(rs.getInt(1))));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        final Responses responses = new Responses(update);
        responses.inlineQuery(() -> responses
                .startMsg()
                .hasMessagesButNotStart()
                .addupUrl()
                .waitForURL()
                .addNameOfSong()
                .addPersianNameSong()
                .addNameOfSinger()
                .addPersianNameOFSinger()
                .hasCallbackQuery()
        );
    }

    private class Responses {
        Update update;

        public Responses(Update update) {
            this.update = update;
        }

        private Responses hasMessagesButNotStart() {
            System.err.println(new Gson().toJson(userMusicForSave));
            if (update.hasMessage()) {
                if (!update.getMessage().getText().equalsIgnoreCase("/start")) {

                    final Audio audio = update.getMessage().getAudio();
                    if (audio != null)
                        try {
                            execute(new SendAudio().setAudio(audio.getFileId())
                                            .setCaption(update.getMessage().getCaption())
                                            .setChatId(update.getMessage().getChatId())
//                            .setReplyMarkup(setWebsites())
                            );
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }


                    if (update.getMessage().hasText()) {
                        try {
                            execute(new SendMessage(update.getMessage().getChatId(), "update.getMessage().hasText()"));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }

                        final String query = update.getMessage().getText();
                        final MusicForSave musicForSave = userMusicForSave.get(Math.toIntExact(update.getMessage().getChatId()));
                        if (musicForSave == null)
                            if (isPersian(query) && query.length() > 1) {
                                try {
                                    execute(new SendMessage(update.getMessage().getChatId(), "isPersian"));
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                                final ArrayList<ArrayList<String>> persiansearchindb = persiansearchindb(query);
                                if (persiansearchindb.size() == 0) {
                                    try {
                                        execute(new SendMessage(update.getMessage().getChatId(), "persian res sizze:" + persiansearchindb.size()));
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                } else {
//                                    new Thread(() -> datatoMsg(update, persiansearchindb)).start();
                                    SendMessage method = new SendMessage();
                                    method.setChatId(update.getMessage().getChatId());
                                    method.setText("کدوم اهنگو میخوای");
                                    method.setReplyMarkup(showSongsForSelect(persiansearchindb));
                                    try {
                                        execute(method);
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } else if (!isPersian(query) && query.length() > 1) {
                                final ArrayList<ArrayList<String>> searchindbFingilish = searchindbFingilish(query);
                                if (searchindbFingilish.size() == 0) {
                                    try {
                                        execute(new SendMessage(update.getMessage().getChatId(), "eng res sizze:" + searchindbFingilish.size()));
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                } else
                                    new Thread(() -> datatoMsg(update, searchindbFingilish)).start();
                            }


                    }
//                Message messagehamid = sendAudio(
//                        new SendAudio().setChatId("@musicaminbot")
//                                .setAudio(/*"https://t.me/musicaminbot/9"*/"https://t.me/musicaminbot/54")
//                                .setCaption("god is great")
//                );
//                System.out.println(messagehamid.getAudio().getFileId());

//                System.out.println(sendVoice(new SendVoice().setVoice("https://t.me/musicaminbot/54").
//                        setChatId("@musicaminbot")).getVoice().getFileId());

                }
            }

            return this;
        }

        private Responses inlineQuery(OnMessageHandler messageHandler) {
            System.out.println("in inline");

            if (update.hasInlineQuery()) {
                final User from = update.getInlineQuery().getFrom();
                System.err.println(new Gson().toJson(from));

                String query = update.getInlineQuery().getQuery();
                System.err.println(query);
                try {
                    if (!query.isEmpty() && query.length() >= 3 && !isPersian(query)) {
                        final ArrayList<ArrayList<String>> searchindb1 = searchindbFingilish(query);
                        if (searchindb1.size() == 0)
                            execute(new AnswerInlineQuery().setInlineQueryId(update.getInlineQuery().getId())
                                    .setResults(new InlineQueryResultArticle()
                                            .setDescription("Sorry nothing to show...")
                                            .setId("1").setTitle(from.getFirstName())
                                            .setInputMessageContent(new InputContactMessageContent().setFirstName("amin").setPhoneNumber("+989351844321"))
                                    ));
                        else {
                            InlineQueryResultAudio[] inlineQueryResults = datatoInline(searchindb1);
                            execute(new AnswerInlineQuery().setInlineQueryId(update.getInlineQuery().getId()).
                                    setResults(inlineQueryResults));
                        }
                    } else if (query.isEmpty() || query.length() < 3) {
                        execute(new AnswerInlineQuery().setInlineQueryId(update.getInlineQuery().getId())
                                .setResults(new InlineQueryResultArticle()
                                        .setDescription("write more...")
                                        .setId("2525").setTitle("Hi " + from.getFirstName())
                                        .setInputMessageContent(new InputContactMessageContent().setFirstName("amin").setPhoneNumber("+989351844321"))
                                ));

                    } else if (!query.isEmpty() && query.length() >= 3 && isPersian(query)) {
                        final ArrayList<ArrayList<String>> persiansearchindb = persiansearchindb(query);
                        if (persiansearchindb.size() == 0)
                            execute(new AnswerInlineQuery().setInlineQueryId(update.getInlineQuery().getId())
                                    .setResults(new InlineQueryResultArticle()
                                            .setDescription("Sorry nothing to show...")
                                            .setId("148456").setTitle(from.getFirstName())
                                            .setInputMessageContent(new InputContactMessageContent().setFirstName("amin").setPhoneNumber("+989351844321"))
                                    ));
                        else {
                            InlineQueryResultAudio[] inlineQueryResults = datatoInline(persiansearchindb);

                            execute(new AnswerInlineQuery().setInlineQueryId(update.getInlineQuery().getId()).
                                    setResults(inlineQueryResults));
                        }
                    }
//                sendApiMethod(new SendMessage(String.valueOf(update.getInlineQuery().getFrom().getId()), query));
//                answerInlineQuery(new AnswerInlineQuery().setInlineQueryId(update.getInlineQuery().getId()).
//                        setResults(new InlineQueryResultAudio().setAudioUrl("https://t.me/musicaminbot/54")
//                                .setCaption("shadmehr")
//                                .setTitle("shadmehr")
//                                        .setId("1"),
//                                new InlineQueryResultAudio().setAudioUrl("http://dl.nex1music.ir/1397/09/07/Mohsen%20Chavoshi%20-%20Che%20Shod.mp3?time=1545243237&filename=/1397/09/07/Mohsen%20Chavoshi%20-%20Che%20Shod.mp3")
//                                        .setCaption("che shod mohsen chavoshi")
//                                .setTitle("mohsen chavoshi che shod")
//                                .setId("2")

//,
//                                new InlineQueryResultAudio().setAudioUrl("http://dl.nex1music.ir/1397/08/04/Behnam%20Bani%20-%20Del%20Nakan.mp3?time=1545322590&filename=/1397/08/04/Behnam%20Bani%20-%20Del%20Nakan.mp3")
//                                        .setCaption("Behnam Bani Del Nakan")
//                                .setTitle("Del Nakan Behnam")
//                                .setId("3")
//
//,
//                                new InlineQueryResultAudio().setAudioUrl("http://dl.nex1music.ir/1397/08/27/Mehdi%20Ahmadvand%20-%20Naro.mp3?time=1545322402&filename=/1397/08/27/Mehdi%20Ahmadvand%20-%20Naro.mp3")
//                                        .setCaption("Mehdi ahmad vand Naro")
//                                .setTitle("Mehdi ahmad vand Naro")
//                                .setId("4")
//
//                        ));

                } catch (TelegramApiException e) {
                    System.out.println(e);
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                messageHandler.handle();
            }

            return this;
        }

        private Responses startMsg() {
            System.out.println("in 2");

            if (update.hasMessage())
                if (update.getMessage().hasText())
                    if (update.getMessage().getText().equalsIgnoreCase("/start2")
                            && userMusicForSave.get(Math.toIntExact(update.getMessage().getChatId())) == null) {
                        System.out.println("start2");
                        final User from = update.getMessage().getFrom();
                        System.err.println(new Gson().toJson(from));
                        final SendMessage sendMessage = new SendMessage();
                        sendMessage
                                .setText("god is great")
                                .setChatId(update.getMessage().getChatId());
                        sendMessage.setReplyMarkup(fullOptions());
                        sendApiMethodAsync(sendMessage, new SentCallback<Message>() {
                            @Override
                            public void onResult(BotApiMethod<Message> method, Message response) {

                            }

                            @Override
                            public void onError(BotApiMethod<Message> method, TelegramApiRequestException apiException) {

                            }

                            @Override
                            public void onException(BotApiMethod<Message> method, Exception exception) {

                            }
                        });


                    } else if (update.getMessage().getText().equalsIgnoreCase("/start")
                            && userMusicForSave.get(Math.toIntExact(update.getMessage().getChatId())) == null) {

                        System.out.println("start");
                        final User from = update.getMessage().getFrom();
                        System.err.println(new Gson().toJson(from));
                        final SendMessage sendMessage = new SendMessage();
                        sendMessage
                                .setText("write song or singer name farsi ya fingilish")
                                .setChatId(update.getMessage().getChatId());
                        sendApiMethodAsync(sendMessage, new SentCallback<Message>() {
                            @Override
                            public void onResult(BotApiMethod<Message> method, Message response) {

                            }

                            @Override
                            public void onError(BotApiMethod<Message> method, TelegramApiRequestException apiException) {

                            }

                            @Override
                            public void onException(BotApiMethod<Message> method, Exception exception) {

                            }
                        });

                    }

            return this;
        }


        private Responses addupUrl() {
            System.out.println("in 3");

            if (update.hasMessage())
                if (update.getMessage().hasText())
                    if (update.getMessage().getText().equals(Cons.ADDUPASNG)
                            && userMusicForSave.get(Math.toIntExact(update.getMessage().getChatId())) == null) {
                        final SendMessage method = new SendMessage();

                        try {
                            final Message now = execute(new SendMessage(update.getMessage().getChatId(), "now").setReplyMarkup(new ReplyKeyboardRemove()));
                            execute(new DeleteMessage(update.getMessage().getChatId(), now.getMessageId()));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }


                        method
                                .setChatId(update.getMessage().getChatId())
                                .setText(Cons.ENTERURLORFILE)
                                .setReplyMarkup(cancelupingInlineBtn());

                        sendApiMethodAsync(method, new SentCallback<Message>() {
                            @Override
                            public void onResult(BotApiMethod<Message> method, Message response) {
                                MusicForSave musicForSave = new MusicForSave().setStatus(Status.First);
                                userMusicForSave.put(update.getMessage().getFrom().getId(), musicForSave);

                            }

                            @Override
                            public void onError(BotApiMethod<Message> method, TelegramApiRequestException apiException) {

                            }

                            @Override
                            public void onException(BotApiMethod<Message> method, Exception exception) {

                            }
                        });
                    }

            return this;
        }


        private Responses waitForURL() {
            System.out.println("in 4");

            if (update.hasMessage()) {
                final Message message = update.getMessage();
                final MusicForSave musicForSave = userMusicForSave.get(message.getFrom().getId());
                if (musicForSave != null)
                    if (musicForSave.getStatus() == Status.First) {
                        final String url = message.getText();
                        if (url != null)
                            if (iscorrect(url)) {
                                musicForSave.setSrc_url(url);
                                sendApiMethodAsync(new SendMessage()
                                                .setChatId(message.getChatId())
                                                .setText(Cons.ADDNAMEOFSONG)

                                        , new SentCallback<Message>() {
                                            @Override
                                            public void onResult(BotApiMethod<Message> method, Message response) {
                                                musicForSave.setStatus(Status.Second);

                                            }

                                            @Override
                                            public void onError(BotApiMethod<Message> method, TelegramApiRequestException apiException) {

                                            }

                                            @Override
                                            public void onException(BotApiMethod<Message> method, Exception exception) {

                                            }
                                        });

//                                    TOChannel.toChannelOne(musicForSave, update, MuzicBot.this);

                            } else {
                                try {
                                    execute(new SendChatAction().setAction(ActionType.TYPING).setChatId(message.getChatId()));
                                    execute(new SendMessage().setText("is not correct url plz send correct url link")
                                            .setChatId(message.getChatId())
                                            .setReplyMarkup(cancelupingInlineBtn())
                                    );
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            }
                    }
            }
            return this;
        }

        public Responses addNameOfSong() {
            System.out.println("in 5");
            if (update.hasMessage())
                if (update.getMessage().hasText()) {
                    final Message message = update.getMessage();
                    final Long chatId = message.getChatId();
                    System.out.println("caht id " + chatId);
                    final MusicForSave musicForSave1 = userMusicForSave.get(Math.toIntExact(chatId));
                    if (musicForSave1 != null)
                        if (musicForSave1.getStatus() == Status.Second) {
                            musicForSave1.setNameOfSong(message.getText());
                            sendApiMethodAsync(new SendMessage(update.getMessage().getChatId()
                                            , "now send persian keyboard name of song like \n تو در مسافت بارانی")
                                    , new SentCallback<Message>() {
                                        @Override
                                        public void onResult(BotApiMethod<Message> method, Message response) {
                                            musicForSave1.setStatus(Status.Third);

                                        }

                                        @Override
                                        public void onError(BotApiMethod<Message> method, TelegramApiRequestException apiException) {

                                        }

                                        @Override
                                        public void onException(BotApiMethod<Message> method, Exception exception) {

                                        }
                                    });
                        }

                }


            return this;
        }

        public Responses addPersianNameSong() {
            System.out.println("in 6");
            if (update.hasMessage())
                if (update.getMessage().hasText()) {
                    final Message message = update.getMessage();
                    final Long chatId = message.getChatId();
                    System.out.println("caht id " + chatId);
                    final MusicForSave musicForSave1 = userMusicForSave.get(Math.toIntExact(chatId));
                    if (musicForSave1 != null)
                        if (musicForSave1.getStatus() == Status.Third) {
                            musicForSave1.setPersianNameOfSong(message.getText());
                            sendApiMethodAsync(new SendMessage(update.getMessage().getChatId()
                                            , "now send keyboard name of singer like \nMohsen Chavoshi")
                                    , new SentCallback<Message>() {
                                        @Override
                                        public void onResult(BotApiMethod<Message> method, Message response) {
                                            musicForSave1.setStatus(Status.Forth);

                                        }

                                        @Override
                                        public void onError(BotApiMethod<Message> method, TelegramApiRequestException apiException) {

                                        }

                                        @Override
                                        public void onException(BotApiMethod<Message> method, Exception exception) {

                                        }
                                    });

                        }

                }

            return this;
        }

        public Responses addNameOfSinger() {
            System.out.println("in 8");
            if (update.hasMessage())
                if (update.getMessage().hasText()) {
                    final Message message = update.getMessage();
                    final Long chatId = message.getChatId();
                    System.out.println("caht id " + chatId);
                    final MusicForSave musicForSave1 = userMusicForSave.get(Math.toIntExact(chatId));
                    if (musicForSave1 != null)
                        if (musicForSave1.getStatus() == Status.Forth) {
                            musicForSave1.setNameOfSinger(message.getText());
                            sendApiMethodAsync(new SendMessage(update.getMessage().getChatId()
                                            , "now send farsi keyboard name of singer like \nمحسن چاوشی")
                                    , new SentCallback<Message>() {
                                        @Override
                                        public void onResult(BotApiMethod<Message> method, Message response) {
                                            musicForSave1.setStatus(Status.Five);

                                        }

                                        @Override
                                        public void onError(BotApiMethod<Message> method, TelegramApiRequestException apiException) {

                                        }

                                        @Override
                                        public void onException(BotApiMethod<Message> method, Exception exception) {

                                        }
                                    });

                        }

                }


            return this;
        }


        public Responses addPersianNameOFSinger() {
            System.out.println("in 8");

            if (update.hasMessage())
                if (update.getMessage().hasText()) {
                    final Message message = update.getMessage();
                    final Long chatId = message.getChatId();
                    System.out.println("caht id " + chatId);
                    final MusicForSave musicForSave1 = userMusicForSave.get(Math.toIntExact(chatId));
                    if (musicForSave1 != null)
                        if (musicForSave1.getStatus() == Status.Five) {
                            musicForSave1.setPersianNameOFSinger(message.getText());
                            sendApiMethodAsync(new SendMessage(update.getMessage().getChatId()
                                            , "thanks your song added to bot")
                                    , new SentCallback<Message>() {
                                        @Override
                                        public void onResult(BotApiMethod<Message> method, Message response) {
                                            musicForSave1.setStatus(Status.Six);
                                            finallyAddSong();

                                        }

                                        @Override
                                        public void onError(BotApiMethod<Message> method, TelegramApiRequestException apiException) {

                                        }

                                        @Override
                                        public void onException(BotApiMethod<Message> method, Exception exception) {

                                        }
                                    });

                        }

                }

            return this;
        }


        public Responses finallyAddSong() {
            System.out.println("in fillay");
            if (update.hasMessage())
                if (update.getMessage().hasText()) {
                    final Message message = update.getMessage();
                    final Long chatId = message.getChatId();
                    final int key = Math.toIntExact(chatId);
                    final MusicForSave musicForSave1 = userMusicForSave.get(key);
                    if (musicForSave1 != null)
                        if (musicForSave1.getStatus() == Status.Six) {
                        }

                }

            return this;
        }


        private Responses hasCallbackQuery() {
            System.out.println("in has call");
            if (update.hasMessage())
                if (update.getMessage().hasText())
                    System.err.println(update.getMessage().getText());
            if (update.hasCallbackQuery()) {
                final boolean cancel = update.getCallbackQuery().getData().equalsIgnoreCase("cancel");

                try {
                    execute(new SendMessage(update.getCallbackQuery().getMessage().getChatId(), "kit"));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }


                if (cancel) {
                    final Message message = update.getCallbackQuery().getMessage();
                    final Integer id = Math.toIntExact(message.getChat().getId());


//
//                    new EditMessageReplyMarkup()
//                            .setReplyMarkup(ok()).setChatId(String.valueOf(id))
//                            .setMessageId(message.getMessageId());

                    sendApiMethodAsync(
                            new EditMessageText().setText("thanks")
                                    .setChatId(String.valueOf(id))
                                    .setMessageId(message.getMessageId())
                                    .setReplyMarkup(tryAgain())

                            , new SentCallback<Serializable>() {
                                @Override
                                public void onResult(BotApiMethod<Serializable> method, Serializable response) {
                                    System.out.println("on result");
                                }

                                @Override
                                public void onError(BotApiMethod<Serializable> method, TelegramApiRequestException apiException) {
                                    System.out.println(apiException);
                                }

                                @Override
                                public void onException(BotApiMethod<Serializable> method, Exception exception) {
                                    System.out.println(exception);

                                }
                            });

                    System.out.println(id);
                    final MusicForSave musicForSave = userMusicForSave.get(id);
                    if (musicForSave != null)
                        userMusicForSave.remove(id);
                    else
                        System.out.println(musicForSave);

                }
                System.out.println(cancel);
            }
            return this;
        }


    }

    private ReplyKeyboardMarkup fullOptions() {
        final ArrayList<KeyboardRow> keyboard = new ArrayList<KeyboardRow>() {{
            final KeyboardRow e = new KeyboardRow();
            e.add(Cons.ADDUPASNG);
            add(e);
        }};
        keyboard.trimToSize();
        return new ReplyKeyboardMarkup().setResizeKeyboard(true).setKeyboard(keyboard);
    }

    private boolean iscorrect(String url) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }

    public static String urlToCorrectUrl(String url) throws MalformedURLException {

        URL euUrl = new URL(url);

        final String path = euUrl.getPath();
//            System.err.println(path);
        String protocol = euUrl.getProtocol();
        final String encode = path.replaceAll(" ", "%20");
//            System.err.println(encode);

        final String host = euUrl.getHost();
        final String src_url = protocol + "://" + host + encode;
        return src_url;
    }

    private InlineQueryResultAudio[] datatoInline(ArrayList<ArrayList<String>> searchindb1) throws MalformedURLException {
        InlineQueryResultAudio[] jInlineQueryResultAudio = new InlineQueryResultAudio[searchindb1.size()];
        for (int i = 0; i < jInlineQueryResultAudio.length; i++) {
            final ArrayList<String> list = searchindb1.get(i);
            final String audioUrl = list.get(1);
            final String src_url = urlToCorrectUrl(audioUrl);
            System.err.println(src_url);
            jInlineQueryResultAudio[i] = new InlineQueryResultAudio()
                    .setAudioUrl(audioUrl)
                    .setCaption(list.get(2))
                    .setTitle(list.get(2))
                    .setId(String.format("%d", (i + 56)));
        }
        return jInlineQueryResultAudio;
    }

    private void datatoMsg(Update update, ArrayList<ArrayList<String>> searchindb1) {
        System.out.println(searchindb1.size());
        for (final ArrayList<String> list : searchindb1) {
            final String audioUrl = list.get(1);

            final SendAudio sendAudio = new SendAudio().setChatId(update.getMessage().getChatId())
                    .setAudio(audioUrl)
                    .setCaption(list.get(2))
                    .setReplyMarkup(cancelupingInlineBtn());

            try {
                execute(sendAudio);
            } catch (TelegramApiException e) {
                System.err.println("on catch");
                try {
                    execute(sendAudio);
                } catch (TelegramApiException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private ArrayList<ArrayList<String>> searchindbFingilish(String query) {
        ArrayList<ArrayList<String>> results = new ArrayList<>();
        try {
            Statement   statement = connection.createStatement();
            String q = "select  * from music4 WHERE " + "LOWER(tags) like '%" + query.toLowerCase() + "%' order by tags desc limit 50;";
            ResultSet resultSet = statement.executeQuery(q);
            while (resultSet.next()) {
                final ArrayList<String> row = new ArrayList<>();
                final String name = resultSet.getString(1);
                final String src_url = resultSet.getString(10);
                final String tags = resultSet.getString(3);
                row.add(name);
                row.add(src_url);
                row.add(tags);
                results.add(row);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;

    }

    private ArrayList<ArrayList<String>> persiansearchindb(String query) {
        ArrayList<ArrayList<String>> results = new ArrayList<>();
        Statement statement;
        try {
            statement = connection.createStatement();

            String q = "select * from music4 WHERE " + "tags_persian like '%" + query + "%'";
            ResultSet resultSet = statement.executeQuery(q);
            while (resultSet.next()) {
                final ArrayList<String> row = new ArrayList<>();
                final String name = resultSet.getString(6);
                final String src_url = resultSet.getString(10);
                final String tags = resultSet.getString(8);
                row.add(name);
                row.add(src_url);
                row.add(tags);
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(results.size());
        return results;
    }


    public static InlineKeyboardMarkup cancelupingInlineBtn() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        String[] items = new String[]{"cancel uping"};
        String[] hostNamesItems = new String[]{"cancel"};
        List<InlineKeyboardButton> list = new ArrayList<InlineKeyboardButton>();
        for (int i = 0; i < items.length; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton(items[i])
                    .setCallbackData(hostNamesItems[i]);
            list.add(button);
        }

        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(list);
        markup.setKeyboard(lists);
        return markup;
    }

    public static InlineKeyboardMarkup showSongsForSelect(ArrayList<ArrayList<String>> songs) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();


        List<List<InlineKeyboardButton>> lists = new ArrayList<>();

        for (int i = 0; i < songs.size(); i++) {
            List<InlineKeyboardButton> list = new ArrayList<>();
            for (int j = 0; j < 2 && i < songs.size(); j++) {
                InlineKeyboardButton button = new InlineKeyboardButton("⭕️" + songs.get(i).get(2))
                        .setCallbackData("song" + songs.get(i++).get(1));
                list.add(button);
            }

            lists.add(list);
        }

        markup.setKeyboard(lists);
        return markup;
    }

    public static InlineKeyboardMarkup tryAgain() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        String[] items = new String[]{"try again..."};
        String[] hostNamesItems = new String[]{"tryAgain"};
        List<InlineKeyboardButton> list = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton(items[i])
                    .setCallbackData(hostNamesItems[i]);
            list.add(button);
        }

        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(list);
        markup.setKeyboard(lists);
        return markup;
    }


    private static MuzicBot muzicBot = new MuzicBot();

    public static MuzicBot getMuzicBot() {
        return muzicBot;
    }

    private MuzicBot() {
    }

    private boolean isPersian(String query) {
        String xx = new String(new char[]{query.charAt(0)});
        final byte[] bytes = xx.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            final byte aByte = bytes[i];
            if (aByte > 0)
                return false;
            else
                return true;
        }
        return false;
    }


    public String getBotUsername() {
        return "melodyAminBot";
    }

    public String getBotToken() {
        return "495402062:AAFW20xQIExpqkfbZpoDtbP_fflq1WznJIM";
    }

}
