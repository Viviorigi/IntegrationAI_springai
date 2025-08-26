package com.vivior.integration_spring_ai.dto;

public record BillItem(String itemName,
                       Integer quantity,
                       Double price,
                       Double subTotal
                       ) {
}
