package Synergia_PI.SynergiaBlog.DTOs;



import Synergia_PI.SynergiaBlog.Entidades.Inscricao.StatusInscricao;
import jakarta.validation.constraints.Future;
import java.time.LocalDate;

public class InscricaoDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNome;
    private String usuarioEmail;
    private Integer usuarioIdade;
    private String usuarioFoto;
    private Long localId;
    private String localNome;
    
    @Future(message = "Data desejada deve ser no futuro")
    private LocalDate dataDesejada;
    
    private StatusInscricao status;
    
    // Construtores
    public InscricaoDTO() {}
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }
    
    public String getUsuarioEmail() { return usuarioEmail; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
    
    public Integer getUsuarioIdade() { return usuarioIdade; }
    public void setUsuarioIdade(Integer usuarioIdade) { this.usuarioIdade = usuarioIdade; }
    
    public String getUsuarioFoto() { return usuarioFoto; }
    public void setUsuarioFoto(String usuarioFoto) { this.usuarioFoto = usuarioFoto; }
    
    public Long getLocalId() { return localId; }
    public void setLocalId(Long localId) { this.localId = localId; }
    
    public String getLocalNome() { return localNome; }
    public void setLocalNome(String localNome) { this.localNome = localNome; }
    
    public LocalDate getDataDesejada() { return dataDesejada; }
    public void setDataDesejada(LocalDate dataDesejada) { this.dataDesejada = dataDesejada; }
    
    public StatusInscricao getStatus() { return status; }
    public void setStatus(StatusInscricao status) { this.status = status; }
}