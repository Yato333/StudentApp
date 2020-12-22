package dev.dmitrij.kuzmiciov.app.util;

import dev.dmitrij.kuzmiciov.app.util.copy.Copier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public final class AppCollections {
    private AppCollections() {}

    public static <E> List<E> deepCopy(List<E> list, Copier<E> copier) {
        var result = new ArrayList<E>();

        for (var el : list) {
            result.add(copier.copy(el));
        }

        return result;
    }

    public static <E> List<E> deepCopyAndModify(List<E> list, Copier<E> copier, UnaryOperator<E> function) {
        var result = new ArrayList<E>();
        for (var el : list) {
            result.add(function.apply(copier.copy(el)));
        }

        return result;
    }
}
