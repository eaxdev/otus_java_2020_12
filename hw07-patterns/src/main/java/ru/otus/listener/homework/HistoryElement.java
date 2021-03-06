package ru.otus.listener.homework;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public class HistoryElement {

    private final Message oldMessage;

    private final Message newMessage;

    private final LocalDateTime createdAt;

    private HistoryElement(Message oldMessage, Message newMessage, LocalDateTime createdAt) {
        this.oldMessage = oldMessage;
        this.newMessage = newMessage;
        this.createdAt = createdAt;
    }

    public static HistoryElement of(Message oldMessage, Message newMessage) {
        return new HistoryElement(oldMessage, newMessage, LocalDateTime.now());
    }

    public Message getOldMessage() {
        return oldMessage;
    }

    public Message getNewMessage() {
        return newMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "HistoryElement{" +
                "oldMessage=" + oldMessage +
                ", newMessage=" + newMessage +
                ", createdAt=" + createdAt +
                '}';
    }
}
