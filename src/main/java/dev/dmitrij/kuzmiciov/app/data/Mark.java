package dev.dmitrij.kuzmiciov.app.data;

import org.jetbrains.annotations.Nullable;

public class Mark {
    public static final Mark ABSENT = new Mark(true);
    public static final Mark PRESENT = new Mark(false);
    
    private static int minMark = 1, maxMark = 10;
    
    protected int value;
    
    /**
     *
     * @param value a number from the range
     */
    public Mark(int value) {
        if(value < 1 || value > 10)
            throw new IllegalArgumentException("Mark has to be within the range of [" + minMark + ";" + maxMark + "]");
        this.value = value;
    }
    
    public Mark(boolean absent) {
        value = absent ? -1 : 0;
    }
    
    public static int getMinMark() {
        return minMark;
    }
    
    public static int getMaxMark() {
        return maxMark;
    }
    
    public static void setMinMark(int minMark) {
        if(minMark < 1)
            throw new IllegalArgumentException("Min mark cannot be less than 1");
        Mark.minMark = minMark;
    }
    
    public static void setMaxMark(int maxMark) {
        if(maxMark < 1)
            throw new IllegalArgumentException("Max mark cannot be less than 1");
        Mark.maxMark = maxMark;
    }
    
    /**
     *
     * @return mark value if it has been set, null if it hasn't
     */
    public @Nullable Integer getMark() {
        if(value < 1)
            return null;
        return value;
    }
    
    public boolean isAbsent() {
        return value == -1;
    }
    
    /**
     *
     * @return a number if the mark has been set,
     * 'Absent' if this object was created by {@link #Mark(boolean)} constructor,
     * empty String if nothing was set as a mark
     */
    @Override
    public String toString() {
        if(value > 0)
            return String.valueOf(value);
        else if(value < 0)
            return "Absent";
        else
            return "";
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!Mark.class.isAssignableFrom(obj.getClass()))
            return false;
        
        var other = (Mark)obj;
        return value == other.value;
    }
}
