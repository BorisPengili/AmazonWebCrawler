package webcrawler.amazonwebcrawler.service;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.stereotype.Service;
import webcrawler.amazonwebcrawler.models.BaseProductModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author : Boris Pengili
 **/
@Service
public class CsvWriterServiceImpl implements CsvWriterService {

    @Override
    public void writeListToCsv(List<BaseProductModel> productModelList) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        var path = "output.csv";
        var idea = new File(path);
        var writer = new FileWriter(idea);
        StatefulBeanToCsv<BaseProductModel> beanWriter = new StatefulBeanToCsvBuilder<BaseProductModel>(writer).build();
        // Write list to StatefulBeanToCsv object
        beanWriter.write(productModelList);
        // closing the writer object
        writer.close();
    }

}
