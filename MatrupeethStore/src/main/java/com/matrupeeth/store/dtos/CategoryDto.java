package com.matrupeeth.store.dtos;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CategoryDto {


    private String categoryId;
    @NotBlank(message="title is required")
    @Size(min =4, max =244,message = "this is title not blannk  4 charter !!")
    private  String title;
    @NotBlank(message = "Description is not blannk")
    private String description;
    private String coverImage;
}
