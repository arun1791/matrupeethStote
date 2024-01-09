package com.matrupeeth.store.valiadte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageNamevalidator implements ConstraintValidator<ImageNamevalid,String> {

    private Logger log = LoggerFactory.getLogger(ImageNamevalidator.class);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.info("Message from valid {} " , value);
        if(value.isBlank())
        {
            return false;
        }else
        {
            return true;
        }

        //return false;
    }
}
