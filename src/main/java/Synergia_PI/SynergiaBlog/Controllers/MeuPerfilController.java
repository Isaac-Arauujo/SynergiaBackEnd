package Synergia_PI.SynergiaBlog.Controllers;



import Synergia_PI.SynergiaBlog.DTOs.InscricaoDTO;
import Synergia_PI.SynergiaBlog.DTOs.UsuarioDTO;
import Synergia_PI.SynergiaBlog.Services.InscricaoService;
import Synergia_PI.SynergiaBlog.Services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
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
        Optional<UsuarioDTO> usuario = usuarioService.findById(usuarioId);
        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<InscricaoDTO> inscricoes = inscricaoService.findByUsuarioId(usuarioId);
        
        // Criar objeto de resposta com usuário e inscrições
        PerfilResponse response = new PerfilResponse(usuario.get(), inscricoes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{usuarioId}/inscricoes")
    @Operation(summary = "Listar inscrições do usuário")
    public ResponseEntity<List<InscricaoDTO>> getInscricoes(@PathVariable Long usuarioId) {
        List<InscricaoDTO> inscricoes = inscricaoService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(inscricoes);
    }

    // Classe interna para resposta do perfil
    public static class PerfilResponse {
        private UsuarioDTO usuario;
        private List<InscricaoDTO> inscricoes;

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