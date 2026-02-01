package dev.sudhanshu.auth_n_authz.libs.mailer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import dev.sudhanshu.auth_n_authz.libs.mailer.exceptions.EmailSendFailedException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SpringMailer implements Mailer {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendEmail(
        String recipientEmail, 
        String subject, 
        String body
    ) throws EmailSendFailedException {
        
        var message = new SimpleMailMessage();
        
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
        } catch(Exception e) {
            /**
             *  TODO: Handle these later
             * MailParseException - in case of failure when parsing the message
             * MailAuthenticationException - in case of authentication failure
             * MailSendException - in case of failure when sending the message
             * MailException
            */
           log.error(e.getMessage());
           throw new EmailSendFailedException();
        }
    }

}
