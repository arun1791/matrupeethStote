package com.matrupeeth.store.services;

import com.matrupeeth.store.dtos.CategoryDto;
import com.matrupeeth.store.dtos.PageableResponse;
import com.matrupeeth.store.dtos.UserDto;

import java.util.List;

public interface CategoryService {
    //create
    CategoryDto create(CategoryDto categoryDto);

    //update
    CategoryDto update(CategoryDto categoryDto,String categoryId);
    //delete
    void delete(String categoryId);

    PageableResponse<CategoryDto>getAll(int pageNumber,int pageSize,String sortBy,String sortDir);

    CategoryDto get(String categoryId);
    //getall

    List<CategoryDto> searchCategory(String keyword);
}
