package Synergia_PI.SynergiaBlog.Services;

import Synergia_PI.SynergiaBlog.DTOs.UsuarioDTO;
import Synergia_PI.SynergiaBlog.DTOs.AtualizarUsuarioDTO;
import Synergia_PI.SynergiaBlog.DTOs.AtualizarPerfilRequestDTO;
import Synergia_PI.SynergiaBlog.Entidades.Usuario;
import Synergia_PI.SynergiaBlog.Interfaces.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public UsuarioDTO create(UsuarioDTO usuarioDTO) {
        try {
            System.out.println("=== INICIANDO CADASTRO DE USUÁRIO ===");
            System.out.println("Email: " + usuarioDTO.getEmail());
            System.out.println("Nome: " + usuarioDTO.getNomeCompleto());
            
            // VALIDAÇÃO DE SENHA - CORREÇÃO ADICIONADA
            if (!usuarioDTO.getSenha().equals(usuarioDTO.getConfirmacaoSenha())) {
                System.out.println("❌ Senha e confirmação de senha não coincidem");
                throw new RuntimeException("Senha e confirmação de senha não coincidem");
            }
            
            // Verificar se email já existe
            if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                System.out.println("❌ Email já cadastrado: " + usuarioDTO.getEmail());
                throw new RuntimeException("Email já cadastrado");
            }
            
            // Verificar se CPF já existe
            if (usuarioRepository.existsByCpf(usuarioDTO.getCpf())) {
                System.out.println("❌ CPF já cadastrado: " + usuarioDTO.getCpf());
                throw new RuntimeException("CPF já cadastrado");
            }
            
            Usuario usuario = toEntity(usuarioDTO);
            Usuario savedUsuario = usuarioRepository.save(usuario);
            
            System.out.println("✅ Usuário cadastrado com sucesso: " + savedUsuario.getNomeCompleto());
            return toDTO(savedUsuario);
            
        } catch (RuntimeException e) {
            // Re-lançar exceções de validação
            throw e;
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado ao cadastrar usuário: " + e.getMessage());
            throw new RuntimeException("Erro interno ao cadastrar usuário");
        }
    }

    // MÉTODO ORIGINAL (para compatibilidade)
    @Transactional
    public Optional<UsuarioDTO> update(Long id, UsuarioDTO usuarioDTO) {
        try {
            System.out.println("=== ATUALIZAÇÃO COMPLETA DE USUÁRIO ===");
            System.out.println("ID do usuário: " + id);
            
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                
                // Validações de email único (se email foi alterado)
                if (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().equals(usuario.getEmail())) {
                    if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                        System.out.println("❌ Email já está em uso: " + usuarioDTO.getEmail());
                        return Optional.empty();
                    }
                }
                
                // Atualiza todos os campos
                updateEntityFromDTO(usuario, usuarioDTO);
                
                Usuario updatedUsuario = usuarioRepository.save(usuario);
                System.out.println("✅ Usuário atualizado com sucesso!");
                
                return Optional.of(toDTO(updatedUsuario));
            } else {
                System.out.println("❌ Usuário não encontrado: " + id);
                return Optional.empty();
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao atualizar usuário: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // NOVO MÉTODO - Para atualização com AtualizarUsuarioDTO (com confirmação de senha)
    @Transactional
    public Optional<UsuarioDTO> atualizarUsuario(Long id, AtualizarUsuarioDTO request) {
        try {
            System.out.println("=== ATUALIZAÇÃO DE USUÁRIO (COM CONFIRMAÇÃO SENHA) ===");
            System.out.println("ID do usuário: " + id);
            System.out.println("Dados recebidos: " + request.toString());
            
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                
                // Validação de email único (se email foi alterado)
                if (request.getEmail() != null && !request.getEmail().equals(usuario.getEmail())) {
                    if (usuarioRepository.existsByEmail(request.getEmail())) {
                        System.out.println("❌ Email já está em uso: " + request.getEmail());
                        return Optional.empty();
                    }
                    usuario.setEmail(request.getEmail());
                    System.out.println("📧 Email atualizado");
                }
                
                // Validação de senha e confirmação
                if (request.getSenha() != null && !request.getSenha().trim().isEmpty()) {
                    if (request.getConfirmacaoSenha() == null || !request.getSenha().equals(request.getConfirmacaoSenha())) {
                        System.out.println("❌ Senha e confirmação de senha não coincidem");
                        return Optional.empty();
                    }
                    usuario.setSenha(request.getSenha());
                    System.out.println("🔑 Senha atualizada");
                }
                
                // Atualiza apenas os campos que foram enviados
                if (request.getNomeCompleto() != null) {
                    usuario.setNomeCompleto(request.getNomeCompleto());
                    System.out.println("👤 Nome atualizado: " + request.getNomeCompleto());
                }
                if (request.getDataNascimento() != null) {
                    usuario.setDataNascimento(request.getDataNascimento());
                    System.out.println("🎂 Data de nascimento atualizada");
                }
                if (request.getFotoPerfil() != null) {
                    usuario.setFotoPerfil(request.getFotoPerfil());
                    System.out.println("🖼️ Foto de perfil atualizada");
                }
                
                Usuario updatedUsuario = usuarioRepository.save(usuario);
                System.out.println("✅ Usuário atualizado com sucesso!");
                
                return Optional.of(toDTO(updatedUsuario));
            } else {
                System.out.println("❌ Usuário não encontrado: " + id);
                return Optional.empty();
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao atualizar usuário: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // MÉTODO - Para atualização com AtualizarPerfilRequestDTO (com confirmação de senha)
    @Transactional
    public Optional<UsuarioDTO> atualizarPerfil(Long id, AtualizarPerfilRequestDTO request) {
        try {
            System.out.println("=== ATUALIZAÇÃO DE PERFIL ===");
            System.out.println("ID do usuário: " + id);
            System.out.println("Dados recebidos: " + request.toString());
            
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
            
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                
                // Validação de email único (se email foi alterado)
                if (request.getEmail() != null && !request.getEmail().equals(usuario.getEmail())) {
                    if (usuarioRepository.existsByEmail(request.getEmail())) {
                        System.out.println("❌ Email já está em uso: " + request.getEmail());
                        return Optional.empty();
                    }
                    usuario.setEmail(request.getEmail());
                    System.out.println("📧 Email atualizado");
                }
                
                // VALIDAÇÃO DE SENHA - CORREÇÃO ADICIONADA
                if (request.getSenha() != null && !request.getSenha().trim().isEmpty()) {
                    if (request.getConfirmacaoSenha() == null || !request.getSenha().equals(request.getConfirmacaoSenha())) {
                        System.out.println("❌ Senha e confirmação de senha não coincidem");
                        return Optional.empty();
                    }
                    usuario.setSenha(request.getSenha());
                    System.out.println("🔑 Senha atualizada");
                }
                
                // Atualiza apenas os campos que foram enviados
                if (request.getNomeCompleto() != null) {
                    usuario.setNomeCompleto(request.getNomeCompleto());
                    System.out.println("👤 Nome atualizado: " + request.getNomeCompleto());
                }
                if (request.getDataNascimento() != null) {
                    usuario.setDataNascimento(request.getDataNascimento());
                    System.out.println("🎂 Data de nascimento atualizada");
                }
                if (request.getFotoPerfil() != null) {
                    usuario.setFotoPerfil(request.getFotoPerfil());
                    System.out.println("🖼️ Foto de perfil atualizada");
                }
                
                Usuario updatedUsuario = usuarioRepository.save(usuario);
                System.out.println("✅ Perfil atualizado com sucesso!");
                
                return Optional.of(toDTO(updatedUsuario));
            } else {
                System.out.println("❌ Usuário não encontrado: " + id);
                return Optional.empty();
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao atualizar perfil: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
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
        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            usuario.setSenha(dto.getSenha()); // Em produção, hash da senha
        }
        if (dto.getFotoPerfil() != null) {
            usuario.setFotoPerfil(dto.getFotoPerfil());
        }
    }
}