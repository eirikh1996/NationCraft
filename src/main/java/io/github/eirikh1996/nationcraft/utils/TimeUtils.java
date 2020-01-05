package io.github.eirikh1996.nationcraft.utils;

public class TimeUtils {
    public static int daysSince(long timestamp) {
        long time = System.currentTimeMillis() - timestamp;
        //Convert to seconds
        time = time / 1000;
        //then minutes
        time = time / 60;
        //then hours
        time = time / 60;
        //then days
        time = time / 24;

        return (int) time;
    }
}
