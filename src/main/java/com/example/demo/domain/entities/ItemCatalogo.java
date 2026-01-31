package com.example.demo.domain.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "item_catalogo")
public class ItemCatalogo {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoItem tipo;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorUnitario;

    public ItemCatalogo() { }

    public ItemCatalogo(String nome, TipoItem tipo, BigDecimal valorUnitario) {
        this.nome = nome;
        this.tipo = tipo;
        this.valorUnitario = valorUnitario;
    }

    public UUID getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public TipoItem getTipo() { return tipo; }
    public void setTipo(TipoItem tipo) { this.tipo = tipo; }

    public BigDecimal getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
}
