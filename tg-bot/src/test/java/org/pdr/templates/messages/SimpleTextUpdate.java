package org.pdr.templates.messages;

import org.telegram.telegrambots.meta.api.objects.Update;

public class SimpleTextUpdate extends Update {
    public SimpleTextUpdate() {
        setUpdateId(123456);
        setMessage(new SimpleMessage());
    }
}
