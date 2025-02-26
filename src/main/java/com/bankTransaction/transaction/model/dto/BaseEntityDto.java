package com.bankTransaction.transaction.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BaseEntityDto {

    private Long id;
    private Date createdAt;
    private Date updatedAt;
}
