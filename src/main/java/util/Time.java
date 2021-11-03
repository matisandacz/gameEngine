package util;

public class Time {

    public static float getTime() {
        return (float)(System.nanoTime() * 1E-9);
    }
}
