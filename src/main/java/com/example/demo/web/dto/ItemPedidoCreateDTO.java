package com.example.demo.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ItemPedidoCreateDTO {
    @NotNull public UUID itemCatalogoId;
    @NotNull @Min(1) public Integer quantidade;
}