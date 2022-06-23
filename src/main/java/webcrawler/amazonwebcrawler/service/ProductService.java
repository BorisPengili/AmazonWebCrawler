package webcrawler.amazonwebcrawler.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import webcrawler.amazonwebcrawler.models.BaseProductModel;
import webcrawler.amazonwebcrawler.models.ProductModel;

import java.io.IOException;
import java.util.List;

/**
 * @author : Boris Pengili
 **/
public interface ProductService {

    /**
     * Map first ProductModel {@link  webcrawler.amazonwebcrawler.models.ProductModel} to {@link webcrawler.amazonwebcrawler.models.entity.ProductEntity}
     * Save all in db
     *
     * @param productModelList the list that will be saved
     */
    void saveAll(List<ProductModel> productModelList);

    /**
     * Create the base product
     *
     * @param productPage product page html
     * @param mainPageElement it is used only for the price because in the page of the product it was difficult to find the price
     * @return {@link webcrawler.amazonwebcrawler.models.ProductModel}
     */
    ProductModel createBaseProduct(Document productPage, Element mainPageElement);

    /**
     * Create the product that will be saved in db
     * Check if there is any error for brand and images. Add to message
     *
     * @param productPage product page html
     * @param productModel productModel
     * @return {@link webcrawler.amazonwebcrawler.models.ProductModel}
     * @throws IOException
     */
    ProductModel createFullProduct(Document productPage, BaseProductModel productModel) throws IOException;
}
