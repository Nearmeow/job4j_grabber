package ru.job4j.grabber;

import ru.job4j.grabber.html.SqlRuParse;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Grabber {
    public static void main(String[] args) {
        SqlRuParse sqlRuParse = new SqlRuParse(new SqlRuDateTimeParser());
        List<Post> posts = sqlRuParse.list("https://www.sql.ru/forum/job-offers/");
        Properties properties = readProperties();

        try (PsqlStore store = new PsqlStore(properties)) {
            for (Post post : posts) {
                store.save(post);
            }
            List<Post> postList = store.getAll();
            postList.forEach(System.out::println);
            System.out.println("***************");
            System.out.println(store.findById(130));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties readProperties() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/rabbit.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
