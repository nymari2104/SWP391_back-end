package com.example.demo.mapper;

import com.example.demo.dto.request.productRequest.ProductCreateRequest;
import com.example.demo.dto.request.productRequest.ProductUpdateRequest;
import com.example.demo.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface ProductMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product toProduct(@MappingTarget Product product, ProductCreateRequest request);

}
