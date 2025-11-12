package Synergia_PI.SynergiaBlog.Validators;



import Synergia_PI.SynergiaBlog.Interfaces.Repositories.UsuarioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailUnicoValidator implements ConstraintValidator<EmailUnico, String> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true; // Deixa @NotBlank tratar
        }
        return !usuarioRepository.existsByEmail(email);
    }
}
