package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.listener.homework.MemoryStorage;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.homework.SwapFieldsProcessor;
import ru.otus.processor.homework.ThrowExceptionEveryEvenSecondClock;

import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяться во время выполнения.
       4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
     */

    public static void main(String[] args) {
        var processors = List.of(new ThrowExceptionEveryEvenSecondClock(),
                new SwapFieldsProcessor());

        var complexProcessor = new ComplexProcessor(processors, Throwable::printStackTrace);
        var storage = new MemoryStorage();
        var historyListener = new HistoryListener(storage);
        complexProcessor.addListener(historyListener);

        ObjectForMessage field13 = new ObjectForMessage();
        field13.setData(List.of("1", "2", "3"));

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        var result = complexProcessor.handle(message);

        field13.setData(List.of("1", "2", "3", "4"));

        System.out.println("result:" + result);
        System.out.println("History: " + storage.getElements());

        complexProcessor.removeListener(historyListener);
    }
}
