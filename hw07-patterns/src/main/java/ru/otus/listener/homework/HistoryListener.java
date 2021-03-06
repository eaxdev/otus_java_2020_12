package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

public class HistoryListener implements Listener {

    private final Storage<?> storage;

    public HistoryListener(Storage<?> storage) {
        this.storage = storage;
    }

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        storage.save(oldMsg.deepCopy(), newMsg.deepCopy());
    }
}
