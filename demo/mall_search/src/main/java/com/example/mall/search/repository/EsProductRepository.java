package com.example.mall.search.repository;

import com.example.mall.search.domain.EsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsProductRepository extends ElasticsearchRepository<EsProduct, Long> {
    Page<EsProduct> findByNameOrSubTitleOrKeywords(String name, String subTitle, String keywords, Pageable page);





}
