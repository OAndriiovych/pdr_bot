package org.pdr.templates.messages.updates;

import org.pdr.templates.messages.SimpleMessage;

public class FalseAnswerCallBackUpdate extends SimpleCallBackUpdate {
    public FalseAnswerCallBackUpdate() {
        super();
        this.getCallbackQuery().setData("False");
        this.getCallbackQuery().setMessage(new SimpleMessage());
    }
}
