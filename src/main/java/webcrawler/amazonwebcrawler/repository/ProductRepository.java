package webcrawler.amazonwebcrawler.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import webcrawler.amazonwebcrawler.models.entity.ProductEntity;

/**
 * @author : Boris Pengili
 **/
@Repository
public interface ProductRepository extends MongoRepository<ProductEntity, String> {
}
