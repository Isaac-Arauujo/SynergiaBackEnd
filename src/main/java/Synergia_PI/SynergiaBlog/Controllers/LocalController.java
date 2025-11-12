package Synergia_PI.SynergiaBlog.Controllers;



import Synergia_PI.SynergiaBlog.DTOs.LocalDTO;
import Synergia_PI.SynergiaBlog.Services.LocalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locais")
@CrossOrigin(origins = "*")
@Tag(name = "Locais", description = "Operações relacionadas a locais de eventos")
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
        Optional<LocalDTO> local = localService.findById(id);
        return local.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Listar locais disponíveis na data")
    public ResponseEntity<List<LocalDTO>> findDisponiveisNaData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        List<LocalDTO> locais = localService.findLocaisDisponiveisNaData(data);
        return ResponseEntity.ok(locais);
    }

    @GetMapping("/{id}/disponibilidade")
    @Operation(summary = "Verificar disponibilidade do local na data")
    public ResponseEntity<Boolean> verificarDisponibilidade(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        boolean disponivel = localService.isDataDisponivel(id, data);
        return ResponseEntity.ok(disponivel);
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo local")
    public ResponseEntity<LocalDTO> create(@Valid @RequestBody LocalDTO localDTO) {
        try {
            LocalDTO createdLocal = localService.create(localDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLocal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar local")
    public ResponseEntity<LocalDTO> update(@PathVariable Long id, @Valid @RequestBody LocalDTO localDTO) {
        Optional<LocalDTO> updatedLocal = localService.update(id, localDTO);
        return updatedLocal.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir local")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (localService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}