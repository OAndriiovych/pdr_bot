package org.pdr.adatpers.messages;

public interface MessageI<T extends MessageI> {

    T setChatId(long chatID);

}
