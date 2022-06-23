package webcrawler.amazonwebcrawler.controller;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import webcrawler.amazonwebcrawler.models.BaseProductModel;
import webcrawler.amazonwebcrawler.models.ProductModel;
import webcrawler.amazonwebcrawler.service.CsvWriterService;
import webcrawler.amazonwebcrawler.service.ProductService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Boris Pengili
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class HtmlCrawlerController {


    private final CsvWriterService csvWriterService;

    private final ProductService productService;

    /**
     * The host of the amazon
     */
    private static final String HOST_URL = "https://www.amazon.de/";
    /**
     * Replace % with the number that you want
     */
    private static final String MAIN_PAGE = HOST_URL + "-/en/gp/bestsellers/ce-de/ref=zg_bs_pg_%d?ie=UTF8&pg=%d";

    /**
     * Get the necessary information like id,price,title etc {@link webcrawler.amazonwebcrawler.models.ProductModel} by crawling
     * Iterate for each page from page 1 to page 10.
     * For each page, find the link for each product and get the information needed by redirecting that link
     *
     * @throws IOException
     * @throws CsvRequiredFieldEmptyException
     * @throws CsvDataTypeMismatchException
     */
    @EventListener(ApplicationReadyEvent.class)
    public void crawlAmazon() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        List<BaseProductModel> baseProductModelList = new ArrayList<>(); // the list are going to be in csv
        List<ProductModel> productModelList = new ArrayList<>(); // the list that are going to be saved in db
        for (var page = 1; page <= 10; page++) {
            var count = 0; // just to get the count of the object for the comment
            var url = String.format(MAIN_PAGE, page, page);
            Document bestSellerListPage = Jsoup.connect(url).get();
            var elementsByClass = bestSellerListPage.getElementsByClass("zg-grid-general-faceout"); //products in main page are seperated by this class
            if (elementsByClass.isEmpty()) {
                log.warn("There is no element in page: " + page);
            }

            for (Element element : elementsByClass) {
                log.info("Count product number {} for page {}", count++, page);
                Document productPage = null;
                String link = null;
                try {
                    link = HOST_URL + element.getElementsByTag("a").attr("href"); //get the link of the product
                    log.info("Link of the product page: "+link);
                    productPage = Jsoup.connect(link).get();
//                    productPage = Jsoup.connect(link).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36")
//                           .referrer("http://www.google.com").get();
                } catch (Exception ex) {
                    log.warn("The link can not be found {} with link {}", element, link);
                }

                BaseProductModel baseProductModel = productService.createBaseProduct(productPage, element); //create the product that will be in csv
                baseProductModel.setLink(link);
                baseProductModel.setPage(page);
                baseProductModelList.add(baseProductModel);
                productModelList.add(productService.createFullProduct(productPage, baseProductModel));//create the product that will be saved in db
            }
        }

        csvWriterService.writeListToCsv(baseProductModelList);

        productService.saveAll(productModelList);
    }


}


