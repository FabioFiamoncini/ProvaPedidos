package com.example.demo.web;

import com.example.demo.domain.entities.ItemCatalogo;
import com.example.demo.domain.entities.ItemPedido;
import com.example.demo.domain.entities.Pedido;
import com.example.demo.repos.ItemCatalogoRepository;
import com.example.demo.repos.ItemPedidoRepository;
import com.example.demo.repos.PedidoRepository;
import com.example.demo.service.PedidoService;
import com.example.demo.web.dto.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepo;
    private final ItemPedidoRepository itemPedidoRepo;
    private final ItemCatalogoRepository catalogoRepo;
    private final PedidoService pedidoService;

    public PedidoController(PedidoRepository pedidoRepo,
                            ItemPedidoRepository itemPedidoRepo,
                            ItemCatalogoRepository catalogoRepo,
                            PedidoService pedidoService) {
        this.pedidoRepo = pedidoRepo;
        this.itemPedidoRepo = itemPedidoRepo;
        this.catalogoRepo = catalogoRepo;
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoDetalheDTO> criarPedido(@Valid @RequestBody PedidoCreateDTO body) {
        var pedido = new Pedido();
        pedido.setPercentualDesconto(body.percentualDesconto);
        var saved = pedidoRepo.save(pedido);
        return ResponseEntity.created(URI.create("/pedidos/" + saved.getId()))
                .body(toDetalheDTO(saved));
    }

    @GetMapping
    public ResponseEntity<?> listarPedidos() {
        var list = pedidoRepo.findAll().stream().map(this::toDetalheDTO).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDetalheDTO> obterPedido(@PathVariable UUID id) {
        return pedidoRepo.findById(id)
                .map(p -> ResponseEntity.ok(toDetalheDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDetalheDTO> atualizarPedido(@PathVariable UUID id,
                                                            @Valid @RequestBody PedidoUpdateDTO body) {
        return pedidoRepo.findById(id).map(pedido -> {
            pedido.setPercentualDesconto(body.percentualDesconto);
            var saved = pedidoRepo.save(pedido);
            return ResponseEntity.ok(toDetalheDTO(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPedido(@PathVariable UUID id) {
        if (!pedidoRepo.existsById(id)) return ResponseEntity.notFound().build();
        pedidoRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{pedidoId}/itens")
    @Transactional
    public ResponseEntity<PedidoDetalheDTO> adicionarItem(@PathVariable UUID pedidoId,
                                                          @Valid @RequestBody ItemPedidoCreateDTO body) {
        var pedido = pedidoRepo.findById(pedidoId).orElse(null);
        if (pedido == null) return ResponseEntity.notFound().build();

        ItemCatalogo itemCatalogo = catalogoRepo.findById(body.itemCatalogoId).orElse(null);
        if (itemCatalogo == null) return ResponseEntity.badRequest().build();

        var item = new ItemPedido(itemCatalogo, body.quantidade, itemCatalogo.getValorUnitario());
        pedido.adicionarItem(item);

        pedidoRepo.save(pedido); // cascade ALL salva o item
        return ResponseEntity.ok(toDetalheDTO(pedido));
    }

    @PutMapping("/{pedidoId}/itens/{itemId}")
    @Transactional
    public ResponseEntity<PedidoDetalheDTO> atualizarItem(@PathVariable UUID pedidoId,
                                                          @PathVariable UUID itemId,
                                                          @Valid @RequestBody ItemPedidoUpdateDTO body) {
        var pedido = pedidoRepo.findById(pedidoId).orElse(null);
        if (pedido == null) return ResponseEntity.notFound().build();

        var item = itemPedidoRepo.findById(itemId).orElse(null);
        if (item == null || item.getPedido() == null || !item.getPedido().getId().equals(pedidoId)) {
            return ResponseEntity.notFound().build();
        }

        item.setQuantidade(body.quantidade);
        itemPedidoRepo.save(item);
        return ResponseEntity.ok(toDetalheDTO(pedido));
    }

    @DeleteMapping("/{pedidoId}/itens/{itemId}")
    @Transactional
    public ResponseEntity<PedidoDetalheDTO> removerItem(@PathVariable UUID pedidoId,
                                                        @PathVariable UUID itemId) {
        var pedido = pedidoRepo.findById(pedidoId).orElse(null);
        if (pedido == null) return ResponseEntity.notFound().build();

        var item = itemPedidoRepo.findById(itemId).orElse(null);
        if (item == null || item.getPedido() == null || !item.getPedido().getId().equals(pedidoId)) {
            return ResponseEntity.notFound().build();
        }

        pedido.removerItem(item);
        pedidoRepo.save(pedido); // orphanRemoval remove o item
        return ResponseEntity.ok(toDetalheDTO(pedido));
    }

    // ===== Mapper → Detalhe com totais =====

    private PedidoDetalheDTO toDetalheDTO(Pedido p) {
        var dto = new PedidoDetalheDTO();
        dto.id = p.getId();
        dto.dataCriacao = p.getDataCriacao();
        dto.percentualDesconto = p.getPercentualDesconto();

        p.getItens().forEach(it -> {
            var i = new PedidoDetalheDTO.ItemDTO();
            i.id = it.getId();
            i.itemCatalogoId = it.getItemCatalogo().getId();
            i.nome = it.getItemCatalogo().getNome();
            i.tipo = it.getItemCatalogo().getTipo();
            i.quantidade = it.getQuantidade();
            i.precoUnitario = it.getValorUnitario();
            i.totalBruto = it.getTotalBruto();
            dto.itens.add(i);
        });

        dto.totalProdutosBruto = pedidoService.totalProdutos(p);
        dto.totalServicos = pedidoService.totalServicos(p);
        dto.descontoAplicado = pedidoService.descontoAplicado(p);
        dto.totalPedido = pedidoService.totalPedido(p);

        return dto;
    }
}