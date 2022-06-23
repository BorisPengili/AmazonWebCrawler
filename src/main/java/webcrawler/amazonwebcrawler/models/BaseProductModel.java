package webcrawler.amazonwebcrawler.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

/**
 * @author : Boris Pengili
 **/
@Getter
@Setter
@ToString
public class BaseProductModel {

    private String link;
    private String asin;
    private String title;
    private String price;
    private String customerReviewNumber;
    private String customerReviewStars;
    private String errorMessage;
    int page;

    public BaseProductModel(String link, String asin, String title, String price, String customerReviewNumber, String customerReviewStars) {
        this.link = link;
        this.asin = asin;
        this.title = title;
        this.price = price;
        this.customerReviewNumber = getFirstWord(customerReviewNumber);
        this.customerReviewStars = getFirstWord(customerReviewStars);
        checkForError();
    }

    private void checkForError() {
        this.errorMessage = "";
        if (!StringUtils.hasText(this.asin)) {
            this.errorMessage += "Asin can not be found; ";
        }

        if (!StringUtils.hasText(this.title)) {
            this.errorMessage += "Title can not be found; ";
        }

        if (!StringUtils.hasText(this.price)) {
            this.errorMessage += "Price can not be found; ";
        }

        if (!StringUtils.hasText(this.customerReviewNumber)) {
            this.errorMessage += "Customer Review Number can not be found; ";
        }

        if (!StringUtils.hasText(this.customerReviewStars)) {
            this.errorMessage += "Customer Review Stars can not be found; ";
        }
    }

    private String getFirstWord(String str) {
        return str.contains(" ") ? str.split(" ")[0] : str;
    }

}
