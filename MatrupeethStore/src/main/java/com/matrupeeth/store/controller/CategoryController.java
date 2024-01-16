package com.matrupeeth.store.controller;

import com.matrupeeth.store.dtos.*;
import com.matrupeeth.store.services.CategoryService;
import com.matrupeeth.store.services.FileService;
import com.matrupeeth.store.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;
    @Autowired
    private ProductService productService;

    @Value("${user.profile.image.path}")
    private String imgaeUploadPath;

    private Logger logger= LoggerFactory.getLogger(UserController.class);

    //craete
    @PreAuthorize("hasRole('ADMIN')")
   @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Validated @RequestBody CategoryDto categoryDto)
    {
        CategoryDto categoryDto1 = categoryService.create(categoryDto);
        return  new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
    }

    //update
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public  ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,@PathVariable String categoryId)
    {
        CategoryDto updateCategory = categoryService.update(categoryDto, categoryId);
        return  new ResponseEntity<>(updateCategory, HttpStatus.OK);


    }

    //get all categories
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public  ResponseEntity<ApiResponseMassege> deleteCategory(@PathVariable String categoryId)
    {
        categoryService.delete(categoryId);
        ApiResponseMassege response = ApiResponseMassege.builder()
                .message("successfully deleted category")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public  ResponseEntity<PageableResponse<CategoryDto>>getAllCategories(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber ,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize ,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy ,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
    )
    {
        PageableResponse<CategoryDto> pageableResponse= categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(pageableResponse, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public  ResponseEntity<CategoryDto>getSingle(@PathVariable String categoryId){
        CategoryDto categoryDto = categoryService.get(categoryId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public  ResponseEntity<List<CategoryDto>>getByuserkeywords(@PathVariable  String keyword)
    {
        return  new ResponseEntity<>(categoryService.searchCategory(keyword),HttpStatus.OK);
    }

    @PostMapping("/image/{categoryId}")
    public  ResponseEntity<ImgaeResponse> uploadUserImage(
            @PathVariable String categoryId,
            @RequestParam("categoryImage") MultipartFile image
    ) throws IOException {
        String imageName = fileService.uploadImage(image, imgaeUploadPath);
        CategoryDto categoryDto = categoryService.get(categoryId);
        categoryDto.setCoverImage(imageName);
        categoryService.update(categoryDto, categoryId);
        ImgaeResponse imgaeResponse=ImgaeResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
        return  new ResponseEntity<>(imgaeResponse,HttpStatus.CREATED);
    }

    @GetMapping("/image/{categoryId}")
    public  void serveUserImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
        CategoryDto categoryDto = categoryService.get(categoryId);
        logger.info("user details user :{}",categoryDto.getCoverImage());
        InputStream resource = fileService.getResource(imgaeUploadPath, categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }

    //crate product with category
    @PostMapping("/{categoryId}/products")
    public  ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable String categoryId,
            @RequestBody ProductDto productDto
    )
    {
        ProductDto productServiceWithCategory = productService.createWithCategory(productDto, categoryId);
        return  new ResponseEntity<>(productServiceWithCategory,HttpStatus.CREATED);

    }
    //update category with product
    @PutMapping("/{categoryId}/products/{productId}")
    public  ResponseEntity<ProductDto> updateProductWithCategory(@PathVariable String categoryId, @PathVariable String productId)
    {
        ProductDto productDto = productService.updateWithCategory(categoryId, productId);
        return  new ResponseEntity<>(productDto,HttpStatus.OK);
    }
    //get product of category

    @GetMapping("/{categoryId}/products")
    public  ResponseEntity<PageableResponse<ProductDto>>  getProductWithCategory(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber ,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize ,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy ,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir,
            @PathVariable String categoryId )
    {
        PageableResponse<ProductDto> response = productService.getAllOfCategory(categoryId,pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(response,HttpStatus.OK);

    }
}
