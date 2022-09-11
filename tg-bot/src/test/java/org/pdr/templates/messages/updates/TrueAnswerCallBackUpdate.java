package org.pdr.templates.messages.updates;

import org.pdr.templates.messages.SimpleMessage;

public class TrueAnswerCallBackUpdate extends SimpleCallBackUpdate {
    public TrueAnswerCallBackUpdate() {
        super();
        this.getCallbackQuery().setData("true");
        this.getCallbackQuery().setMessage(new SimpleMessage());
    }
}
