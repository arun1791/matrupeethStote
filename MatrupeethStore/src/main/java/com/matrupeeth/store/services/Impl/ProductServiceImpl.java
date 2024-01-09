package com.matrupeeth.store.services.Impl;

import com.matrupeeth.store.dtos.CategoryDto;
import com.matrupeeth.store.dtos.PageableResponse;
import com.matrupeeth.store.dtos.ProductDto;
import com.matrupeeth.store.entities.Category;
import com.matrupeeth.store.entities.Product;
import com.matrupeeth.store.exception.ResourcesNotFoundException;
import com.matrupeeth.store.helper.Helper;
import com.matrupeeth.store.repositories.CategoryRepository;
import com.matrupeeth.store.repositories.ProductRepository;
import com.matrupeeth.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ProductDto create(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();

        Product product = modelMapper.map(productDto, Product.class);
        product.setProductId(productId);
        //added date
        product.setAddedDate(new Date());
        Product saveProduct = productRepository.save(product);
        return modelMapper.map(saveProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        //fetach the
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourcesNotFoundException("Product not found !1"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isStock());
        product.setProductImageName(productDto.getProductImageName());
        Product updateProduct = productRepository.save(product);
        return modelMapper.map(updateProduct, ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourcesNotFoundException("Product not found !1"));
        productRepository.delete(product);
    }

    @Override
    public ProductDto getSignle(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourcesNotFoundException("Product not found !1"));

        return modelMapper.map(product,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Page<Product> page = productRepository.findAll(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
        return pageableResponse;

    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public PageableResponse<ProductDto> serachByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());
        Page<Product> page = productRepository.findByTitleContaining(subTitle,pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class);
        return pageableResponse;
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourcesNotFoundException("Categorty id  not foun "));
        //featch the category
        String productId = UUID.randomUUID().toString();

        Product product = modelMapper.map(productDto, Product.class);
        product.setProductId(productId);
        //added date
        product.setAddedDate(new Date());
        product.setCategory(category);
        Product saveProduct = productRepository.save(product);
        return modelMapper.map(saveProduct, ProductDto.class);

    }

    @Override
    public ProductDto updateWithCategory(String productId, String categoryId) {
        //product feacth operation
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourcesNotFoundException("product was givn i not gound"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourcesNotFoundException("Categorty id  not foun "));
        product.setCategory(category);
        Product saveProduct = productRepository.save(product);
        return modelMapper.map(saveProduct,ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourcesNotFoundException("Categorty id  not foun "));
        Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending());

        Pageable pageable=   PageRequest.of(pageNumber,pageSize,sort );
        Page<Product> page = productRepository.findByCategory(category,pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }
}
