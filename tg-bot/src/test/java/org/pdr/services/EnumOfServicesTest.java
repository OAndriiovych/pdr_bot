package org.pdr.services;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.templates.messages.updates.SimpleCallBackUpdate;
import org.pdr.templates.messages.updates.SimpleTextUpdate;

import java.lang.reflect.Field;

class EnumOfServicesTest {


    void testAllServiceFaultTolerance() throws NoSuchFieldException, IllegalAccessException {
        Field privateField = EnumOfServices.class.getDeclaredField("enumService");
        privateField.setAccessible(true);
        for (EnumOfServices sut : EnumOfServices.values()) {
            Service service = (Service) privateField.get(sut);
            service.processUpdate(new InternalUpdate(new SimpleTextUpdate()));
            service.processUpdate(new InternalUpdate(new SimpleCallBackUpdate()));
        }
    }
}