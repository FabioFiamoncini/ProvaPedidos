package com.example.demo.web.dto;

import com.example.demo.domain.entities.TipoItem;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PedidoDetalheDTO {
    public UUID id;
    public OffsetDateTime dataCriacao;
    public BigDecimal percentualDesconto;

    public BigDecimal totalProdutosBruto;
    public BigDecimal totalServicos;
    public BigDecimal descontoAplicado;
    public BigDecimal totalPedido;

    public List<ItemDTO> itens = new ArrayList<>();

    public static class ItemDTO {
        public UUID id;
        public UUID itemCatalogoId;
        public String nome;
        public TipoItem tipo;
        public Integer quantidade;
        public BigDecimal precoUnitario;
        public BigDecimal totalBruto;
    }
}