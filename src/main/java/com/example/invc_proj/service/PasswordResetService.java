package com.example.invc_proj.service;


import com.example.invc_proj.model.PasswordResetToken;
import com.example.invc_proj.model.User;
import com.example.invc_proj.repository.PasswordResetTokenRepo;
import com.example.invc_proj.repository.UserRepo;
import com.example.invc_proj.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;



import java.time.LocalDateTime;
import java.util.UUID;
@Service
public class PasswordResetService {

    private final PasswordResetTokenRepo tokenRepository;
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public PasswordResetService(
                                PasswordResetTokenRepo tokenRepository,
                                UserRepo userRepository,
                                PasswordEncoder passwordEncoder,
                                EmailService emailService)
        {
            this.tokenRepository = tokenRepository;
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.emailService = emailService;
        }

    public void createAndSendResetToken(String email) {
        User user = userRepository.getEmailbyId(email)
                .orElseThrow(() -> new RuntimeException("Email not registered"));

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(30);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(expiry);

        tokenRepository.save(resetToken);
        emailService.sendResetEmail(user.getEmailId(), token);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken); // Cleanup after use
    }

}
