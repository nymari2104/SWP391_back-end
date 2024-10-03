package com.example.demo.mapper;

import com.example.demo.dto.request.productRequest.ProductCreateRequest;
import com.example.demo.dto.request.productRequest.ProductUpdateRequest;
import com.example.demo.entity.Product;
import org.mapstruct.*;

@Mapper
public interface ProductMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product toProduct(@MappingTarget Product product, ProductCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "productName", source = "name")
    @Mapping(target = "unitPrice", source = "unitprice")
    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);

}
