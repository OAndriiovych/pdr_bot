package org.pdr.repository;

import org.pdr.entity.Payment;

import java.util.HashMap;
import java.util.Map;

public class PaymentRepository {

    private static final Map<Long, Payment> repository = new HashMap<>();

    public void save(Payment newPayment) {
        newPayment.setId(IdGenerator.getNextId());
        repository.put(newPayment.getLinkUser().getChatId(), newPayment);
    }

    public Payment getPaymentByChatId(long chatId) {
        return repository.get(chatId);
    }

    private static class IdGenerator {
        private static int lastId = 0;

        private static int getNextId() {
            return lastId++;
        }
    }
}


