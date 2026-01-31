package com.example.demo.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public class PedidoUpdateDTO {
    @DecimalMin("0.00")
    @Digits(integer = 3, fraction = 2)
    @Max(100) @Min(0)
    public BigDecimal percentualDesconto;
}