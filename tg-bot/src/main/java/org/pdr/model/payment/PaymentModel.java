package org.pdr.model.payment;

import org.pdr.adatpers.messages.MessageI;
import org.pdr.adatpers.messages.TextMessage;
import org.pdr.entity.Payment;
import org.pdr.entity.User;
import org.pdr.repository.PaymentRepository;
import org.pdr.repository.UserRepository;
import org.pdr.utils.LiqPayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PaymentModel {
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final LiqPayUtil liqPayUtil;

    public PaymentModel() {
        this.userRepository = new UserRepository();
        this.paymentRepository = new PaymentRepository();
        this.liqPayUtil = new LiqPayUtil();
    }

    public List<MessageI> createPayment(long chatId) {
        List<MessageI> mess = new ArrayList<>();
        User userByChatId = Objects.requireNonNull(userRepository.getUserByChatId(chatId));
        Payment payment = getOrCreatePayment(userByChatId);
        if (payment.isPaid()) {
            mess.add(new TextMessage("Ваша підписка ще активна"));
        } else {
            String urlForPayment = liqPayUtil.createUrlForPayment(payment);
            mess.add(new TextMessage("Сторінка для оплати готова"));
            mess.add(new TextMessage(urlForPayment));
        }
        return mess;
    }


    private Payment getOrCreatePayment(User user) {
        Payment payment = paymentRepository.getPaymentByUser(user);
        if (payment == null) {
            payment = new Payment();
            payment.setLinkUser(user);
            //#TODO add Logger
            paymentRepository.save(payment);
        }
        return payment;
    }
}
