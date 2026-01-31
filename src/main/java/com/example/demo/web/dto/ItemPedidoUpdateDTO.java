package com.example.demo.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ItemPedidoUpdateDTO {
    @NotNull @Min(1) public Integer quantidade;
}