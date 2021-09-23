package com.longer.es.dao;

import com.longer.es.pojo.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductDao extends ElasticsearchRepository<Product, Long> {

}
