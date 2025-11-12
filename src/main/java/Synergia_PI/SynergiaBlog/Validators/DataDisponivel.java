package Synergia_PI.SynergiaBlog.Validators;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DataDisponivelValidator.class)
public @interface DataDisponivel {
    String message() default "A data selecionada não está disponível para este local";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}