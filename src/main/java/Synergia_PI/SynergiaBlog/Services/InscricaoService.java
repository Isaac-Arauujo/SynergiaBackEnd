package Synergia_PI.SynergiaBlog.Services;

import Synergia_PI.SynergiaBlog.DTOs.InscricaoDTO;
import Synergia_PI.SynergiaBlog.DTOs.InscricaoRequestDTO;
import Synergia_PI.SynergiaBlog.Entidades.Inscricao;
import Synergia_PI.SynergiaBlog.Entidades.Local;
import Synergia_PI.SynergiaBlog.Entidades.Inscricao.StatusInscricao;
import Synergia_PI.SynergiaBlog.Entidades.Usuario;
import Synergia_PI.SynergiaBlog.Interfaces.Repositories.InscricaoRepository;
import Synergia_PI.SynergiaBlog.Interfaces.Repositories.LocalRepository;
import Synergia_PI.SynergiaBlog.Interfaces.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InscricaoService {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private LocalService localService;

    @Autowired
    private EmailService emailService;

    public List<InscricaoDTO> findAll() {
        return inscricaoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<InscricaoDTO> findByStatus(StatusInscricao status) {
        return inscricaoRepository.findByStatusOrderByCreatedAtDesc(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<InscricaoDTO> findByUsuarioId(Long usuarioId) {
        return inscricaoRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<InscricaoDTO> findByLocalId(Long localId) {
        return inscricaoRepository.findByLocalIdOrderByCreatedAtDesc(localId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<InscricaoDTO> findById(Long id) {
        return inscricaoRepository.findById(id)
                .map(this::toDTO);
    }

    @Transactional
    public Optional<InscricaoDTO> create(InscricaoRequestDTO inscricaoRequest, Long usuarioId) {
        try {
            System.out.println("=== INICIANDO CRIAÇÃO DE INSCRIÇÃO ===");
            System.out.println("Usuário ID: " + usuarioId);
            System.out.println("Local ID: " + inscricaoRequest.getLocalId());
            System.out.println("Data desejada: " + inscricaoRequest.getDataDesejada());
            
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            Optional<Local> localOpt = localRepository.findById(inscricaoRequest.getLocalId());
            
            if (usuarioOpt.isEmpty() || localOpt.isEmpty()) {
                System.out.println("❌ Usuário ou Local não encontrado");
                return Optional.empty();
            }

            Usuario usuario = usuarioOpt.get();
            Local local = localOpt.get();

            // Verificar se a data está disponível
            if (!localService.isDataDisponivel(inscricaoRequest.getLocalId(), inscricaoRequest.getDataDesejada())) {
                System.out.println("❌ Data não disponível para o local");
                return Optional.empty();
            }

            // Verificar se já existe inscrição para o mesmo local e data
            Optional<Inscricao> existingInscricao = inscricaoRepository.findByUsuarioAndLocal(usuarioId, inscricaoRequest.getLocalId());
            if (existingInscricao.isPresent()) {
                System.out.println("❌ Usuário já possui inscrição para este local");
                return Optional.empty();
            }

            Inscricao inscricao = new Inscricao();
            inscricao.setUsuario(usuario);
            inscricao.setLocal(local);
            inscricao.setDataDesejada(inscricaoRequest.getDataDesejada());
            inscricao.setStatus(StatusInscricao.PENDENTE);

            Inscricao savedInscricao = inscricaoRepository.save(inscricao);
            System.out.println("✅ Inscrição criada com sucesso. ID: " + savedInscricao.getId());
            
            // ENVIAR EMAIL DE CONFIRMAÇÃO DE INSCRIÇÃO
            try {
                System.out.println("📧 Enviando email de confirmação para: " + usuario.getEmail());
                emailService.enviarEmailConfirmacaoInscricao(
                    usuario.getEmail(),
                    usuario.getNomeCompleto(),
                    local.getNome(),
                    inscricaoRequest.getDataDesejada().toString()
                );
                System.out.println("✅ Email enviado com sucesso!");
            } catch (Exception e) {
                System.out.println("⚠️ Erro ao enviar email, mas inscrição foi salva: " + e.getMessage());
                e.printStackTrace();
            }
            
            return Optional.of(toDTO(savedInscricao));
            
        } catch (Exception e) {
            System.out.println("❌ Erro ao criar inscrição: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<InscricaoDTO> updateStatus(Long id, StatusInscricao novoStatus) {
        try {
            System.out.println("=== INICIANDO ATUALIZAÇÃO DE STATUS ===");
            System.out.println("ID da inscrição: " + id);
            System.out.println("Novo status: " + novoStatus);
            
            Optional<Inscricao> inscricaoOpt = inscricaoRepository.findById(id);
            
            if (inscricaoOpt.isPresent()) {
                Inscricao inscricao = inscricaoOpt.get();
                System.out.println("Status ANTES da atualização: " + inscricao.getStatus());
                System.out.println("Usuário: " + inscricao.getUsuario().getNomeCompleto());
                System.out.println("Local: " + inscricao.getLocal().getNome());
                
                StatusInscricao statusAnterior = inscricao.getStatus();
                
                // ATUALIZA O STATUS
                inscricao.setStatus(novoStatus);
                
                // SALVA E FLUSH - GARANTE PERSISTÊNCIA IMEDIATA
                Inscricao updatedInscricao = inscricaoRepository.saveAndFlush(inscricao);
                
                System.out.println("Status DEPOIS da atualização: " + updatedInscricao.getStatus());
                
                // ENVIAR EMAIL SE A INSCRIÇÃO FOI CONFIRMADA
                if (novoStatus == StatusInscricao.CONFIRMADA && statusAnterior != StatusInscricao.CONFIRMADA) {
                    try {
                        System.out.println("📧 Enviando email de confirmação para: " + inscricao.getUsuario().getEmail());
                        emailService.enviarEmailInscricaoConfirmada(
                            inscricao.getUsuario().getEmail(),
                            inscricao.getUsuario().getNomeCompleto(),
                            inscricao.getLocal().getNome(),
                            inscricao.getDataDesejada().toString()
                        );
                        System.out.println("✅ Email de confirmação enviado com sucesso!");
                    } catch (Exception e) {
                        System.out.println("⚠️ Erro ao enviar email de confirmação: " + e.getMessage());
                    }
                }
                
                System.out.println("=== ATUALIZAÇÃO CONCLUÍDA COM SUCESSO ===");
                
                return Optional.of(toDTO(updatedInscricao));
            } else {
                System.out.println("❌ Inscrição não encontrada para o ID: " + id);
                return Optional.empty();
            }
        } catch (Exception e) {
            System.out.println("❌ ERRO CRÍTICO ao atualizar status: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Transactional
    public boolean confirmarInscricao(Long id) {
        System.out.println("🎯 === MÉTODO confirmarInscricao CHAMADO ===");
        System.out.println("🎯 Confirmando inscrição ID: " + id);
        
        Optional<InscricaoDTO> resultado = updateStatus(id, StatusInscricao.CONFIRMADA);
        boolean sucesso = resultado.isPresent();
        
        System.out.println("🎯 Resultado da confirmação: " + sucesso);
        System.out.println("🎯 === FIM confirmarInscricao ===");
        
        return sucesso;
    }

    @Transactional
    public boolean recusarInscricao(Long id) {
        System.out.println("🗑️ === MÉTODO recusarInscricao CHAMADO ===");
        System.out.println("🗑️ Recusando inscrição ID: " + id);
        
        Optional<InscricaoDTO> resultado = updateStatus(id, StatusInscricao.RECUSADA);
        boolean sucesso = resultado.isPresent();
        
        System.out.println("🗑️ Resultado da recusa: " + sucesso);
        System.out.println("🗑️ === FIM recusarInscricao ===");
        
        return sucesso;
    }

    @Transactional
    public boolean delete(Long id) {
        if (inscricaoRepository.existsById(id)) {
            inscricaoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsInscricaoParaLocalEData(Long localId, Long usuarioId) {
        return inscricaoRepository.findByUsuarioAndLocal(usuarioId, localId).isPresent();
    }

    // MÉTODO DE DEBUG PARA VERIFICAR O ESTADO ATUAL
    public String verificarEstadoInscricao(Long id) {
        Optional<Inscricao> inscricaoOpt = inscricaoRepository.findById(id);
        if (inscricaoOpt.isPresent()) {
            Inscricao inscricao = inscricaoOpt.get();
            String estado = String.format(
                "📊 INSCRIÇÃO ID %d: Status=%s, Usuário=%s, Local=%s, Data=%s",
                inscricao.getId(),
                inscricao.getStatus(),
                inscricao.getUsuario().getNomeCompleto(),
                inscricao.getLocal().getNome(),
                inscricao.getDataDesejada()
            );
            System.out.println(estado);
            return estado;
        }
        return "❌ Inscrição não encontrada: " + id;
    }

    private InscricaoDTO toDTO(Inscricao inscricao) {
        InscricaoDTO dto = new InscricaoDTO();
        dto.setId(inscricao.getId());
        dto.setUsuarioId(inscricao.getUsuario().getId());
        dto.setUsuarioNome(inscricao.getUsuario().getNomeCompleto());
        dto.setUsuarioEmail(inscricao.getUsuario().getEmail());
        dto.setUsuarioIdade(inscricao.getUsuario().getIdade());
        dto.setUsuarioFoto(inscricao.getUsuario().getFotoPerfil());
        dto.setLocalId(inscricao.getLocal().getId());
        dto.setLocalNome(inscricao.getLocal().getNome());
        dto.setDataDesejada(inscricao.getDataDesejada());
        dto.setStatus(inscricao.getStatus());
        return dto;
    }
}