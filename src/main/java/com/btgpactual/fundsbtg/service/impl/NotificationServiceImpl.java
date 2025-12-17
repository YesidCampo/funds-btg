package com.btgpactual.fundsbtg.service.impl;

import com.btgpactual.fundsbtg.common.constants.AppConstants;
import com.btgpactual.fundsbtg.common.constants.NotificationType;
import com.btgpactual.fundsbtg.model.Fund;
import com.btgpactual.fundsbtg.model.Subscription;
import com.btgpactual.fundsbtg.model.User;
import com.btgpactual.fundsbtg.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    
    @Value("${spring.mail.username:noreply@btgpactual.com}")
    private String fromEmail;
    
    public NotificationServiceImpl(JavaMailSender mailSender, 
                                  SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    
    @Override
    public Mono<Void> sendSubscriptionNotification(User user, Fund fund, Subscription subscription) {
        logger.info("Enviando notificación de suscripción a usuario: {}", user.getEmail());
        
        if (user.getPreferredNotification() == NotificationType.EMAIL) {
            return sendSubscriptionEmail(user, fund, subscription);
        } else if (user.getPreferredNotification() == NotificationType.SMS) {
            return sendSubscriptionSMS(user, fund, subscription);
        }
        
        return Mono.empty();
    }
    
    @Override
    public Mono<Void> sendCancellationNotification(User user, Fund fund, Subscription subscription) {
        logger.info("Enviando notificación de cancelación a usuario: {}", user.getEmail());
        
        if (user.getPreferredNotification() == NotificationType.EMAIL) {
            return sendCancellationEmail(user, fund, subscription);
        } else if (user.getPreferredNotification() == NotificationType.SMS) {
            return sendCancellationSMS(user, fund, subscription);
        }
        
        return Mono.empty();
    }
    
    private Mono<Void> sendSubscriptionEmail(User user, Fund fund, Subscription subscription) {
        return Mono.fromRunnable(() -> {
            try {
                Map<String, Object> variables = new HashMap<>();
                variables.put("userName", user.getUserName());
                variables.put("fundName", fund.getPrettyName());
                variables.put("investmentAmount", subscription.getInvestmentAmount());
                variables.put("currency", fund.getCurrency());
                variables.put("subscriptionDate", subscription.getSubscriptionDate().format(DATE_FORMATTER));
                variables.put("currentBalance", user.getBalance());
                
                String htmlContent = processTemplate(AppConstants.EMAIL_TEMPLATE_SUBSCRIPTION, variables);
                
                sendEmail(user.getEmail(), AppConstants.EMAIL_SUBJECT_SUBSCRIPTION, htmlContent);
                
                logger.info("Email de suscripción enviado exitosamente a: {}", user.getEmail());
            } catch (Exception e) {
                logger.error("Error al enviar email de suscripción: {}", e.getMessage());
                throw new RuntimeException("Error al enviar email", e);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
    
    private Mono<Void> sendCancellationEmail(User user, Fund fund, Subscription subscription) {
        return Mono.fromRunnable(() -> {
            try {
                Map<String, Object> variables = new HashMap<>();
                variables.put("userName", user.getUserName());
                variables.put("fundName", fund.getPrettyName());
                variables.put("investmentAmount", subscription.getInvestmentAmount());
                variables.put("currency", fund.getCurrency());
                variables.put("cancellationDate", subscription.getCancellationDate().format(DATE_FORMATTER));
                variables.put("currentBalance", user.getBalance());
                
                String htmlContent = processTemplate(AppConstants.EMAIL_TEMPLATE_CANCELLATION, variables);
                
                sendEmail(user.getEmail(), AppConstants.EMAIL_SUBJECT_CANCELLATION, htmlContent);
                
                logger.info("Email de cancelación enviado exitosamente a: {}", user.getEmail());
            } catch (Exception e) {
                logger.error("Error al enviar email de cancelación: {}", e.getMessage());
                throw new RuntimeException("Error al enviar email", e);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
    
    private Mono<Void> sendSubscriptionSMS(User user, Fund fund, Subscription subscription) {
        return Mono.fromRunnable(() -> {
            String message = String.format(
                    "BTG Pactual: Se ha suscrito exitosamente al fondo %s por %s %s. Nuevo saldo: %s %s",
                    fund.getPrettyName(),
                    fund.getCurrency(),
                    subscription.getInvestmentAmount(),
                    fund.getCurrency(),
                    user.getBalance()
            );
            
            logger.info("SMS enviado a {}: {}", user.getPhoneNumber(), message);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
    
    private Mono<Void> sendCancellationSMS(User user, Fund fund, Subscription subscription) {
        return Mono.fromRunnable(() -> {
            String message = String.format(
                    "BTG Pactual: Se ha cancelado su suscripción al fondo %s. Monto devuelto: %s %s. Nuevo saldo: %s %s",
                    fund.getPrettyName(),
                    fund.getCurrency(),
                    subscription.getInvestmentAmount(),
                    fund.getCurrency(),
                    user.getBalance()
            );
            
            logger.info("SMS enviado a {}: {}", user.getPhoneNumber(), message);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
    
    private String processTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }
    
    private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        
        mailSender.send(message);
    }
}
