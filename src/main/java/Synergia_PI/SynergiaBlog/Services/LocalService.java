package Synergia_PI.SynergiaBlog.Services;



import Synergia_PI.SynergiaBlog.DTOs.FerramentaLocalDTO;
import Synergia_PI.SynergiaBlog.DTOs.LocalDTO;
import Synergia_PI.SynergiaBlog.Entidades.Ferramenta;
import Synergia_PI.SynergiaBlog.Entidades.Local;
import Synergia_PI.SynergiaBlog.Entidades.LocalFerramenta;
import Synergia_PI.SynergiaBlog.Interfaces.Repositories.FerramentaRepository;
import Synergia_PI.SynergiaBlog.Interfaces.Repositories.LocalFerramentaRepository;
import Synergia_PI.SynergiaBlog.Interfaces.Repositories.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocalService {

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private FerramentaRepository ferramentaRepository;

    @Autowired
    private LocalFerramentaRepository localFerramentaRepository;

    public List<LocalDTO> findAll() {
        return localRepository.findByOrderByDataInicioAsc().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<LocalDTO> findById(Long id) {
        return localRepository.findById(id)
                .map(this::toDTO);
    }

    public List<LocalDTO> findLocaisDisponiveisNaData(LocalDate data) {
        return localRepository.findLocaisDisponiveisNaData(data).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LocalDTO create(LocalDTO localDTO) {
        Local local = toEntity(localDTO);
        Local savedLocal = localRepository.save(local);
        
        // Salvar ferramentas associadas
        if (localDTO.getFerramentas() != null) {
            for (FerramentaLocalDTO ferramentaDTO : localDTO.getFerramentas()) {
                Optional<Ferramenta> ferramentaOpt = ferramentaRepository.findById(ferramentaDTO.getFerramentaId());
                if (ferramentaOpt.isPresent()) {
                    LocalFerramenta localFerramenta = new LocalFerramenta();
                    localFerramenta.setLocal(savedLocal);
                    localFerramenta.setFerramenta(ferramentaOpt.get());
                    localFerramenta.setQuantidade(ferramentaDTO.getQuantidade());
                    localFerramentaRepository.save(localFerramenta);
                }
            }
        }
        
        return toDTO(savedLocal);
    }

    @Transactional
    public Optional<LocalDTO> update(Long id, LocalDTO localDTO) {
        return localRepository.findById(id)
                .map(existingLocal -> {
                    updateEntityFromDTO(existingLocal, localDTO);
                    Local updatedLocal = localRepository.save(existingLocal);
                    
                    // Atualizar ferramentas
                    updateFerramentasDoLocal(updatedLocal, localDTO.getFerramentas());
                    
                    return toDTO(updatedLocal);
                });
    }

    public boolean delete(Long id) {
        if (localRepository.existsById(id)) {
            localRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean isDataDisponivel(Long localId, LocalDate data) {
        Optional<Local> localOpt = localRepository.findById(localId);
        if (localOpt.isEmpty()) {
            return false;
        }
        
        Local local = localOpt.get();
        return !data.isBefore(local.getDataInicio()) && 
               !data.isAfter(local.getDataFinal()) &&
               !localRepository.existsInscricaoNaData(localId, data);
    }

    private LocalDTO toDTO(Local local) {
        LocalDTO dto = new LocalDTO();
        dto.setId(local.getId());
        dto.setNome(local.getNome());
        dto.setDescricao(local.getDescricao());
        dto.setImagemUrl(local.getImagemUrl());
        dto.setRua(local.getRua());
        dto.setNumero(local.getNumero());
        dto.setCep(local.getCep());
        dto.setDataInicio(local.getDataInicio());
        dto.setDataFinal(local.getDataFinal());
        
        // Carregar ferramentas
        List<FerramentaLocalDTO> ferramentas = localFerramentaRepository.findByLocalId(local.getId()).stream()
                .map(lf -> new FerramentaLocalDTO(
                    lf.getFerramenta().getId(),
                    lf.getFerramenta().getNome(),
                    lf.getQuantidade()
                ))
                .collect(Collectors.toList());
        dto.setFerramentas(ferramentas);
        
        return dto;
    }

    private Local toEntity(LocalDTO dto) {
        Local local = new Local();
        local.setNome(dto.getNome());
        local.setDescricao(dto.getDescricao());
        local.setImagemUrl(dto.getImagemUrl());
        local.setRua(dto.getRua());
        local.setNumero(dto.getNumero());
        local.setCep(dto.getCep());
        local.setDataInicio(dto.getDataInicio());
        local.setDataFinal(dto.getDataFinal());
        return local;
    }

    private void updateEntityFromDTO(Local local, LocalDTO dto) {
        if (dto.getNome() != null) {
            local.setNome(dto.getNome());
        }
        if (dto.getDescricao() != null) {
            local.setDescricao(dto.getDescricao());
        }
        if (dto.getImagemUrl() != null) {
            local.setImagemUrl(dto.getImagemUrl());
        }
        if (dto.getRua() != null) {
            local.setRua(dto.getRua());
        }
        if (dto.getNumero() != null) {
            local.setNumero(dto.getNumero());
        }
        if (dto.getCep() != null) {
            local.setCep(dto.getCep());
        }
        if (dto.getDataInicio() != null) {
            local.setDataInicio(dto.getDataInicio());
        }
        if (dto.getDataFinal() != null) {
            local.setDataFinal(dto.getDataFinal());
        }
    }

    private void updateFerramentasDoLocal(Local local, List<FerramentaLocalDTO> ferramentasDTO) {
        // Remover ferramentas existentes
        List<LocalFerramenta> ferramentasExistentes = localFerramentaRepository.findByLocalId(local.getId());
        localFerramentaRepository.deleteAll(ferramentasExistentes);
        
        // Adicionar novas ferramentas
        if (ferramentasDTO != null) {
            for (FerramentaLocalDTO ferramentaDTO : ferramentasDTO) {
                Optional<Ferramenta> ferramentaOpt = ferramentaRepository.findById(ferramentaDTO.getFerramentaId());
                if (ferramentaOpt.isPresent()) {
                    LocalFerramenta localFerramenta = new LocalFerramenta();
                    localFerramenta.setLocal(local);
                    localFerramenta.setFerramenta(ferramentaOpt.get());
                    localFerramenta.setQuantidade(ferramentaDTO.getQuantidade());
                    localFerramentaRepository.save(localFerramenta);
                }
            }
        }
    }
}