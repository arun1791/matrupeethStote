package com.matrupeeth.store.controller;

import com.matrupeeth.store.dtos.*;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Value("${product.image.path}")
    private String imgaeUploadPath;
    private Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private FileService fileService;

    //created by
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<ProductDto>create(@RequestBody ProductDto productDto)
    {
        ProductDto productDto1 = productService.create(productDto);
        return new ResponseEntity<>(productDto1, HttpStatus.CREATED);
    }
    //updated
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto>updateProduct(@RequestBody ProductDto productDto,@PathVariable String productId)
    {
        ProductDto productDto1 = productService.update(productDto,productId);
        return new ResponseEntity<>(productDto1, HttpStatus.OK);
    }

    //deleted
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMassege>deleteProduct(@PathVariable String productId)
    {
        productService.delete(productId);
        ApiResponseMassege response  = ApiResponseMassege.builder()
                .message("Product deleted successfully")
                .status(HttpStatus.OK)
                .success(true)
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
    //getall
    @GetMapping
    public  ResponseEntity<PageableResponse>getAllProduct(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber ,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize ,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy ,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
    )
    {
        PageableResponse<ProductDto> allPage = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(allPage,HttpStatus.OK);
    }
    //getsingle
    @GetMapping("/{productId}")
    public  ResponseEntity<ProductDto>getSingleProduct(@PathVariable String productId)
    {
        ProductDto signleProduct = productService.getSignle(productId);
        return  new ResponseEntity<>(signleProduct,HttpStatus.OK);
    }
    //get all live
    @GetMapping("/live")
    public ResponseEntity<PageableResponse>getAllLive(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber ,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize ,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy ,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        PageableResponse<ProductDto> allLiveProduct = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(allLiveProduct,HttpStatus.OK);
    }
    //get serahc tiitel
    @GetMapping("/search/{subTitle}")
    public  ResponseEntity<PageableResponse>getSearchTitle(
            @RequestParam(value="pageNumber",defaultValue = "0",required = false) int pageNumber ,
            @RequestParam(value="pageSize",defaultValue = "10",required = false) int pageSize ,
            @RequestParam(value="sortBy",defaultValue = "title",required = false) String sortBy ,
            @RequestParam(value="sortDir",defaultValue = "asc",required = false) String sortDir,
            @PathVariable String subTitle
    ){
        PageableResponse<ProductDto> productDtoPageableResponse = productService.serachByTitle(subTitle, pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(productDtoPageableResponse,HttpStatus.OK);
    }
    //upload image
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/image/{productId}")
    public  ResponseEntity<ImgaeResponse> uploadUserImage(
            @PathVariable String productId,
            @RequestParam("productImage") MultipartFile image
    ) throws IOException {
        String imageName = fileService.uploadImage(image, imgaeUploadPath);
        ProductDto productDto = productService.getSignle(productId);
        productDto.setProductImageName(imageName);
        productService.update(productDto, productId);
        ImgaeResponse imgaeResponse=ImgaeResponse.builder().imageName(imageName).message("product image uploaded!! ").success(true).status(HttpStatus.CREATED).build();
        return  new ResponseEntity<>(imgaeResponse,HttpStatus.CREATED);
    }

    //seve image

    @GetMapping("/image/{productId}")
    public  void serveUserImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
        ProductDto productDto = productService.getSignle(productId);
        logger.info("user details user :{}",productDto.getProductImageName());
        InputStream resource = fileService.getResource(imgaeUploadPath, productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }




}
