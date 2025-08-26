package com.vivior.integration_spring_ai.dto;

public record ExpenseInfo(
        String category,
        String itemName,
        Double amount
) {
}
