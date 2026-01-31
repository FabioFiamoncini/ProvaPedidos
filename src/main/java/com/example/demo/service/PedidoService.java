package com.example.demo.service;

import com.example.demo.domain.entities.TipoItem;
import com.example.demo.domain.entities.Pedido;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PedidoService {

    public BigDecimal totalProdutos(Pedido pedido) {
        return pedido.getItens().stream()
                .filter(i -> i.getItemCatalogo().getTipo() == TipoItem.PRODUTO)
                .map(i -> i.getValorUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalServicos(Pedido pedido) {
        return pedido.getItens().stream()
                .filter(i -> i.getItemCatalogo().getTipo() == TipoItem.SERVICO)
                .map(i -> i.getValorUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal descontoAplicado(Pedido pedido) {
        BigDecimal produtos = totalProdutos(pedido);
        BigDecimal pct = pedido.getPercentualDesconto() == null ? BigDecimal.ZERO : pedido.getPercentualDesconto();
        return produtos.multiply(pct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal totalPedido(Pedido pedido) {
        return totalProdutos(pedido).subtract(descontoAplicado(pedido)).add(totalServicos(pedido));
    }
}
