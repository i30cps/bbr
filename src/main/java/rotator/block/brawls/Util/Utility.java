package rotator.block.brawls.Util;

import java.util.Comparator;

public class Utility {
	public static <T extends Comparable<T>> T min(T a, T b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static <T> T min(T a, T b, Comparator<T> comparator) {
        return comparator.compare(a, b) <= 0 ? a : b;
    }

    public static <T> T max(T a, T b, Comparator<T> comparator) {
        return comparator.compare(a, b) >= 0 ? a : b;
    }

    public static int abs(int x) {
        if (x < 0) return -x;
        return x;
    }

    public static double abs(double x) {
        if (x < 0) return -x;
        return x;
    }
}

