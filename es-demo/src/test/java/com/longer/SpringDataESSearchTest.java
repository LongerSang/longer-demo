package com.longer;

import com.longer.es.dao.ProductDao;
import com.longer.es.pojo.Product;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class SpringDataESSearchTest {

    @Autowired
    private ProductDao productDao;

    /**
     * term 查询
     * search（termQueryBuilder） 调用搜索方法，参数查询构建器对象
     */
    @Test
    public void termQuery() {
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category", "手机");
        Iterable<Product> products = productDao.search(termQueryBuilder);
        for (Product product : products) {
            System.out.println(product);
        }
    }

    /**
     * term 查询加分页
     */
    @Test
    public void termQueryByPage() {
        int from = 0;
        int size = 5;
        // 设置查询分页
        PageRequest pageRequest = PageRequest.of(from, size);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("category", "手机");
        Iterable<Product> products = productDao.search(termQueryBuilder, pageRequest);
        for (Product product : products) {
            System.out.println(product);
        }
    }

}
