package com.example.demo.web.dto;

import com.example.demo.domain.entities.TipoItem;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemCatalogoDTO {
    public UUID id;
    public String nome;
    public TipoItem tipo;
    public BigDecimal precoUnitario;
}