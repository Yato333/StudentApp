package dev.dmitrij.kuzmiciov.app.util;

public final class AppMath implements Utility {
    private AppMath() {}

    public static float round(float n, int precision) {
        float multiplier = (float) Math.pow(10, precision);
        return Math.round(n * multiplier) / multiplier;
    }

    public static double round(double n, int precision) {
        double multiplier = Math.pow(10, precision);
        return Math.round(n * multiplier) / multiplier;
    }
}
