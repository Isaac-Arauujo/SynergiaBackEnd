package Synergia_PI.SynergiaBlog.Entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "local_ferramenta")
public class LocalFerramenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;
    
    @ManyToOne
    @JoinColumn(name = "ferramenta_id", nullable = false)
    private Ferramenta ferramenta;
    
    @Column(nullable = false)
    private Integer quantidade;
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Local getLocal() { return local; }
    public void setLocal(Local local) { this.local = local; }
    
    public Ferramenta getFerramenta() { return ferramenta; }
    public void setFerramenta(Ferramenta ferramenta) { this.ferramenta = ferramenta; }
    
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}