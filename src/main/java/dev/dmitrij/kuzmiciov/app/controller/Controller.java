package dev.dmitrij.kuzmiciov.app.controller;

public abstract class Controller {
    protected abstract void initialize() throws InitError;

    public class InitError extends Exception {
        public InitError(Throwable cause) {
            super("Scene root failed to initialize", cause);
        }
    }
}
