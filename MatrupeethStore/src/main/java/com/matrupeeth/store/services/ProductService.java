package com.matrupeeth.store.services;

import com.matrupeeth.store.dtos.PageableResponse;
import com.matrupeeth.store.dtos.ProductDto;

import java.util.List;

public interface ProductService {

    //craete
    ProductDto create(ProductDto productDto);

    //update
    ProductDto update(ProductDto productDto, String productId);
    //delete
    void delete(String productId);

    //get signle
    ProductDto getSignle(String productId);
    //get All
    PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);
    //get live all
    PageableResponse<ProductDto>getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir);

    //serach
    PageableResponse<ProductDto>serachByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir);

    //other methods
    //create product with category
    ProductDto createWithCategory(ProductDto productDto ,String categoryId);

    //update category of product
    ProductDto updateWithCategory(String productId,String categoryId);

    //get
    PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir);


}

