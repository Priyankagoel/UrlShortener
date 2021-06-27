package com.germinait.assignment.urlshortener.common;

import java.util.ArrayList;
import java.util.List;

public class IntToCharConverter {
    public static final IntToCharConverter intToCharConverter = new IntToCharConverter();

    public static List<Character> intToCharMap;
    private IntToCharConverter() {
        generateIntToCharMap();
    }

    private void generateIntToCharMap() {

        intToCharMap = new ArrayList<>();
        for (int i = 0; i < 62; i++) {
            int j = 0;
            if (i < 10) {
                j = i + 48;
            } else if (i > 9 && i <= 35) {
                j = i + 55;
            } else {
                j = i + 61;
            }
            intToCharMap.add((char) j);
        }
    }
}
