package dev.sudhanshu.auth_n_authz.services.notification.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.sudhanshu.auth_n_authz.libs.exceptions.http.HttpInternalServerException;
import dev.sudhanshu.auth_n_authz.libs.mailer.Mailer;
import dev.sudhanshu.auth_n_authz.libs.mailer.exceptions.EmailSendFailedException;
import dev.sudhanshu.auth_n_authz.services.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailNotificationService implements NotificationService<SendEmailCommand> {

    @Autowired
    Mailer mailer;

    @Override
    public boolean send(SendEmailCommand command) {
        try {
            mailer.sendEmail(
                command.recipientEmail(),  
                command.subject(),
                command.body()
            );
        } catch (EmailSendFailedException e) {
            log.error(e.getMessage());
            throw new HttpInternalServerException();
        }

        return true;
    }

}
