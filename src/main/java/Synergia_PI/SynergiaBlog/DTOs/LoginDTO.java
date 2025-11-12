package Synergia_PI.SynergiaBlog.DTOs;


import jakarta.validation.constraints.*;

public class LoginDTO {
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
    
    // Construtores
    public LoginDTO() {}
    
    public LoginDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }
    
    // Getters e Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}