package Synergia_PI.SynergiaBlog.Validators;



import Synergia_PI.SynergiaBlog.Interfaces.Repositories.UsuarioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CpfUnicoValidator implements ConstraintValidator<CpfUnico, String> {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null) {
            return true; // Deixa @NotBlank tratar
        }
        return !usuarioRepository.existsByCpf(cpf);
    }
}