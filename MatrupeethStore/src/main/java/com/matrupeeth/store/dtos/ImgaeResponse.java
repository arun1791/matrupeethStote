package com.matrupeeth.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImgaeResponse {
        private  String imageName;
        private String message;
        private boolean success;
        private HttpStatus status;


}
