package webcrawler.amazonwebcrawler.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author : Boris Pengili
 **/

@Getter
@Setter
@ToString(callSuper=true)
public class ProductModel extends BaseProductModel{

    private byte[] imageByte;

    private String brand;

    public ProductModel(String link, String asin, String title, String price, String customerReviewNumber, String customerReviewStars) {
        super(link, asin, title, price, customerReviewNumber, customerReviewStars);
    }
}
