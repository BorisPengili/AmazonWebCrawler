package webcrawler.amazonwebcrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//Crawl amazon best seller list
//The main class that start is HtmlCrawlerController
public class AmazonWebcrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmazonWebcrawlerApplication.class, args);
	}

}
