package org.pdr.model.payment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.pdr.adatpers.messages.MessageI;
import org.pdr.entity.Payment;
import org.pdr.entity.User;
import org.pdr.repository.PaymentRepository;
import org.pdr.repository.UserRepository;
import org.pdr.utils.MyProperties;

import java.io.IOException;
import java.util.List;

class PaymentModelTest {
    @BeforeAll
    static void preloadProp() {
        try {
            MyProperties.reloadPropertiesFile("src/test/resources/bot.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSuccessPaymentCreation() {
        PaymentModel paymentModel = new PaymentModel();
        UserRepository userRepository = new UserRepository();
        PaymentRepository paymentRepository = new PaymentRepository();
        long chatId = 1;
        User testUser = new User(chatId, 1);
        userRepository.save(testUser);

        List<MessageI> payment = paymentModel.createPayment(chatId);
        Payment testPayment = paymentRepository.getPaymentByUser(testUser);

        Assertions.assertEquals(2, payment.size());
        Assertions.assertNotNull(testPayment);

        paymentRepository.deletePayment(testPayment);
        userRepository.deleteUser(testUser);
    }

    @Test
    void testPaidPayment() {
        PaymentModel paymentModel = new PaymentModel();
        UserRepository userRepository = new UserRepository();
        PaymentRepository paymentRepository = new PaymentRepository();
        long chatId = 1;
        User testUser = new User(chatId, 1);
        userRepository.save(testUser);
        Payment testPayment = new Payment();
        testPayment.setPaid(true);
        testPayment.setLinkUser(testUser);
        paymentRepository.save(testPayment);

        List<MessageI> payment = paymentModel.createPayment(chatId);

        Assertions.assertEquals(1, payment.size());

        paymentRepository.deletePayment(testPayment);
        userRepository.deleteUser(testUser);
    }

}