package com.example.demo.service;

import com.example.demo.entity.VerificationCode;
import com.example.demo.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class VerificationCodeService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationCodeService.class);
    
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;

    @Value("${auth.code.expiration}")
    private long codeExpirationMs;

    @Autowired
    public VerificationCodeService(VerificationCodeRepository verificationCodeRepository, EmailService emailService) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void generateAndSendCode(String email) {
        try {
            logger.info("Generando nuevo código de verificación para email: {}", email);
            
            // Generar un nuevo código de 6 dígitos
            String code = generateRandomCode();

            // Calcular la fecha de expiración (5 minutos desde ahora)
            LocalDateTime expiryDate = LocalDateTime.now().plusNanos(codeExpirationMs * 1000000);

            // Guardar el código en la base de datos
            VerificationCode verificationCode = new VerificationCode(email, code, expiryDate);
            verificationCodeRepository.save(verificationCode);
            logger.debug("Código guardado en base de datos para email: {}", email);

            // Enviar el código por correo electrónico
            emailService.sendVerificationCode(email, code);
            logger.info("Código de verificación enviado exitosamente para email: {}", email);
            
        } catch (TransactionSystemException e) {
            logger.error("Error de transacción JPA al generar código para email: {}", email, e);
            throw new RuntimeException("Error de transacción al generar código de verificación. Intente nuevamente.", e);
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al generar código para email: {}", email, e);
            throw new RuntimeException("Error de base de datos al generar código de verificación", e);
        } catch (Exception e) {
            logger.error("Error inesperado al generar y enviar código para email: {}", email, e);
            throw new RuntimeException("Error al generar código de verificación", e);
        }
    }

    @Transactional
    public boolean verifyCode(String email, String code) {
        try {
            Optional<VerificationCode> verificationCodeOpt = verificationCodeRepository.findByEmailAndCodeAndUsedFalse(email, code);

            if (verificationCodeOpt.isPresent()) {
                VerificationCode verificationCode = verificationCodeOpt.get();

                // Verificar si el código ha expirado
                if (verificationCode.isExpired()) {
                    logger.warn("Código expirado para email: {}", email);
                    return false;
                }

                // Marcar el código como usado
                verificationCode.setUsed(true);
                verificationCodeRepository.save(verificationCode);
                
                logger.info("Código verificado exitosamente para email: {}", email);
                return true;
            }
            
            logger.warn("Código no encontrado o ya usado para email: {}", email);
            return false;
            
        } catch (ObjectOptimisticLockingFailureException e) {
            logger.error("Error de concurrencia al verificar código para email: {}", email, e);
            throw new RuntimeException("Error de concurrencia al verificar código. Intente nuevamente.", e);
        } catch (TransactionSystemException e) {
            logger.error("Error de transacción JPA al verificar código para email: {}", email, e);
            throw new RuntimeException("Error de transacción al verificar código. Intente nuevamente.", e);
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al verificar código para email: {}", email, e);
            throw new RuntimeException("Error de base de datos al verificar código", e);
        }
    }

    private String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Genera un número entre 100000 y 999999
        return String.valueOf(code);
    }
}