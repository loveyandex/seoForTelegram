import com.google.gson.Gson;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.sql.SQLException;

/**
 * created By aMIN on 3/28/2019 7:13 PM
 */

public class DeployingToChannelAndToDb extends TelegramLongPollingBot {

    public static DeployingToChannelAndToDb getInstance() {
        return new DeployingToChannelAndToDb();
    }

    public static void main(String[] args) throws SQLException {

        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            api.registerBot(new DeployingToChannelAndToDb());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        System.out.println(new Gson().toJson(message));

        System.out.println(new Gson().toJson(update));



        Message channelPost = update.getChannelPost();
        if (channelPost != null) {
            System.err.println(new Gson().toJson(channelPost));
            DeleteMessage method = new DeleteMessage();
            String chatId = String.valueOf(channelPost.getChatId());
            System.out.println(chatId);
            method.setChatId(chatId)
                    .setMessageId(channelPost.getMessageId());

            try {
                execute(method);
            } catch (TelegramApiException e) {
                e.printStackTrace();

            }


        }
    }

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return "858001251:AAGIgPCmRaHjZlFsdxIH7IMCcNH67RTYT_Y";
    }
}
