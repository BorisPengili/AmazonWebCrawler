package webcrawler.amazonwebcrawler.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import webcrawler.amazonwebcrawler.models.BaseProductModel;

import java.io.IOException;
import java.util.List;

/**
 * @author : Boris Pengili
 **/
public interface CsvWriterService {

    /**
     * From baseproduct list to csv by using library {@link com.opencsv.bean.StatefulBeanToCsvBuilder}
     *
     * @param list the list of the base product found from amazon best seller list
     * @throws IOException
     * @throws CsvRequiredFieldEmptyException
     * @throws CsvDataTypeMismatchException
     */
    void writeListToCsv(List<BaseProductModel> list) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;
}
