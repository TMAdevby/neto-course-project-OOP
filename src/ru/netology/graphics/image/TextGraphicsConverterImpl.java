package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {

    private int maxWidth = Integer.MAX_VALUE;
    private int maxHeight = Integer.MAX_VALUE;
    private double maxRatio = Double.MAX_VALUE;
    private TextColorSchema schema = new DefaultColorSchema(); // Схема по умолчанию

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        // Скачиваем изображение
        BufferedImage img = ImageIO.read(new URL(url));

        int width = img.getWidth();
        int height = img.getHeight();
        double ratio = (double) width / height;

        // Проверка соотношения сторон
        if (ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        // Вычисляем новые размеры с сохранением пропорций
        int newWidth = width;
        int newHeight = height;

        if (newWidth > maxWidth) {
            double scale = (double) maxWidth / newWidth;
            newWidth = maxWidth;
            newHeight = (int) (newHeight * scale);
        }

        if (newHeight > maxHeight) {
            double scale = (double) maxHeight / newHeight;
            newHeight = maxHeight;
            newWidth = (int) (newWidth * scale);
        }

        // Масштабируем изображение
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        // Создаём чёрно-белое изображение
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        graphics.dispose();

        // Получаем растровое представление для чтения пикселей
        WritableRaster bwRaster = bwImg.getRaster();
        int[] pixel = new int[3]; // Переиспользуем массив для оптимизации

        // Двумерный массив символов
        char[][] chars = new char[newHeight][newWidth];

        // Проходим по каждому пикселю
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, pixel)[0]; // 0-255
                char c = schema.convert(color);
                chars[h][w] = c;
            }
        }

        // Собираем текст, удваивая каждый символ по горизонтали (чтобы не было слишком узко)
        StringBuilder sb = new StringBuilder();
        for (char[] row : chars) {
            for (char c : row) {
                sb.append(c).append(c); // Два символа на пиксель
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
