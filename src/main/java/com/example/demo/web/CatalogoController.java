package com.example.demo.web;

import com.example.demo.domain.entities.ItemCatalogo;
import com.example.demo.repos.ItemCatalogoRepository;
import com.example.demo.web.dto.ItemCatalogoCreateDTO;
import com.example.demo.web.dto.ItemCatalogoDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/catalogo")
public class CatalogoController {

    private final ItemCatalogoRepository repo;

    public CatalogoController(ItemCatalogoRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<ItemCatalogoDTO> criarItem(@Valid @RequestBody ItemCatalogoCreateDTO body) {
        var entity = new ItemCatalogo(body.nome, body.tipo, body.precoUnitario);
        var saved = repo.save(entity);
        return ResponseEntity.created(URI.create("/catalogo/" + saved.getId()))
                .body(toDto(saved));
    }

    @GetMapping
    public List<ItemCatalogoDTO> listarItem() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemCatalogoDTO> obterItem(@PathVariable UUID id) {
        return repo.findById(id)
                .map(e -> ResponseEntity.ok(toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemCatalogoDTO> atualizarItem(@PathVariable UUID id,
                                                         @Valid @RequestBody ItemCatalogoCreateDTO body) {
        return repo.findById(id).map(entity -> {
            entity.setNome(body.nome);
            entity.setTipo(body.tipo);
            entity.setValorUnitario(body.precoUnitario);
            var saved = repo.save(entity);
            return ResponseEntity.ok(toDto(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ItemCatalogoDTO toDto(ItemCatalogo e) {
        var dto = new ItemCatalogoDTO();
        dto.id = e.getId();
        dto.nome = e.getNome();
        dto.tipo = e.getTipo();
        dto.precoUnitario = e.getValorUnitario();
        return dto;
    }
}