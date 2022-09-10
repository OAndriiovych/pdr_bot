package org.pdr.services;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;

public abstract class Service {
    protected Service(){}
    protected abstract Response processUpdate(InternalUpdate internalUpdate);
    protected abstract MessageI getDefaultMessage();
}
