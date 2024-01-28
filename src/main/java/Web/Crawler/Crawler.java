package Web.Crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Класс Crawler представляет собой простой веб-сканер для прохода по страницам сайта.
 */
public class Crawler {
    /**
     * Точка входа в программу. Запускает сканирование с указанного URL.
     *
     * @param args Аргументы командной строки (не используются в данной программе).
     */
    public static void main(String[] args) {
        String url = "https://en.wikipedia.org/";
        crawl(1,url,new ArrayList<String>());
    }
    /**
     * Рекурсивный метод сканирования страницы сайта.
     *
     * @param level        Текущий уровень глубины сканирования.
     * @param url          URL страницы для сканирования.
     * @param visitedUrls  Список посещенных URL для избежания повторного сканирования.
     */
    private static void crawl(int level, String url, ArrayList<String> visitedUrls){
        if (level <= 5) {
            Document doc = request(url,visitedUrls);

            if (doc !=null){
                for (Element link : doc.select("a[href]")){
                    String hrefValue = link.absUrl("href");
                    if (!visitedUrls.contains(hrefValue)){
                        crawl(++level,hrefValue,visitedUrls);
                    }
                }
            }
        }
    }
    /**
     * Метод для выполнения HTTP-запроса и получения HTML-документа.
     *
     * @param url     URL страницы для запроса.
     * @param visited Список посещенных URL для избежания повторного запроса.
     * @return HTML-документ или null в случае ошибки.
     */
    private static Document request(String url, ArrayList<String> visited){
        try {
            Connection con = Jsoup.connect(url);
            Document doc = con.get();

            if (con.response().statusCode() == 200) {
                System.out.println("Link: " + url);
                System.out.println("Title: "+ doc.title());
                visited.add(url);

                return doc;
            }
        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING,"Something bad happened!");
        }

        return null;
    }
}
