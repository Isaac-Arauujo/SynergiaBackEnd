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

@RestController
@RequestMapping("/api/inscricoes")
@CrossOrigin(origins = "*")
@Tag(name = "Inscrições", description = "Operações relacionadas a inscrições")
public class InscricaoController {

    @Autowired
    private InscricaoService inscricaoService;

    @GetMapping
    @Operation(summary = "Listar todas as inscrições (apenas admin)")
    public ResponseEntity<List<InscricaoDTO>> findAll() {
        List<InscricaoDTO> inscricoes = inscricaoService.findAll();
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar inscrições do usuário")
    public ResponseEntity<List<InscricaoDTO>> findByUsuarioId(@PathVariable Long usuarioId) {
        List<InscricaoDTO> inscricoes = inscricaoService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/local/{localId}")
    @Operation(summary = "Listar inscrições do local (apenas admin)")
    public ResponseEntity<List<InscricaoDTO>> findByLocalId(@PathVariable Long localId) {
        List<InscricaoDTO> inscricoes = inscricaoService.findByLocalId(localId);
        return ResponseEntity.ok(inscricoes);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar inscrições por status (apenas admin)")
    public ResponseEntity<List<InscricaoDTO>> findByStatus(@PathVariable StatusInscricao status) {
        List<InscricaoDTO> inscricoes = inscricaoService.findByStatus(status);
        return ResponseEntity.ok(inscricoes);
    }

    @PostMapping
    @Operation(summary = "Criar nova inscrição")
    public ResponseEntity<?> create(
            @Valid @RequestBody InscricaoRequestDTO inscricaoRequest,
            @RequestHeader("Usuario-ID") Long usuarioId) {
        try {
            System.out.println("📱 Recebida requisição de inscrição do usuário: " + usuarioId);
            var result = inscricaoService.create(inscricaoRequest, usuarioId);
            return result.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.badRequest().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar inscrição: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar inscrição (apenas admin)")
    public ResponseEntity<?> confirmarInscricao(@PathVariable Long id) {
        try {
            System.out.println("🎯 Confirmando inscrição ID: " + id);
            boolean sucesso = inscricaoService.confirmarInscricao(id);
            
            if (sucesso) {
                System.out.println("✅ Inscrição confirmada com sucesso!");
                return ResponseEntity.ok().build();
            } else {
                System.out.println("❌ Falha ao confirmar inscrição");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("💥 Erro ao confirmar inscrição: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao confirmar inscrição: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/recusar")
    @Operation(summary = "Recusar inscrição (apenas admin)")
    public ResponseEntity<?> recusarInscricao(@PathVariable Long id) {
        try {
            System.out.println("🗑️ Recusando inscrição ID: " + id);
            boolean sucesso = inscricaoService.recusarInscricao(id);
            
            if (sucesso) {
                System.out.println("✅ Inscrição recusada com sucesso!");
                return ResponseEntity.ok().build();
            } else {
                System.out.println("❌ Falha ao recusar inscrição");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("💥 Erro ao recusar inscrição: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao recusar inscrição: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir inscrição")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (inscricaoService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/debug")
    @Operation(summary = "Debug - Verificar estado da inscrição")
    public ResponseEntity<String> verificarEstado(@PathVariable Long id) {
        String estado = inscricaoService.verificarEstadoInscricao(id);
        return ResponseEntity.ok(estado);
    }
}