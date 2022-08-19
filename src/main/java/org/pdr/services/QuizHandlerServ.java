package org.pdr.services;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.model.quiz.Quiz;
import org.pdr.model.repository.MessageRepository;
import org.pdr.model.repository.QuizRepository;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

class QuizHandlerServ extends Service {
    MessageRepository messageRepository = new MessageRepository();
    QuizRepository quizRepository = new QuizRepository();

    @Override
    EnumOfServices processUpdate(InternalUpdate internalUpdate) {
        EnumOfServices nextServ = EnumOfServices.QUIZ_HANDLER;
        long chatId = internalUpdate.getChatId();
        Quiz quiz = quizRepository.getByChatId(chatId);
        List<MessageI> messageIS = quiz.processCallbackQuery(internalUpdate);
        messageIS.forEach(m -> m.setChatId(chatId));
        CHAT_SENDER.execute(messageIS);
        if (!quiz.isEnd()) {
            MessageI nextMessage = quiz.createNextMessage();
            Message execute = CHAT_SENDER.execute(nextMessage.setChatId(chatId));
            messageRepository.registrateNewMessageId(chatId, execute.getMessageId());
        } else {
            quizRepository.removeQuiz(chatId);
            messageRepository.removeMessage(chatId);
            nextServ = EnumOfServices.MAIN_MANU;
            MainMenuServ.sendButtons(chatId);
        }
        return nextServ;
    }
}
