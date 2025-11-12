package Synergia_PI.SynergiaBlog.DTOs;


import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocalDTO {
    private Long id;
    
    @NotBlank(message = "Nome do local é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
    
    @NotBlank(message = "Imagem é obrigatória")
    private String imagemUrl;
    
    @NotBlank(message = "Rua é obrigatória")
    private String rua;
    
    @NotBlank(message = "Número é obrigatório")
    private String numero;
    
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{8}", message = "CEP deve conter 8 dígitos")
    private String cep;
    
    @Future(message = "Data de início deve ser no futuro")
    private LocalDate dataInicio;
    
    @Future(message = "Data final deve ser no futuro")
    private LocalDate dataFinal;
    
    private List<FerramentaLocalDTO> ferramentas = new ArrayList<>();
    
    // Construtores
    public LocalDTO() {}
    
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
    
    public List<FerramentaLocalDTO> getFerramentas() { return ferramentas; }
    public void setFerramentas(List<FerramentaLocalDTO> ferramentas) { this.ferramentas = ferramentas; }
}