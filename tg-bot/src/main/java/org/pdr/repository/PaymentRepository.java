package org.pdr.repository;

import org.pdr.entity.Payment;
import org.pdr.entity.User;

import java.util.HashMap;
import java.util.Map;

public class PaymentRepository {

    private static final Map<Long, Payment> repository = new HashMap<>();

    public void save(Payment newPayment) {
        if (newPayment.getId() == null) {
            newPayment.setId(IdGenerator.getNextId());
        }
        repository.put(newPayment.getLinkUser().getUserId(), newPayment);
    }

    public Payment getPaymentByUser(User chatId) {
        return repository.get(chatId.getUserId());
    }

    public void deletePayment(Payment payment) {
        repository.remove(payment.getLinkUser().getUserId());
    }

    private static class IdGenerator {
        private static int lastId = 0;

        private static int getNextId() {
            return lastId++;
        }
    }
}


