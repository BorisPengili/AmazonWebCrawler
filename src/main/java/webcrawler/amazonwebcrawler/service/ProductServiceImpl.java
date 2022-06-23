package webcrawler.amazonwebcrawler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import webcrawler.amazonwebcrawler.models.BaseProductModel;
import webcrawler.amazonwebcrawler.models.ProductModel;
import webcrawler.amazonwebcrawler.models.entity.ProductEntity;
import webcrawler.amazonwebcrawler.repository.ProductRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : Boris Pengili
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void saveAll(List<ProductModel> productModelList) {
        try {
            List<ProductEntity> list = new ArrayList<>();
            for (ProductModel productModel : productModelList) {
                var productEntity = ProductEntity.builder()
                        .asin(productModel.getAsin())
                        .link(productModel.getLink())
                        .price(productModel.getPrice())
                        .title(productModel.getTitle())
                        .customerReviewNumber(castCustomerReviewNumber(productModel))
                        .customerReviewStars(castCustomerReviewStars(productModel))
                        .brand(productModel.getBrand())
                        .imageAttachment(productModel.getImageByte())
                        .errorMessage(checkErrorForBrandAndForImage(productModel))
                        .build();
                list.add(productEntity);
            }
            productRepository.saveAll(list);
        } catch (Exception ex) {
            log.error("Something went wrong when trying to save: {} ", productModelList);
            log.error("Error message: {}", ex.getMessage());
        }
    }

    @Override
    public ProductModel createBaseProduct(Document productPage, Element element) {
        try {
            //asin
            Element asinElement = productPage.getElementById("ASIN");
            String asin = !Objects.isNull(asinElement) ? asinElement.attr("value") : "";
            //title
            var titleElement = productPage.getElementById("productTitle");
            String title = !Objects.isNull(titleElement) ? titleElement.text() : "";
            //price
            String price = getPrice(element);
            //customer number
            var customerReviewNumberElement = productPage.getElementById("acrCustomerReviewLink");
            String customerReviewNumber = !Objects.isNull(customerReviewNumberElement) ? customerReviewNumberElement.text() : "";
            //customer Review Element
            var customerReviewElement = productPage.getElementsByClass("reviewCountTextLinkedHistogram noUnderline").first();
            String customerReview = !Objects.isNull(customerReviewElement) ? customerReviewElement.text() : "";
            return new ProductModel(null, asin, title, price, customerReviewNumber, customerReview);
        } catch (Exception ex) {
            log.warn("Something went wrong when finding base image {} {}", productPage, element);
            log.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public ProductModel createFullProduct(Document productPage, BaseProductModel baseProductModel) throws IOException {

        try {
            var productModel = (ProductModel) baseProductModel;
            Element imageElement = productPage.getElementById("imgTagWrapperId");
            String imageLink = !Objects.isNull(imageElement) ? imageElement.childNode(1).attr("src") : "";
            Connection.Response resultImageResponse = Jsoup.connect(imageLink).ignoreContentType(true).execute();
            byte[] imageInBytes = resultImageResponse.bodyAsBytes();
            //brand
            Elements brandElement = productPage.getElementsByClass("a-spacing-small po-brand");
            String brand = brandElement.select("td:nth-child(2n)").text();
            productModel.setBrand(brand);
            productModel.setImageByte(imageInBytes);
            return productModel;
        } catch (Exception ex) {
            log.warn("Something went wrong when finding image and brandElement {} {}", baseProductModel, productPage);
            log.error(ex.getMessage());
        }
        return (ProductModel) baseProductModel;
    }


    //    The price can be in 2 format like 1) 14€ - 20€ or just 2) 20€
//    If it is in the first format find by class "_cDEzb_p13n-sc-price_3mJ9Z"
//    If it is in the second format find by class "p13n-sc-price"
    private String getPrice(Element element) {
        var price = "";

        var priceElements = element.getElementsByClass("_cDEzb_p13n-sc-price_3mJ9Z");
        for (Element priceElement : priceElements) {
            if (StringUtils.hasText(price)) {
                price += "-";
            }
            price += !Objects.isNull(priceElement) ? priceElement.text() : "";
        }

        if (priceElements.isEmpty()) {
            var priceElement = element.getElementsByClass("p13n-sc-price").first();
            price = !Objects.isNull(priceElement) ? priceElement.text() : "";
        }
        return price;
    }

    // The review number is with coma like 78,999 or 78.99. Remove coma than cast to Integer
    private Integer castCustomerReviewNumber(ProductModel productModel) {
        String customerReviewNumber = productModel.getCustomerReviewNumber();
        if (!StringUtils.hasText(customerReviewNumber)) {
            return null;
        }
        try {
            customerReviewNumber = customerReviewNumber.replace(",", "");
            customerReviewNumber = customerReviewNumber.replace(".", "");
            return Integer.valueOf(customerReviewNumber);
        } catch (Exception ex) {
            log.error("Something went wrong in casting customer review number{}", productModel);
            log.error("Error message: {}", ex.getMessage());
            return null;
        }
    }

    //cast from String to Float. Sometimes the number is like 76,444. So it is needed to be replace with point -> 76.444
    private Float castCustomerReviewStars(ProductModel productModel) {
        String customerReviewNumberStars = productModel.getCustomerReviewStars();
        if (!StringUtils.hasText(customerReviewNumberStars)) {
            return null;
        }
        try {
            customerReviewNumberStars = customerReviewNumberStars.replace(",", ".");

            return Float.valueOf(customerReviewNumberStars);
        } catch (Exception ex) {
            log.error("Something went wrong in casting customer review stars {}", productModel);
            log.error("Error message: {}", ex.getMessage());
            return null;
        }
    }

    private String checkErrorForBrandAndForImage(ProductModel productModel) {
        String errorMessage = productModel.getErrorMessage();
        if (!StringUtils.hasText(productModel.getBrand())) {
            errorMessage += "Brand can not be found: ";
        }

        if (productModel.getImageByte() == null && productModel.getImageByte().length == 0) {
            errorMessage += "Image can not be found: ";
        }

        return errorMessage;
    }

}

