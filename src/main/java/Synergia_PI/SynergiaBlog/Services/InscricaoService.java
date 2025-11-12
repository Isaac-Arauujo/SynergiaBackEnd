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
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Optional<Local> localOpt = localRepository.findById(inscricaoRequest.getLocalId());
        
        if (usuarioOpt.isEmpty() || localOpt.isEmpty()) {
            return Optional.empty();
        }

        // Verificar se a data está disponível
        if (!localService.isDataDisponivel(inscricaoRequest.getLocalId(), inscricaoRequest.getDataDesejada())) {
            return Optional.empty();
        }

        // Verificar se já existe inscrição para o mesmo local e data
        Optional<Inscricao> existingInscricao = inscricaoRepository.findByUsuarioAndLocal(usuarioId, inscricaoRequest.getLocalId());
        if (existingInscricao.isPresent()) {
            return Optional.empty();
        }

        Inscricao inscricao = new Inscricao();
        inscricao.setUsuario(usuarioOpt.get());
        inscricao.setLocal(localOpt.get());
        inscricao.setDataDesejada(inscricaoRequest.getDataDesejada());
        inscricao.setStatus(StatusInscricao.PENDENTE);

        Inscricao savedInscricao = inscricaoRepository.save(inscricao);
        return Optional.of(toDTO(savedInscricao));
    }

    @Transactional
    public Optional<InscricaoDTO> updateStatus(Long id, StatusInscricao novoStatus) {
        return inscricaoRepository.findById(id)
                .map(inscricao -> {
                    inscricao.setStatus(novoStatus);
                    Inscricao updatedInscricao = inscricaoRepository.save(inscricao);
                    return toDTO(updatedInscricao);
                });
    }

    @Transactional
    public boolean confirmarInscricao(Long id) {
        return updateStatus(id, StatusInscricao.CONFIRMADA).isPresent();
    }

    @Transactional
    public boolean recusarInscricao(Long id) {
        return updateStatus(id, StatusInscricao.RECUSADA).isPresent();
    }

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