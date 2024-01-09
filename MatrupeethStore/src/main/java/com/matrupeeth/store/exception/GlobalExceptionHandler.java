package com.matrupeeth.store.exception;

import com.matrupeeth.store.dtos.ApiResponseMassege;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourcesNotFoundException.class)
    public ResponseEntity<ApiResponseMassege> resourcesNotFoundExceptionHandler(ResourcesNotFoundException ex)
    {
        logger.info("Exception handler invoked !!");
        ApiResponseMassege response = ApiResponseMassege.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND).success(true).build();

        return  new ResponseEntity<ApiResponseMassege>(response,HttpStatus.NOT_FOUND);
    }

    //MethedArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethedArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex)
    {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String, Object> response=new HashMap<>();
        allErrors.stream().forEach(ObjectError->{
            String message = ObjectError.getDefaultMessage();
            String field = ((FieldError) ObjectError).getField();
            response.put(field,message);
        });
        return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    //Api handel bad api request
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMassege> badApiRequest(BadApiRequest ex)
    {
        logger.info("bad Api request handler invoked !!");
        ApiResponseMassege response = ApiResponseMassege.builder().message(ex.getMessage()).status(HttpStatus. BAD_REQUEST).success(false).build();

        return  new ResponseEntity<ApiResponseMassege>(response,HttpStatus.BAD_REQUEST);
    }

}
