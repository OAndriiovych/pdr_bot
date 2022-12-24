package org.pdr.services;

import org.pdr.adatpers.InternalUpdate;
import org.pdr.adatpers.messages.MessageI;

public abstract class Service {
    protected EnumOfServices currentServ = EnumOfServices.valueOf(this.getClass());

    Response processUpdate(InternalUpdate internalUpdate) {
        Response response;
        if (internalUpdate.isTextMessage()) {
            response = processTextMessage(internalUpdate);
        } else if (internalUpdate.isCallBack()) {
            response = processCallbackQuery(internalUpdate);
        } else if (internalUpdate.isReply()) {
            response = processReply(internalUpdate);
        } else {
            //#TODO illegal situation add logger
            response = getDef();
        }
        return response;
    }

    private Response getDef() {
        Response response = new Response(currentServ);
        response.setSendDefaultMessage(false);
        return response;
    }

    protected Response processTextMessage(InternalUpdate internalUpdate) {
        return getDef();
    }

    protected Response processCallbackQuery(InternalUpdate internalUpdate) {
        return getDef();
    }

    protected Response processReply(InternalUpdate internalUpdate) {
        return getDef();
    }

    protected abstract MessageI getDefaultMessage();
}
