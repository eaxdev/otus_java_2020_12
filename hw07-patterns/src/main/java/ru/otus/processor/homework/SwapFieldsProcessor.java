package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class SwapFieldsProcessor implements Processor {

    @Override
    public Message process(Message message) {
        var currentField11 = message.getField11();
        var currentField12 = message.getField12();

        return message.toBuilder()
                .field11(currentField12)
                .field12(currentField11)
                .build();
    }
}
