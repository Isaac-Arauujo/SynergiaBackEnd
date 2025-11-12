package Synergia_PI.SynergiaBlog.Services;



import Synergia_PI.SynergiaBlog.DTOs.FerramentaDTO;
import Synergia_PI.SynergiaBlog.Entidades.Ferramenta;
import Synergia_PI.SynergiaBlog.Interfaces.Repositories.FerramentaRepository;
import Synergia_PI.SynergiaBlog.Interfaces.Repositories.LocalFerramentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FerramentaService {

    @Autowired
    private FerramentaRepository ferramentaRepository;

    @Autowired
    private LocalFerramentaRepository localFerramentaRepository;

    public List<FerramentaDTO> findAll() {
        return ferramentaRepository.findByOrderByNomeAsc().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<FerramentaDTO> findDisponiveis() {
        return ferramentaRepository.findFerramentasDisponiveis().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<FerramentaDTO> findById(Long id) {
        return ferramentaRepository.findById(id)
                .map(this::toDTO);
    }

    public FerramentaDTO create(FerramentaDTO ferramentaDTO) {
        Ferramenta ferramenta = toEntity(ferramentaDTO);
        Ferramenta savedFerramenta = ferramentaRepository.save(ferramenta);
        return toDTO(savedFerramenta);
    }

    public Optional<FerramentaDTO> update(Long id, FerramentaDTO ferramentaDTO) {
        return ferramentaRepository.findById(id)
                .map(existingFerramenta -> {
                    updateEntityFromDTO(existingFerramenta, ferramentaDTO);
                    Ferramenta updatedFerramenta = ferramentaRepository.save(existingFerramenta);
                    return toDTO(updatedFerramenta);
                });
    }

    public boolean delete(Long id) {
        if (ferramentaRepository.existsById(id)) {
            ferramentaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean isDisponivel(Long ferramentaId, Integer quantidadeRequerida) {
        Optional<Ferramenta> ferramentaOpt = ferramentaRepository.findById(ferramentaId);
        if (ferramentaOpt.isEmpty()) {
            return false;
        }
        
        Ferramenta ferramenta = ferramentaOpt.get();
        Integer quantidadeAlocada = localFerramentaRepository.findQuantidadeTotalAlocada(ferramentaId);
        if (quantidadeAlocada == null) {
            quantidadeAlocada = 0;
        }
        
        Integer quantidadeDisponivel = ferramenta.getQuantidade() - quantidadeAlocada;
        return quantidadeDisponivel >= quantidadeRequerida;
    }

    private FerramentaDTO toDTO(Ferramenta ferramenta) {
        FerramentaDTO dto = new FerramentaDTO();
        dto.setId(ferramenta.getId());
        dto.setNome(ferramenta.getNome());
        dto.setDescricao(ferramenta.getDescricao());
        dto.setImagemUrl(ferramenta.getImagemUrl());
        dto.setQuantidade(ferramenta.getQuantidade());
        
        // Calcular quantidade disponível
        Integer quantidadeAlocada = localFerramentaRepository.findQuantidadeTotalAlocada(ferramenta.getId());
        if (quantidadeAlocada == null) {
            quantidadeAlocada = 0;
        }
        dto.setQuantidadeDisponivel(ferramenta.getQuantidade() - quantidadeAlocada);
        
        return dto;
    }

    private Ferramenta toEntity(FerramentaDTO dto) {
        Ferramenta ferramenta = new Ferramenta();
        ferramenta.setNome(dto.getNome());
        ferramenta.setDescricao(dto.getDescricao());
        ferramenta.setImagemUrl(dto.getImagemUrl());
        ferramenta.setQuantidade(dto.getQuantidade());
        return ferramenta;
    }

    private void updateEntityFromDTO(Ferramenta ferramenta, FerramentaDTO dto) {
        if (dto.getNome() != null) {
            ferramenta.setNome(dto.getNome());
        }
        if (dto.getDescricao() != null) {
            ferramenta.setDescricao(dto.getDescricao());
        }
        if (dto.getImagemUrl() != null) {
            ferramenta.setImagemUrl(dto.getImagemUrl());
        }
        if (dto.getQuantidade() != null) {
            ferramenta.setQuantidade(dto.getQuantidade());
        }
    }
}