package ru.otus.listener.homework;

import ru.otus.model.Message;

import java.util.List;

public interface Storage<T> {

    void save(Message oldMessage, Message newMessage);

    List<T> getElements();

}
