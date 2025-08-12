package ru.netology.graphics;

import ru.netology.graphics.image.DefaultColorSchema;
import ru.netology.graphics.image.TextGraphicsConverter;
import ru.netology.graphics.image.TextGraphicsConverterImpl;
import ru.netology.graphics.server.GServer;

import java.io.File;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        TextGraphicsConverter converter = new TextGraphicsConverterImpl();

        // Настройка конвертера
        converter.setMaxWidth(100);
        converter.setMaxHeight(100);
        converter.setMaxRatio(4.0);
        converter.setTextColorSchema(new DefaultColorSchema());

        // Тест через сервер
//        GServer server = new GServer(converter);
//        server.start();


        // Или то же, но с выводом на экран:
        //String url = "https://raw.githubusercontent.com/netology-code/java-diplom/main/pics/simple-test.png";
        String url = "https://cdn.culture.ru/images/b22b8a3b-e177-59e3-9417-1a9b53abee4c";
        String imgTxt = converter.convert(url);
        System.out.println(imgTxt);
    }
}
