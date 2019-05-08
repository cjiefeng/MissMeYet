import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MissMeBot extends TelegramLongPollingBot {
    PhotoBank photoBank;
    String miss = "I miss you";
    String love = "I love you";
    String all = "OPQRSTU";


    public MissMeBot() {
        Firebase firebase = new Firebase();
        photoBank = new PhotoBank();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            long chat_id = update.getMessage().getChatId();
            String message_text = update.getMessage().getCaption();

            if(chat_id != 171558159) {
                sendMsg(chat_id, "Not allowed");
                return;
            }
            List<PhotoSize> photos = update.getMessage().getPhoto();
            String f_id = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getFileId();

            if (photoBank.addImage(message_text, f_id)) {
                sendMsg(chat_id, "added to " + message_text + "List successfully");
            } else {
                sendMsg(chat_id, "miss or love only");
            }

            System.out.println(f_id);
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            long chat_id = update.getMessage().getChatId();
            String message = update.getMessage().getText();

            // if(chat_id != 166724934) {
            if(chat_id != 171558159) {
                sendMsg(chat_id, message);
                return;
            }

            if (message.equals("/start")) {
                sendButtons(chat_id);
            } else if (message.equals(miss) || message.equals(love) || message.equals(all)){
                sendPht(chat_id, message);
            } else {
                sendMsg(chat_id, "use the buttons dearie");
            }
        }
    }

    /**
     * Method for creating a message and sending it.
     * @param chatId chat id
     * @param s The String that you want to send as a message.
     */
    public synchronized void sendMsg(long chatId, String s) {
        SendMessage msg = new SendMessage();
        msg.enableMarkdown(true);
        msg.setChatId(chatId);
        msg.setText(s);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for creating a message and sending it.
     * @param chatId chat id
     */
    public synchronized void sendPht(long chatId, String s) {
        SendPhoto pht = new SendPhoto();
        pht.setChatId(chatId);
        if (s.equals(miss)) {
            pht.setPhoto(photoBank.getRandomMiss());
        } else if (s.equals(love)) {
            pht.setPhoto(photoBank.getRandomLove());
        } else if (s.equals(all)) {
            pht.setPhoto(photoBank.getRandom());
        }
        try {
            execute(pht);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendButtons(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText("How are you feeling?");


        // Create a keyboard
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Create a list of keyboard rows
        List<KeyboardRow> keyboard = new ArrayList<>();

        // First keyboard row
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Add buttons to the first keyboard row
        keyboardFirstRow.add(new KeyboardButton(miss));

        // Second keyboard row
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Add the buttons to the second keyboard row
        keyboardSecondRow.add(new KeyboardButton(love));

        // Third keyboard row
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        // Add the buttons to the second keyboard row
        keyboardThirdRow.add(new KeyboardButton(all));

        // Add all of the keyboard rows to the list
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        // and assign this list to our keyboard
        replyKeyboardMarkup.setKeyboard(keyboard);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        return "MissMeYetBot";
        // return "echofishbot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        // missmmeyet
        return "800521747:AAFysK3kZs365WFkNj_SvsQh37-qbYwpAxI";
        // echofish
        // return "611529602:AAFZWSfQADkZ_VCspAlJMYny4--8HV4nzw8";
    }
}
