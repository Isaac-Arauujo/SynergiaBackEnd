package Synergia_PI.SynergiaBlog.Entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locais")
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome do local é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    private String nome;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;
    
    @NotBlank(message = "Imagem é obrigatória")
    @Column(name = "imagem_url", nullable = false)
    private String imagemUrl;
    
    @NotBlank(message = "Rua é obrigatória")
    @Column(nullable = false)
    private String rua;
    
    @NotBlank(message = "Número é obrigatório")
    @Column(nullable = false)
    private String numero;
    
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos")
    @Column(nullable = false)
    private String cep;
    
    @Future(message = "Data de início deve ser no futuro")
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;
    
    @Future(message = "Data final deve ser no futuro")
    @Column(name = "data_final", nullable = false)
    private LocalDate dataFinal;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL)
    private List<Inscricao> inscricoes = new ArrayList<>();
    
    @OneToMany(mappedBy = "local", cascade = CascadeType.ALL)
    private List<LocalFerramenta> ferramentas = new ArrayList<>();
    
    // Construtores
    public Local() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getImagemUrl() { return imagemUrl; }
    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }
    
    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }
    
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    
    public LocalDate getDataFinal() { return dataFinal; }
    public void setDataFinal(LocalDate dataFinal) { this.dataFinal = dataFinal; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public List<Inscricao> getInscricoes() { return inscricoes; }
    public void setInscricoes(List<Inscricao> inscricoes) { this.inscricoes = inscricoes; }
    
    public List<LocalFerramenta> getFerramentas() { return ferramentas; }
    public void setFerramentas(List<LocalFerramenta> ferramentas) { this.ferramentas = ferramentas; }
}