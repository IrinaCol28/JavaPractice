package com.example.practice.dto;

import com.example.practice.models.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "id", source = "customer.id")
    @Mapping(target = "name", source = "customer.name")
    @Mapping(target = "email", source = "customer.email")
    @Mapping(target = "phone", source = "customer.phone")
    CustomerDTO customerToDTO(Customer customer);

    @Mapping(target = "id", source = "customerDTO.id")
    @Mapping(target = "name", source = "customerDTO.name")
    @Mapping(target = "email", source = "customerDTO.email")
    @Mapping(target = "phone", source = "customerDTO.phone")
    Customer dtoToCustomer(CustomerDTO customerDTO);
}

