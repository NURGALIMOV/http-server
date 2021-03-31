package ru.inurgalimov.http.utils.guava;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Bytes {

    /**
     * from guava library (as it is)
     */
    public int indexOf(byte[] array, byte[] target, int start) {
        if (target.length == 0) {
            return 0;
        }

        outer:
        for (int i = start; i < array.length - target.length + 1; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }
}
