package org.pdr.services;

import org.pdr.Main;
import org.pdr.adatpers.ChatSenderI;
import org.pdr.adatpers.InternalUpdate;

abstract class Service {
    protected Service(){}
    protected static final ChatSenderI CHAT_SENDER = Main.CHAT_SENDER; // only service  Layer can send messages

    abstract EnumOfServices processUpdate(InternalUpdate internalUpdate);
}
