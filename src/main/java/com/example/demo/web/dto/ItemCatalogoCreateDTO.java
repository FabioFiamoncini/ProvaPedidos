package com.example.demo.web.dto;

import com.example.demo.domain.entities.TipoItem;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ItemCatalogoCreateDTO {
    @NotBlank @Size(max = 150)
    public String nome;

    @NotNull
    public TipoItem tipo;

    @NotNull @DecimalMin("0.00")
    public BigDecimal precoUnitario;
}
