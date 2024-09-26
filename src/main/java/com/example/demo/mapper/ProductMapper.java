package com.example.demo.mapper;

import com.example.demo.dto.request.productRequest.ProductCreateRequest;
import com.example.demo.dto.request.productRequest.ProductUpdateRequest;
import com.example.demo.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface ProductMapper {

    Product toProduct(@MappingTarget Product product, ProductCreateRequest request);

}
