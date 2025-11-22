package Synergia_PI.SynergiaBlog.Controllers;

import Synergia_PI.SynergiaBlog.DTOs.FerramentaDTO;
import Synergia_PI.SynergiaBlog.Services.FerramentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ferramentas")
@CrossOrigin(origins = "*")
@Tag(name = "Ferramentas", description = "Operações relacionadas a ferramentas")
public class FerramentaController {

    @Autowired
    private FerramentaService ferramentaService;

    @GetMapping
    @Operation(summary = "Listar todas as ferramentas")
    public ResponseEntity<List<FerramentaDTO>> findAll() {
        List<FerramentaDTO> ferramentas = ferramentaService.findAll();
        return ResponseEntity.ok(ferramentas);
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Listar ferramentas disponíveis")
    public ResponseEntity<List<FerramentaDTO>> findDisponiveis() {
        List<FerramentaDTO> ferramentas = ferramentaService.findDisponiveis();
        return ResponseEntity.ok(ferramentas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ferramenta por ID")
    public ResponseEntity<FerramentaDTO> findById(@PathVariable Long id) {
        return ferramentaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar nova ferramenta (apenas admin)")
    public ResponseEntity<FerramentaDTO> create(@Valid @RequestBody FerramentaDTO ferramentaDTO) {
        FerramentaDTO createdFerramenta = ferramentaService.create(ferramentaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFerramenta);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ferramenta (apenas admin)")
    public ResponseEntity<FerramentaDTO> update(
            @PathVariable Long id, 
            @Valid @RequestBody FerramentaDTO ferramentaDTO) {
        return ferramentaService.update(id, ferramentaDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir ferramenta (apenas admin)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (ferramentaService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/disponibilidade")
    @Operation(summary = "Verificar disponibilidade da ferramenta")
    public ResponseEntity<Boolean> isDisponivel(
            @PathVariable Long id, 
            @RequestParam Integer quantidade) {
        boolean disponivel = ferramentaService.isDisponivel(id, quantidade);
        return ResponseEntity.ok(disponivel);
    }
}