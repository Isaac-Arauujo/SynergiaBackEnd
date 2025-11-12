package Synergia_PI.SynergiaBlog.Entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inscricoes")
public class Inscricao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;
    
    @Future(message = "Data desejada deve ser no futuro")
    @Column(name = "data_desejada", nullable = false)
    private LocalDate dataDesejada;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusInscricao status = StatusInscricao.PENDENTE;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Construtores
    public Inscricao() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Local getLocal() { return local; }
    public void setLocal(Local local) { this.local = local; }
    
    public LocalDate getDataDesejada() { return dataDesejada; }
    public void setDataDesejada(LocalDate dataDesejada) { this.dataDesejada = dataDesejada; }
    
    public StatusInscricao getStatus() { return status; }
    public void setStatus(StatusInscricao status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    
    
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

public enum StatusInscricao {
    PENDENTE, CONFIRMADA, RECUSADA
}


}



