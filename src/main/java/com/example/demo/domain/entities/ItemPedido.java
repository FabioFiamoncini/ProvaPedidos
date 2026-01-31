package com.example.demo.domain.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_catalogo_id", nullable = false)
    private ItemCatalogo itemCatalogo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorUnitario;

    public ItemPedido() { }

    public ItemPedido(ItemCatalogo itemCatalogo, Integer quantidade, BigDecimal valorUnitario) {
        this.itemCatalogo = itemCatalogo;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getTotalBruto() {
        if (valorUnitario == null || quantidade == null) return BigDecimal.ZERO;
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade.longValue()));
    }

    public boolean isProduto() {
        return itemCatalogo != null && itemCatalogo.getTipo() == TipoItem.PRODUTO;
    }

    public UUID getId() { return id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public ItemCatalogo getItemCatalogo() { return itemCatalogo; }
    public void setItemCatalogo(ItemCatalogo itemCatalogo) { this.itemCatalogo = itemCatalogo; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
}
