package dev.dmitrij.kuzmiciov.app.util.copy;

import javafx.scene.shape.Rectangle;

public interface Copier<E> {
    /**
     *
     * @param obj - an object to copy
     * @return a deep copy of an object
     */
    E copy(E obj);

    class RectangleCopier implements Copier<Rectangle> {
        @Override
        public Rectangle copy(Rectangle obj) {
            return new Rectangle(obj.getWidth(), obj.getHeight(), obj.getFill());
        }
    }
}
