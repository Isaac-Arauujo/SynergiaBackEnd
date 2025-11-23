package Synergia_PI.SynergiaBlog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.Year;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    // Método simples para email texto
    public void enviarEmailSimples(String para, String assunto, String texto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(para);
            message.setSubject(assunto);
            message.setText(texto);
            message.setFrom("noreply@synergia.org");
            
            mailSender.send(message);
            System.out.println("✅ Email enviado com sucesso para: " + para);
        } catch (Exception e) {
            System.out.println("❌ Erro ao enviar email: " + e.getMessage());
        }
    }

    // Método para email HTML
    public void enviarEmailHtml(String para, String assunto, String templateName, Context context) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            String htmlContent = templateEngine.process(templateName, context);
            
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(htmlContent, true);
            helper.setFrom("noreply@synergia.org");
            
            mailSender.send(message);
            System.out.println("✅ Email HTML enviado com sucesso para: " + para);
        } catch (MessagingException e) {
            System.out.println("❌ Erro ao enviar email HTML: " + e.getMessage());
        }
    }

    // Método específico para confirmação de inscrição
    public void enviarEmailConfirmacaoInscricao(String emailVoluntario, String nomeVoluntario, 
                                               String nomeLocal, String dataDesejada) {
        try {
            Context context = new Context();
            context.setVariable("nomeVoluntario", nomeVoluntario);
            context.setVariable("nomeLocal", nomeLocal);
            context.setVariable("dataDesejada", dataDesejada);
            context.setVariable("anoAtual", Year.now().getValue());
            
            enviarEmailHtml(emailVoluntario, 
                "Confirmação de Inscrição - Synergia", 
                "email-confirmacao-inscricao", 
                context);
                
        } catch (Exception e) {
            // Fallback para email simples se o HTML falhar
            String texto = String.format(
                "Olá %s!\n\nSua inscrição para o local '%s' na data %s foi realizada com sucesso!\n\n" +
                "Aguarde a confirmação da ONG.\n\n" +
                "Atenciosamente,\nEquipe Synergia",
                nomeVoluntario, nomeLocal, dataDesejada
            );
            enviarEmailSimples(emailVoluntario, "Confirmação de Inscrição - Synergia", texto);
        }
    }

    // Método para quando a inscrição é confirmada pela ONG
    public void enviarEmailInscricaoConfirmada(String emailVoluntario, String nomeVoluntario, 
                                              String nomeLocal, String dataDesejada) {
        try {
            Context context = new Context();
            context.setVariable("nomeVoluntario", nomeVoluntario);
            context.setVariable("nomeLocal", nomeLocal);
            context.setVariable("dataDesejada", dataDesejada);
            context.setVariable("anoAtual", Year.now().getValue());
            
            enviarEmailHtml(emailVoluntario, 
                "Inscrição Confirmada - Synergia", 
                "email-inscricao-confirmada", 
                context);
        } catch (Exception e) {
            // Fallback para email simples
            String texto = String.format(
                "Olá %s!\n\nÓtima notícia! Sua inscrição para o local '%s' na data %s foi CONFIRMADA!\n\n" +
                "Estamos ansiosos para tê-lo conosco nesta ação voluntária.\n\n" +
                "Atenciosamente,\nEquipe Synergia",
                nomeVoluntario, nomeLocal, dataDesejada
            );
            enviarEmailSimples(emailVoluntario, "Inscrição Confirmada - Synergia", texto);
        }
    }
}