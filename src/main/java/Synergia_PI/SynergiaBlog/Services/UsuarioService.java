package Synergia_PI.SynergiaBlog.Services;


import Synergia_PI.SynergiaBlog.DTOs.UsuarioDTO;
import Synergia_PI.SynergiaBlog.Entidades.Usuario;
import Synergia_PI.SynergiaBlog.Interfaces.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> findById(Long id) {
        return usuarioRepository.findById(id)
                .map(this::toDTO);
    }

    public Optional<UsuarioDTO> findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(this::toDTO);
    }

    public UsuarioDTO create(UsuarioDTO usuarioDTO) {
        Usuario usuario = toEntity(usuarioDTO);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return toDTO(savedUsuario);
    }

    public Optional<UsuarioDTO> update(Long id, UsuarioDTO usuarioDTO) {
        return usuarioRepository.findById(id)
                .map(existingUsuario -> {
                    updateEntityFromDTO(existingUsuario, usuarioDTO);
                    Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
                    return toDTO(updatedUsuario);
                });
    }

    public boolean delete(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<UsuarioDTO> login(String email, String senha) {
        return usuarioRepository.findByEmailAndSenha(email, senha)
                .map(this::toDTO);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existsByCpf(String cpf) {
        return usuarioRepository.existsByCpf(cpf);
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNomeCompleto(usuario.getNomeCompleto());
        dto.setDataNascimento(usuario.getDataNascimento());
        dto.setCpf(usuario.getCpf());
        dto.setEmail(usuario.getEmail());
        dto.setFotoPerfil(usuario.getFotoPerfil());
        // Não incluir senha no DTO por segurança
        return dto;
    }

    private Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setCpf(dto.getCpf());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha()); // Em produção, hash da senha
        usuario.setFotoPerfil(dto.getFotoPerfil());
        return usuario;
    }

    private void updateEntityFromDTO(Usuario usuario, UsuarioDTO dto) {
        if (dto.getNomeCompleto() != null) {
            usuario.setNomeCompleto(dto.getNomeCompleto());
        }
        if (dto.getDataNascimento() != null) {
            usuario.setDataNascimento(dto.getDataNascimento());
        }
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            usuario.setSenha(dto.getSenha()); // Em produção, hash da senha
        }
        if (dto.getFotoPerfil() != null) {
            usuario.setFotoPerfil(dto.getFotoPerfil());
        }
    }
}