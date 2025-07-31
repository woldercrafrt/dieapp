package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            logger.info("Intentando enviar correo a: " + to);
            
            // Enviar correo real
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("applicationsecure@gmail.com");
            
            logger.info("Configuración del mensaje completada, enviando...");
            mailSender.send(message);
            
            // Registrar en el log que se envió el correo
            logger.info("\n===================================================");
            logger.info("CORREO ENVIADO CORRECTAMENTE");
            logger.info("Para: " + to);
            logger.info("Asunto: " + subject);
            logger.info("===================================================");
        } catch (org.springframework.mail.MailAuthenticationException e) {
            logger.error("Error de autenticación SMTP: " + e.getMessage());
            logger.error("Verifica las credenciales de Gmail y que la autenticación de 2 factores esté configurada");
            throw new RuntimeException("Error de autenticación al enviar correo electrónico", e);
        } catch (org.springframework.mail.MailSendException e) {
            logger.error("Error al enviar correo SMTP: " + e.getMessage());
            logger.error("Verifica la configuración del servidor SMTP");
            throw new RuntimeException("Error al enviar correo electrónico", e);
        } catch (Exception e) {
            logger.error("Error general al enviar correo a " + to + ": " + e.getMessage(), e);
            logger.error("Tipo de excepción: " + e.getClass().getSimpleName());
            throw new RuntimeException("Error al enviar correo electrónico", e);
        }
    }

    public void sendVerificationCode(String to, String code) {
        String subject = "Código de verificación para iniciar sesión";
        String body = "Tu código de verificación es: " + code + ". Este código expirará en 5 minutos.";
        
        sendEmail(to, subject, body);
    }
}