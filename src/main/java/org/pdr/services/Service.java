package org.pdr.services;

import org.pdr.adatpers.InternalUpdate;

public interface Service {
    public abstract Service processUpdate(InternalUpdate internalUpdate);
}
