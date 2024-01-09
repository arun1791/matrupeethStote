package com.matrupeeth.store.services.Impl;

import com.matrupeeth.store.dtos.CategoryDto;
import com.matrupeeth.store.dtos.PageableResponse;
import com.matrupeeth.store.dtos.UserDto;
import com.matrupeeth.store.entities.Category;
import com.matrupeeth.store.exception.ResourcesNotFoundException;
import com.matrupeeth.store.helper.Helper;
import com.matrupeeth.store.repositories.CategoryRepository;
import com.matrupeeth.store.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        //create a new categoryid randome number
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = modelMapper.map(categoryDto, Category.class);
        Category saveCategory = categoryRepository.save(category);
        return modelMapper.map(saveCategory,CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourcesNotFoundException("Category id not found exception !!"));
    //update category
        category.setTitle(categoryDto.getTitle());
        category.setDescription(categoryDto.getDescription());
        category.setCoverImage(categoryDto.getCoverImage());
        Category updateCategory = categoryRepository.save(category);
        return modelMapper.map(updateCategory,CategoryDto.class);
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourcesNotFoundException("Category id not found exception !!"));
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize);

        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Page<Category> page = categoryRepository.findAll(pageable);
        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }

    @Override
    public CategoryDto get(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourcesNotFoundException("Category id not found exception !!"));

        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        List<Category> categories = categoryRepository.findByTitleContaining(keyword);
        List<CategoryDto> dtoList = categories.stream().map(category -> modelMapper.map(categories, CategoryDto.class)).collect(Collectors.toList());
        return dtoList;

    }
}
