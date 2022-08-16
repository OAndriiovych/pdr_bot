package org.pdr.adatpers.messages;

import org.pdr.utils.DataStructure;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
@DataStructure
public class TextMessage extends SendMessage implements MessageI {
    public TextMessage(String text, ReplyKeyboard replyKeyboard) {
        this(text);
        setReplyMarkup(replyKeyboard);
    }


    public TextMessage(String text) {
        setText(text);
    }

    public MessageI setButtons(List<List<String>> nameOfButtons) {
        List<KeyboardRow> commands = new ArrayList<>();
        for (List<String> nameOfButtonsByRows : nameOfButtons) {
            KeyboardRow rowOfButtons = new KeyboardRow();
            for (String nameOfButton : nameOfButtonsByRows) {
                rowOfButtons.add(nameOfButton);
            }
            commands.add(rowOfButtons);
        }
        setReplyMarkup(new ReplyKeyboardMarkup(commands, true, true, true, null));
        return this;
    }

    @Override
    public MessageI setChatId(long chatID) {
        super.setChatId(chatID + "");
        return this;
    }
}