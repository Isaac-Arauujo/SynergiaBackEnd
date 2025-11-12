package Synergia_PI.SynergiaBlog.DTOs;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class InscricaoRequestDTO {
    @NotNull(message = "Local é obrigatório")
    private Long localId;
    
    @Future(message = "Data desejada deve ser no futuro")
    private LocalDate dataDesejada;
    
    // Construtores
    public InscricaoRequestDTO() {}
    
    public InscricaoRequestDTO(Long localId, LocalDate dataDesejada) {
        this.localId = localId;
        this.dataDesejada = dataDesejada;
    }
    
    // Getters e Setters
    public Long getLocalId() { return localId; }
    public void setLocalId(Long localId) { this.localId = localId; }
    
    public LocalDate getDataDesejada() { return dataDesejada; }
    public void setDataDesejada(LocalDate dataDesejada) { this.dataDesejada = dataDesejada; }
}