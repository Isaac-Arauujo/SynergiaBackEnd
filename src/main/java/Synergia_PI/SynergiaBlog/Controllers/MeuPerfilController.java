package Synergia_PI.SynergiaBlog.Controllers;

import Synergia_PI.SynergiaBlog.DTOs.InscricaoDTO;
import Synergia_PI.SynergiaBlog.DTOs.UsuarioDTO;
import Synergia_PI.SynergiaBlog.DTOs.AtualizarPerfilRequestDTO;
import Synergia_PI.SynergiaBlog.Services.InscricaoService;
import Synergia_PI.SynergiaBlog.Services.UsuarioService;
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
@RequestMapping("/api/meu-perfil")
@CrossOrigin(origins = "*")
@Tag(name = "Meu Perfil", description = "Operações relacionadas ao perfil do usuário")
public class MeuPerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private InscricaoService inscricaoService;

    @GetMapping("/{usuarioId}")
    @Operation(summary = "Obter dados do perfil")
    public ResponseEntity<?> getPerfil(@PathVariable Long usuarioId) {
        try {
            System.out.println("📱 MeuPerfilController - Obtendo perfil do usuário ID: " + usuarioId);
            
            Optional<UsuarioDTO> usuario = usuarioService.findById(usuarioId);
            if (usuario.isEmpty()) {
                System.out.println("❌ Usuário não encontrado: " + usuarioId);
                return ResponseEntity.notFound().build();
            }

            List<InscricaoDTO> inscricoes = inscricaoService.findByUsuarioId(usuarioId);
            
            // Criar objeto de resposta com usuário e inscrições
            PerfilResponse response = new PerfilResponse(usuario.get(), inscricoes);
            System.out.println("✅ Perfil obtido com sucesso para usuário: " + usuario.get().getNomeCompleto());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("💥 Erro ao obter perfil: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter perfil: " + e.getMessage());
        }
    }

    @GetMapping("/{usuarioId}/inscricoes")
    @Operation(summary = "Listar inscrições do usuário")
    public ResponseEntity<List<InscricaoDTO>> getInscricoes(@PathVariable Long usuarioId) {
        try {
            System.out.println("📱 MeuPerfilController - Obtendo inscrições do usuário ID: " + usuarioId);
            
            List<InscricaoDTO> inscricoes = inscricaoService.findByUsuarioId(usuarioId);
            System.out.println("✅ Encontradas " + inscricoes.size() + " inscrições para o usuário");
            
            return ResponseEntity.ok(inscricoes);
        } catch (Exception e) {
            System.out.println("💥 Erro ao obter inscrições: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{usuarioId}")
    @Operation(summary = "Atualizar perfil do usuário")
    public ResponseEntity<?> atualizarPerfil(
            @PathVariable Long usuarioId,
            @Valid @RequestBody AtualizarPerfilRequestDTO request) {
        try {
            System.out.println("📱 MeuPerfilController - Atualizando perfil do usuário ID: " + usuarioId);
            System.out.println("Dados recebidos: " + request.toString());
            
            Optional<UsuarioDTO> updatedUsuario = usuarioService.atualizarPerfil(usuarioId, request);
            
            if (updatedUsuario.isPresent()) {
                // Retorna os dados atualizados do perfil
                List<InscricaoDTO> inscricoes = inscricaoService.findByUsuarioId(usuarioId);
                PerfilResponse response = new PerfilResponse(updatedUsuario.get(), inscricoes);
                
                System.out.println("✅ Perfil atualizado com sucesso para usuário: " + updatedUsuario.get().getNomeCompleto());
                return ResponseEntity.ok(response);
            } else {
                System.out.println("❌ Falha ao atualizar perfil do usuário ID: " + usuarioId);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email já está em uso por outro usuário ou usuário não encontrado");
            }
        } catch (Exception e) {
            System.out.println("💥 Erro ao atualizar perfil: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar perfil: " + e.getMessage());
        }
    }

    // Classe interna para resposta do perfil
    public static class PerfilResponse {
        private UsuarioDTO usuario;
        private List<InscricaoDTO> inscricoes;

        public PerfilResponse() {}

        public PerfilResponse(UsuarioDTO usuario, List<InscricaoDTO> inscricoes) {
            this.usuario = usuario;
            this.inscricoes = inscricoes;
        }

        // Getters e Setters
        public UsuarioDTO getUsuario() { return usuario; }
        public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }
        
        public List<InscricaoDTO> getInscricoes() { return inscricoes; }
        public void setInscricoes(List<InscricaoDTO> inscricoes) { this.inscricoes = inscricoes; }
    }
}