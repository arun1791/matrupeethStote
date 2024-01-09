package com.matrupeeth.store.helper;

import com.matrupeeth.store.dtos.PageableResponse;
import com.matrupeeth.store.dtos.UserDto;
import com.matrupeeth.store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public  static<U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type)
    {
        List<U> Allusers = page.getContent();
        List<V> dtoList = Allusers.stream().map(object -> new ModelMapper().map(object,type)).collect(Collectors.toList());
        PageableResponse<V> response=new PageableResponse();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        return response;

    }
}
