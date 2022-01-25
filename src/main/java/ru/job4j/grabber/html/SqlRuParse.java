package ru.job4j.grabber.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;

public class SqlRuParse {

    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) throws Exception {
        for (int i = 1; i < 6; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element parent = td.parent();
                Element href = td.child(0);
                System.out.println(parent.child(5).text());
                System.out.println(href.attr("href"));
                System.out.println(href.text());
            }
        }
    }

    public Post getPostFromLink(String link) throws IOException, ParseException {
        Document doc = Jsoup.connect(link).get();
        Post post = new Post();
        post.setDescription(getDescriptionFromDoc(doc));
        post.setCreated(getCreatedFromDoc(doc));
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
