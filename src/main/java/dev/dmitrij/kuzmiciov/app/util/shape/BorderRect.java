package dev.dmitrij.kuzmiciov.app.util.shape;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class BorderRect extends Rectangle implements Cloneable {
    public static final BorderRect
        NAME = new BorderRect(100, 30),
        NUMBER = new BorderRect(30, 30);
    
    public BorderRect(double width, double height, Paint borderColor) {
        super(width, height, Color.TRANSPARENT);
        setStroke(borderColor);
    }

    public BorderRect(double width, double height) {
        this(width, height, Color.BLACK);
    }
    
    public BorderRect(BorderRect other) {
        this(other.getWidth(), other.getHeight(), other.getStroke());
    }
    
    @Override
    public BorderRect clone() {
        try {
            super.clone();
        } catch(CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        return new BorderRect(this);
    }
}
