package ru.job4j.grabber.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        try {
            for (int i = 1; i < 6; i++) {
                Document doc = Jsoup.connect(link + i).get();
                Elements row = doc.select(".postslisttopic");
                for (Element td : row) {
                    Element href = td.child(0);
                    String title = href.text();
                    if (title.toLowerCase().contains("java") && !title.toLowerCase().contains("javascript")) {
                        Post post = detail(href.attr("href"));
                        post.setTitle(title);
                        posts.add(post);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        Post post = new Post();
        try {
            Document doc = Jsoup.connect(link).get();
            post.setDescription(getDescriptionFromDoc(doc));
            post.setCreated(getCreatedFromDoc(doc));
            post.setLink(link);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return post;
    }

    private String getDescriptionFromDoc(Document doc) {
        Elements bodyElements = doc.select(".msgBody");
        return bodyElements.get(1).text();
    }

    private LocalDateTime getCreatedFromDoc(Document doc) throws ParseException {
        Elements footerElements = doc.select(".msgFooter");
        String[] footerStrings = footerElements.get(0).text().split("\\[");
        return dateTimeParser.parse(footerStrings[0].trim());
    }
}
