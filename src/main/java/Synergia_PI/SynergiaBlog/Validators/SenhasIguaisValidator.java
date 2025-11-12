package Synergia_PI.SynergiaBlog.Validators;



import Synergia_PI.SynergiaBlog.DTOs.UsuarioDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhasIguaisValidator implements ConstraintValidator<SenhasIguais, UsuarioDTO> {

    @Override
    public boolean isValid(UsuarioDTO usuario, ConstraintValidatorContext context) {
        if (usuario.getSenha() == null || usuario.getConfirmacaoSenha() == null) {
            return false;
        }
        return usuario.getSenha().equals(usuario.getConfirmacaoSenha());
    }
}