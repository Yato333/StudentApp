package dev.dmitrij.kuzmiciov.app.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;

/**
 * This class is used for controlling some JavaFX {@link Node}.
 * When you create an FXML file for a certain Node,
 * you should specify a sub-class of this class in a fx:controller property.
 * <p/>
 * <u><b>Warning!</b></u> An object of this class should not be created manually as it can lead to unexpected behaviour.
 * @see javafx.fxml.FXMLLoader
 * @implSpec default constructor - required by FXMLLoader
 */
public abstract class Controller {
    /**
     * This method is called automatically by the {@link javafx.fxml.FXMLLoader FXMLLoader} class to
     * initialize all the nodes captured by it.
     * <p/>
     * <u><b>Warning!</b></u> Do not call this method manually if this object was created manually because some of its
     * fields might be not initialized and {@link NullPointerException} might be thrown.
     * @throws InitError if something went wrong
     * @implNote In this method you should finish stylizing all of the nodes
     * and prepare the {@link javafx.scene.Node Node} that this object is controlling for showing.
     */
    @FXML @SuppressWarnings("unused")
    protected abstract void initialize() throws InitError;

    public static class InitError extends Exception {
        public InitError(Throwable cause) {
            super("Scene root failed to initialize", cause);
        }
    }
}
