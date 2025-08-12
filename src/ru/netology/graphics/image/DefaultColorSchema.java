package ru.netology.graphics.image;

public class DefaultColorSchema implements TextColorSchema {
    private final char[] schema = {'#', '$', '@', '%', '*', '+', '-', ' '};

    @Override
    public char convert(int color) {

        int index = color * (schema.length - 1) / 255;
        return schema[index];
    }
}
