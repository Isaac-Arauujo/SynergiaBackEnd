package Synergia_PI.SynergiaBlog.DTOs;



public class FerramentaLocalDTO {
    private Long ferramentaId;
    private String nomeFerramenta;
    private Integer quantidade;
    
    // Construtores
    public FerramentaLocalDTO() {}
    
    public FerramentaLocalDTO(Long ferramentaId, String nomeFerramenta, Integer quantidade) {
        this.ferramentaId = ferramentaId;
        this.nomeFerramenta = nomeFerramenta;
        this.quantidade = quantidade;
    }
    
    // Getters e Setters
    public Long getFerramentaId() { return ferramentaId; }
    public void setFerramentaId(Long ferramentaId) { this.ferramentaId = ferramentaId; }
    
    public String getNomeFerramenta() { return nomeFerramenta; }
    public void setNomeFerramenta(String nomeFerramenta) { this.nomeFerramenta = nomeFerramenta; }
    
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}