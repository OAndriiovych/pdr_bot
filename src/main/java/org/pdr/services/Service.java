package org.pdr.services;

import org.pdr.adatpers.InternalUpdate;

public abstract class Service {
    public abstract Service processUpdate(InternalUpdate internalUpdate);
}
