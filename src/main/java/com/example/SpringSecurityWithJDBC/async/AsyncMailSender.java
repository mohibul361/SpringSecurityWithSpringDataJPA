package com.example.SpringSecurityWithJDBC.async;

import com.example.SpringSecurityWithJDBC.Utils.Utils;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Slf4j
public class AsyncMailSender {

    private final JavaMailSender mailSender;

    public AsyncMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String email, String userName, String password, String memberName) throws Exception {

        log.info("Current thread pool name: " + Thread.currentThread().getName());

        try {
            InternetAddress internetAddress = new InternetAddress(email);

            internetAddress.validate();

            String body = Utils.getEmailTemplate(memberName, userName, password, "http://kwfbd.com/login");

            String subject = "Account created: Your login credentials!";

            System.out.println("subject : " + subject);

            Properties props = ((JavaMailSenderImpl) mailSender).getJavaMailProperties();

            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");

            Session session = Session.getDefaultInstance(props);

            System.out.println("session : " + session);

            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom("mohibul323@gmail.com");
            mimeMessage.reply(false);
            mimeMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(email));
            mimeMessage.setText(body, "UTF-8", "html");
            mimeMessage.setSubject(subject);
            mailSender.send(mimeMessage);
        } catch (AddressException e) {

            log.info("Invalid email address: " + e.getMessage());
            throw new BadCredentialsException("Invalid email address");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Sending email failed to: {}", email);
            throw new BadCredentialsException("Sending email failed");
        }
    }

}
