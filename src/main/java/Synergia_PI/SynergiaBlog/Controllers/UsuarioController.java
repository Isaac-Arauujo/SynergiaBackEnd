package Synergia_PI.SynergiaBlog.Controllers;

import Synergia_PI.SynergiaBlog.DTOs.LoginDTO;
import Synergia_PI.SynergiaBlog.DTOs.UsuarioDTO;
import Synergia_PI.SynergiaBlog.DTOs.AtualizarUsuarioDTO;
import Synergia_PI.SynergiaBlog.DTOs.AtualizarPerfilRequestDTO;
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
            System.out.println("📱 Recebida requisição de cadastro para: " + usuarioDTO.getEmail());
            
            // Verificação adicional de senha no controller também
            if (usuarioDTO.getSenha() == null || usuarioDTO.getConfirmacaoSenha() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Senha e confirmação de senha são obrigatórias");
            }
            
            if (!usuarioDTO.getSenha().equals(usuarioDTO.getConfirmacaoSenha())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Senha e confirmação de senha não coincidem");
            }
            
            UsuarioDTO createdUsuario = usuarioService.create(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
            
        } catch (RuntimeException e) {
            // Captura as exceções de validação do service
            String errorMessage = e.getMessage();
            HttpStatus status = HttpStatus.BAD_REQUEST;
            
            if (errorMessage.contains("já cadastrado")) {
                status = HttpStatus.CONFLICT;
            }
            
            return ResponseEntity.status(status).body(errorMessage);
            
        } catch (Exception e) {
            System.out.println("💥 Erro inesperado no cadastro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao cadastrar usuário");
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

    // ENDPOINT ORIGINAL (para compatibilidade) - Exige todos os campos
    @PutMapping("/{id}/completo")
    @Operation(summary = "Atualizar usuário (completo - todos campos obrigatórios)")
    public ResponseEntity<?> updateCompleto(
            @PathVariable Long id, 
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            System.out.println("📱 Recebida requisição PARA ATUALIZAÇÃO COMPLETA");
            Optional<UsuarioDTO> updatedUsuario = usuarioService.update(id, usuarioDTO);
            if (updatedUsuario.isPresent()) {
                return ResponseEntity.ok(updatedUsuario.get());
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email já está em uso por outro usuário");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    // NOVO ENDPOINT - Atualização parcial COM confirmação de senha
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário (parcial - apenas campos enviados)")
    public ResponseEntity<?> update(
            @PathVariable Long id, 
            @Valid @RequestBody AtualizarUsuarioDTO usuarioDTO) {
        try {
            System.out.println("📱 Recebida requisição para atualizar usuário ID: " + id);
            System.out.println("Dados recebidos: " + usuarioDTO.toString());
            
            Optional<UsuarioDTO> updatedUsuario = usuarioService.atualizarUsuario(id, usuarioDTO);
            
            if (updatedUsuario.isPresent()) {
                System.out.println("✅ Usuário atualizado com sucesso!");
                return ResponseEntity.ok(updatedUsuario.get());
            } else {
                System.out.println("❌ Falha ao atualizar usuário");
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email já está em uso, senhas não coincidem ou usuário não encontrado");
            }
        } catch (Exception e) {
            System.out.println("💥 Erro ao atualizar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    // ENDPOINT - Atualização de perfil COM confirmação de senha
    @PutMapping("/{id}/perfil")
    @Operation(summary = "Atualizar perfil do usuário")
    public ResponseEntity<?> atualizarPerfil(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarPerfilRequestDTO request) {
        try {
            System.out.println("📱 Recebida requisição para atualizar perfil do usuário ID: " + id);
            
            Optional<UsuarioDTO> updatedUsuario = usuarioService.atualizarPerfil(id, request);
            
            if (updatedUsuario.isPresent()) {
                System.out.println("✅ Perfil atualizado com sucesso para usuário ID: " + id);
                return ResponseEntity.ok(updatedUsuario.get());
            } else {
                System.out.println("❌ Falha ao atualizar perfil do usuário ID: " + id);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email já está em uso, senhas não coincidem ou usuário não encontrado");
            }
        } catch (Exception e) {
            System.out.println("💥 Erro ao atualizar perfil: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar perfil: " + e.getMessage());
        }
    }

    // NOVO ENDPOINT: Promover usuário para admin
    @PutMapping("/{id}/promover-admin")
    @Operation(summary = "Promover usuário para administrador")
    public ResponseEntity<?> promoverParaAdmin(@PathVariable Long id) {
        try {
            System.out.println("📱 Recebida requisição para promover usuário para admin ID: " + id);
            
            Optional<UsuarioDTO> updatedUsuario = usuarioService.promoverParaAdmin(id);
            
            if (updatedUsuario.isPresent()) {
                System.out.println("✅ Usuário promovido para ADMIN com sucesso!");
                return ResponseEntity.ok(updatedUsuario.get());
            } else {
                System.out.println("❌ Falha ao promover usuário para admin");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuário não encontrado");
            }
        } catch (Exception e) {
            System.out.println("💥 Erro ao promover usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao promover usuário: " + e.getMessage());
        }
    }

    // NOVO ENDPOINT: Rebaixar admin para voluntário
    @PutMapping("/{id}/rebaixar-voluntario")
    @Operation(summary = "Rebaixar administrador para voluntário")
    public ResponseEntity<?> rebaixarParaVoluntario(@PathVariable Long id) {
        try {
            System.out.println("📱 Recebida requisição para rebaixar admin para voluntário ID: " + id);
            
            Optional<UsuarioDTO> updatedUsuario = usuarioService.rebaixarParaVoluntario(id);
            
            if (updatedUsuario.isPresent()) {
                System.out.println("✅ Admin rebaixado para VOLUNTÁRIO com sucesso!");
                return ResponseEntity.ok(updatedUsuario.get());
            } else {
                System.out.println("❌ Falha ao rebaixar admin para voluntário");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Usuário não encontrado");
            }
        } catch (Exception e) {
            System.out.println("💥 Erro ao rebaixar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao rebaixar usuário: " + e.getMessage());
        }
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