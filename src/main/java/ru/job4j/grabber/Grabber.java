package ru.job4j.grabber;

import org.quartz.impl.jdbcjobstore.PostgreSQLDelegate;
import ru.job4j.grabber.html.SqlRuParse;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.util.List;

public class Grabber {
    public static void main(String[] args) {
        SqlRuParse sqlRuParse = new SqlRuParse(new SqlRuDateTimeParser());
        List<Post> posts = sqlRuParse.list("https://www.sql.ru/forum/job-offers/");
        posts.forEach(System.out::println);
    }
}
