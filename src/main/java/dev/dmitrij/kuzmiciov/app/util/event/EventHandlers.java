package dev.dmitrij.kuzmiciov.app.util.event;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jetbrains.annotations.NotNull;

public final class EventHandlers {
    private EventHandlers() {}

    public static EventHandler<KeyEvent> commitOnEnterHandler(@NotNull Node node) {
        return keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER)
                node.getParent().requestFocus();
        };
    }
}
