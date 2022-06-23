package webcrawler.amazonwebcrawler.models.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author : Boris Pengili
 **/
@Document(collection = "product")
@Data
@Builder
public class ProductEntity {

    @Id
    private String id;

    /**
     * The link where you can find the product
     */
    private String link;

    /**
     * The id of the product
     */
    private String asin;

    /**
     * The title of product
     */
    private String title;

    /**
     * The price in Euro of product
     */
    private String price;

    /**
     * The number of review by customers
     */
    private Integer customerReviewNumber;

    /**
     * The review of the customer by 1 to 5
     */
    private Float customerReviewStars;

    /**
     * The error message if one of the field is missing
     */
    private String errorMessage;

    /**
     * The image of the product
     */
    private byte[] imageAttachment;

    /**
     * The brand of the product
     */
    private String brand;

}
