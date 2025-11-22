package Synergia_PI.SynergiaBlog.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class AtualizarPerfilRequestDTO {
    
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nomeCompleto;
    
    private LocalDate dataNascimento;
    
    @Email(message = "Email deve ser válido")
    private String email;
    
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;
    
    private String confirmacaoSenha; // CAMPO ADICIONADO
    
    private String fotoPerfil;
    
    // Construtores
    public AtualizarPerfilRequestDTO() {}
    
    // Getters e Setters
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getConfirmacaoSenha() { return confirmacaoSenha; }
    public void setConfirmacaoSenha(String confirmacaoSenha) { this.confirmacaoSenha = confirmacaoSenha; }
    
    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
    
    @Override
    public String toString() {
        return "AtualizarPerfilRequestDTO{" +
                "nomeCompleto='" + nomeCompleto + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", email='" + email + '\'' +
                ", senha='" + (senha != null ? "***" : "null") + '\'' +
                ", confirmacaoSenha='" + (confirmacaoSenha != null ? "***" : "null") + '\'' +
                ", fotoPerfil='" + fotoPerfil + '\'' +
                '}';
    }
}