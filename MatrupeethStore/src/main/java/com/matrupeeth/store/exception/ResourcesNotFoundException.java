package com.matrupeeth.store.exception;

import lombok.Builder;

public class ResourcesNotFoundException extends RuntimeException {

    @Builder
    public ResourcesNotFoundException()
    {
        super("Resource not found !!");
    }
    public ResourcesNotFoundException(String message)
    {
        super(message);
    }
}
