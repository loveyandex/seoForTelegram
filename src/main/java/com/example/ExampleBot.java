package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

/**
 * 
 * This example bot is an echo bot that just repeats the messages sent to him
 *
 */
@Component
public class ExampleBot extends TelegramLongPollingBot {
	private static final Logger logger = LoggerFactory.getLogger(ExampleBot.class);
	@Value("700687388:AAHGmzovGb0LVXKRZAkechWrHBst7BJPMjw")
	private String token;
	
	@Value("codeTakhfif_bot")
	private String username;
	
	@Override
	public String getBotToken() {
		return "700687388:AAHGmzovGb0LVXKRZAkechWrHBst7BJPMjw";
	}
	
	@Override
	public String getBotUsername() {
		return "codeTakhfif_bot";
	}
	
	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			Message message = update.getMessage();
			SendMessage response = new SendMessage();
			Long chatId = message.getChatId();
			response.setChatId(chatId);
			String text = message.getText();
			response.setText(text);
			try {
				execute(response);
				logger.info("Sent message \"{}\" to {}", text, chatId);
			} catch (TelegramApiException e) {
				logger.error("Failed to send message \"{}\" to {} due to error: {}", text, chatId, e.getMessage());
			}
		}
	}

	@PostConstruct
	public void start() {
		logger.info("username: {}, token: {}", username, token);
	}

}
