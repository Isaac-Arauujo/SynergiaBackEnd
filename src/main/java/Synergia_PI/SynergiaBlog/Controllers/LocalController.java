package Synergia_PI.SynergiaBlog.Controllers;

import Synergia_PI.SynergiaBlog.DTOs.LocalDTO;
import Synergia_PI.SynergiaBlog.Services.LocalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/locais")
@CrossOrigin(origins = "*")
@Tag(name = "Locais", description = "Operações relacionadas a locais de trabalho")
public class LocalController {

    @Autowired
    private LocalService localService;

    @GetMapping
    @Operation(summary = "Listar todos os locais")
    public ResponseEntity<List<LocalDTO>> findAll() {
        List<LocalDTO> locais = localService.findAll();
        return ResponseEntity.ok(locais);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar local por ID")
    public ResponseEntity<LocalDTO> findById(@PathVariable Long id) {
        return localService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Buscar locais disponíveis na data")
    public ResponseEntity<List<LocalDTO>> findLocaisDisponiveisNaData(
            @RequestParam LocalDate data) {
        List<LocalDTO> locais = localService.findLocaisDisponiveisNaData(data);
        return ResponseEntity.ok(locais);
    }

    @PostMapping
    @Operation(summary = "Criar novo local (apenas admin)")
    public ResponseEntity<LocalDTO> create(@Valid @RequestBody LocalDTO localDTO) {
        LocalDTO createdLocal = localService.create(localDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLocal);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar local (apenas admin)")
    public ResponseEntity<LocalDTO> update(
            @PathVariable Long id, 
            @Valid @RequestBody LocalDTO localDTO) {
        return localService.update(id, localDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir local (apenas admin)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (localService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/disponibilidade")
    @Operation(summary = "Verificar disponibilidade do local na data")
    public ResponseEntity<Boolean> isDataDisponivel(
            @PathVariable Long id, 
            @RequestParam LocalDate data) {
        boolean disponivel = localService.isDataDisponivel(id, data);
        return ResponseEntity.ok(disponivel);
    }
}