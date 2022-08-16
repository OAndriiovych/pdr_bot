package org.pdr.services;

import org.pdr.Main;
import org.pdr.adatpers.ChatSender;
import org.pdr.adatpers.InternalUpdate;

public interface Service {
    ChatSender CHAT_SENDER = Main.CHAT_SENDER;

    Service processUpdate(InternalUpdate internalUpdate);
}
