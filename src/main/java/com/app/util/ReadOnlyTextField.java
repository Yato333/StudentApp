package com.app.util;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class ReadOnlyTextField extends TextField {
    private static String EDITABLE_STYLE;
    private static final String READ_ONLY_STYLE = "-fx-background-color: transparent; -fx-background-insets: 0px";


    private BooleanProperty readOnly = new SimpleBooleanProperty(this, "readOnly", true);

    public boolean isReadOnly() {
        return readOnly.get();
    }

    public BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly.set(readOnly);
        if(readOnly) {
            setEditable(false);
            setStyle(READ_ONLY_STYLE);
        } else {
            setEditable(true);
            setStyle(EDITABLE_STYLE);
        }
    }

    public void switchReadOnly() {
        setReadOnly(!isReadOnly());
    }


    public static final int UNLIMITED_LENGTH = -1;

    private final IntegerProperty maxLength = new SimpleIntegerProperty(this, "maxLength", UNLIMITED_LENGTH);

    private ChangeListener<String> textLengthListener;

    public int getMaxLength() {
        return maxLength.get();
    }

    public IntegerProperty maxLengthProperty() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        if (getMaxLength() != maxLength) {
            if(maxLength == UNLIMITED_LENGTH && textLengthListener != null)
                textProperty().removeListener(textLengthListener);
            else if (maxLength >= 0) {
                textLengthListener = (observable, oldValue, newValue) -> {
                    if (newValue != null && newValue.length() > maxLength)
                        deleteNextChar();
                };
                textProperty().addListener(textLengthListener);
            } else throw new IllegalArgumentException("Max length can be UNLIMITED_LENGTH or greater or equal to 0");
            this.maxLength.set(maxLength);
        }
    }


    public ReadOnlyTextField(boolean commitOnEnter) {
        if(EDITABLE_STYLE == null)
            EDITABLE_STYLE = getStyle();
        setReadOnly(true);
        if(commitOnEnter) {
            setOnKeyPressed(keyEvent -> {
                if (isFocused() && keyEvent.getCode() == KeyCode.ENTER) {
                    setFocused(false);
                    setReadOnly(true);
                    keyEvent.consume();
                }
            });
        }
    }
    public ReadOnlyTextField() {
        this(true);
    }

    public ReadOnlyTextField(String text) {
        this();
        setText(text);
    }

    public ReadOnlyTextField(String text, boolean commitOnEnter, int maxLength) {
        this(commitOnEnter);
        setText(text);
        setMaxLength(maxLength);
    }

    public void removeFocus() {
        if(getParent() != null)
            getParent().requestFocus();
    }

    public void switchFocus() {
        if(isFocused()) {
            removeFocus();
        } else {
            requestFocus();
        }
    }
}
