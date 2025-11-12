package Synergia_PI.SynergiaBlog.Controllers;



import Synergia_PI.SynergiaBlog.DTOs.LoginDTO;
import Synergia_PI.SynergiaBlog.DTOs.UsuarioDTO;
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
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        List<UsuarioDTO> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
        Optional<UsuarioDTO> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastro")
    @Operation(summary = "Cadastrar novo usuário")
    public ResponseEntity<?> create(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            // Verificar se email já existe
            if (usuarioService.existsByEmail(usuarioDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email já cadastrado");
            }
            
            // Verificar se CPF já existe
            if (usuarioService.existsByCpf(usuarioDTO.getCpf())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("CPF já cadastrado");
            }
            
            UsuarioDTO createdUsuario = usuarioService.create(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        Optional<UsuarioDTO> usuario = usuarioService.login(loginDTO.getEmail(), loginDTO.getSenha());
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou senha inválidos");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        Optional<UsuarioDTO> updatedUsuario = usuarioService.update(id, usuarioDTO);
        return updatedUsuario.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (usuarioService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/verificar-email/{email}")
    @Operation(summary = "Verificar se email está disponível")
    public ResponseEntity<Boolean> verificarEmail(@PathVariable String email) {
        boolean disponivel = !usuarioService.existsByEmail(email);
        return ResponseEntity.ok(disponivel);
    }

    @GetMapping("/verificar-cpf/{cpf}")
    @Operation(summary = "Verificar se CPF está disponível")
    public ResponseEntity<Boolean> verificarCpf(@PathVariable String cpf) {
        boolean disponivel = !usuarioService.existsByCpf(cpf);
        return ResponseEntity.ok(disponivel);
    }
}