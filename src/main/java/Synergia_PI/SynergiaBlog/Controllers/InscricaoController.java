package Synergia_PI.SynergiaBlog.Controllers;


import Synergia_PI.SynergiaBlog.DTOs.InscricaoDTO;
import Synergia_PI.SynergiaBlog.DTOs.InscricaoRequestDTO;
import Synergia_PI.SynergiaBlog.Entidades.Inscricao.StatusInscricao;
import Synergia_PI.SynergiaBlog.Services.InscricaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inscricoes")
@CrossOrigin(origins = "*")
@Tag(name = "Inscrições", description = "Operações relacionadas a inscrições")
public class InscricaoController {

    @Autowired
    private InscricaoService inscricaoService;

    @GetMapping
    @Operation(summary = "Listar todas as inscrições")
    public ResponseEntity<List<InscricaoDTO>> findAll() {
        List<InscricaoDTO> inscricoes = inscricaoService.findAll();
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar inscrições por status")
    public ResponseEntity<List<InscricaoDTO>> findByStatus(@PathVariable StatusInscricao status) {
        List<InscricaoDTO> inscricoes = inscricaoService.findByStatus(status);
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar inscrições do usuário")
    public ResponseEntity<List<InscricaoDTO>> findByUsuarioId(@PathVariable Long usuarioId) {
        List<InscricaoDTO> inscricoes = inscricaoService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/local/{localId}")
    @Operation(summary = "Listar inscrições do local")
    public ResponseEntity<List<InscricaoDTO>> findByLocalId(@PathVariable Long localId) {
        List<InscricaoDTO> inscricoes = inscricaoService.findByLocalId(localId);
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar inscrição por ID")
    public ResponseEntity<InscricaoDTO> findById(@PathVariable Long id) {
        Optional<InscricaoDTO> inscricao = inscricaoService.findById(id);
        return inscricao.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/usuario/{usuarioId}")
    @Operation(summary = "Criar nova inscrição")
    public ResponseEntity<?> create(
            @PathVariable Long usuarioId,
            @Valid @RequestBody InscricaoRequestDTO inscricaoRequest) {
        
        Optional<InscricaoDTO> createdInscricao = inscricaoService.create(inscricaoRequest, usuarioId);
        
        if (createdInscricao.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInscricao.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Não foi possível criar a inscrição. Verifique se a data está disponível ou se você já está inscrito neste local.");
        }
    }

    @PutMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar inscrição")
    public ResponseEntity<Void> confirmar(@PathVariable Long id) {
        if (inscricaoService.confirmarInscricao(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/recusar")
    @Operation(summary = "Recusar inscrição")
    public ResponseEntity<Void> recusar(@PathVariable Long id) {
        if (inscricaoService.recusarInscricao(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status da inscrição")
    public ResponseEntity<InscricaoDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam StatusInscricao status) {
        Optional<InscricaoDTO> updatedInscricao = inscricaoService.updateStatus(id, status);
        return updatedInscricao.map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar inscrição")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (inscricaoService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}