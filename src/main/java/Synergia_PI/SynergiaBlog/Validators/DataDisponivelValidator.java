package Synergia_PI.SynergiaBlog.Validators;



import Synergia_PI.SynergiaBlog.DTOs.InscricaoRequestDTO;
import Synergia_PI.SynergiaBlog.Entidades.Local;
import Synergia_PI.SynergiaBlog.Interfaces.Repositories.LocalRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

public class DataDisponivelValidator implements ConstraintValidator<DataDisponivel, InscricaoRequestDTO> {

    @Autowired
    private LocalRepository localRepository;

    @Override
    public boolean isValid(InscricaoRequestDTO inscricao, ConstraintValidatorContext context) {
        if (inscricao.getLocalId() == null || inscricao.getDataDesejada() == null) {
            return false;
        }
        
        Optional<Local> localOpt = localRepository.findById(inscricao.getLocalId());
        if (localOpt.isEmpty()) {
            return false;
        }
        
        Local local = localOpt.get();
        return !inscricao.getDataDesejada().isBefore(local.getDataInicio()) &&
               !inscricao.getDataDesejada().isAfter(local.getDataFinal());
    }
}