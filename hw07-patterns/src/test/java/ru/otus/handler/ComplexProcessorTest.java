package ru.otus.handler;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.listener.homework.HistoryElement;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.listener.homework.MemoryStorage;
import ru.otus.listener.homework.Storage;
import ru.otus.model.Message;
import ru.otus.listener.Listener;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.SwapFieldsProcessor;
import ru.otus.processor.homework.ThrowExceptionEveryEvenSecondClock;
import ru.otus.processor.homework.ThrowExceptionEveryEvenSecondTimeHelper;

import java.time.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ComplexProcessorTest {

    @Test
    @DisplayName("Тестируем вызовы процессоров")
    void handleProcessorsTest() {
        //given
        var message = new Message.Builder(1L).field7("field7").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenReturn(message);

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
        });

        //when
        var result = complexProcessor.handle(message);

        //then
        verify(processor1).process(message);
        verify(processor2).process(message);
        assertThat(result).isEqualTo(message);
    }

    @Test
    @DisplayName("Тестируем обработку исключения")
    void handleExceptionTest() {
        //given
        var message = new Message.Builder(1L).field8("field8").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenThrow(new RuntimeException("Test Exception"));

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            throw new TestException(ex.getMessage());
        });

        //when
        assertThatExceptionOfType(TestException.class).isThrownBy(() -> complexProcessor.handle(message));

        //then
        verify(processor1, times(1)).process(message);
        verify(processor2, never()).process(message);
    }

    @Test
    @DisplayName("Тестируем уведомления")
    void notifyTest() {
        //given
        var message = new Message.Builder(1L).field9("field9").build();

        var listener = mock(Listener.class);

        var complexProcessor = new ComplexProcessor(new ArrayList<>(), (ex) -> {
        });

        complexProcessor.addListener(listener);

        //when
        complexProcessor.handle(message);
        complexProcessor.removeListener(listener);
        complexProcessor.handle(message);

        //then
        verify(listener, times(1)).onUpdated(message, message);
    }

    @Test
    @DisplayName("Меняет местами значения field11 и field12")
    void shouldSwapFields() {
        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .build();

        var complexProcessor = new ComplexProcessor(List.of(new SwapFieldsProcessor()), ex -> {
        });

        Message afterHandle = complexProcessor.handle(message);
        assertThat(afterHandle.getField11()).isEqualTo(message.getField12());
        assertThat(afterHandle.getField12()).isEqualTo(message.getField11());
    }

    @Test
    @DisplayName("Выбрасывает исключение, если секунда на момент запуска, четная (через Clock)")
    void shouldThrowExceptionIfCurrentSecondIsEvenClock() {
        var message = new Message.Builder(1L).build();

        var oddClock = Clock.fixed(
                Instant.parse("2021-03-06T12:00:03.000Z"),
                ZoneId.systemDefault()
        );
        var complexProcessorOdd = new ComplexProcessor(List.of(new ThrowExceptionEveryEvenSecondClock(oddClock)), (ex) -> {
            throw new TestException(ex.getMessage());
        });

        assertThatCode(() -> complexProcessorOdd.handle(message)).doesNotThrowAnyException();

        var evenClock = Clock.fixed(
                Instant.parse("2021-03-06T12:00:02.000Z"),
                ZoneId.systemDefault()
        );
        var complexProcessorEven = new ComplexProcessor(List.of(new ThrowExceptionEveryEvenSecondClock(evenClock)), (ex) -> {
            throw new TestException(ex.getMessage());
        });

        assertThatThrownBy(() -> complexProcessorEven.handle(message)).hasMessage("Current second is: 2");
    }

    @Test
    @DisplayName("Выбрасывает исключение, если секунда на момент запуска, четная (через Helper)")
    void shouldThrowExceptionIfCurrentSecondIsEvenHelper() {
        var message = new Message.Builder(1L).build();

        var complexProcessorOdd = new ComplexProcessor(List.of(new ThrowExceptionEveryEvenSecondTimeHelper(() -> 3)), (ex) -> {
            throw new TestException(ex.getMessage());
        });

        assertThatCode(() -> complexProcessorOdd.handle(message)).doesNotThrowAnyException();

        var complexProcessorEven = new ComplexProcessor(List.of(new ThrowExceptionEveryEvenSecondTimeHelper(() -> 2)), (ex) -> {
            throw new TestException(ex.getMessage());
        });

        assertThatThrownBy(() -> complexProcessorEven.handle(message)).hasMessage("Current second is: 2");
    }

    @Test
    @DisplayName("История")
    void shouldSaveHistory() {
        var messageNew = new Message.Builder(1L).build();

        ObjectForMessage field13 = new ObjectForMessage();
        field13.setData(List.of("1", "2", "3"));

        var messageOld = new Message.Builder(1L).field13(field13).build();

        Storage<HistoryElement> storage = new MemoryStorage();
        var listener = new HistoryListener(storage);

        listener.onUpdated(messageOld, messageNew);

        field13.setData(List.of("1", "2", "3", "4"));

        assertThat(storage.getElements()).hasSize(1);

        HistoryElement first = storage.getElements().get(0);
        assertThat(first.getOldMessage().getField13().getData()).containsOnly("1", "2", "3");
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }
}