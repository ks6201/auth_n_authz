package dev.sudhanshu.auth_n_authz.libs.mailer;

import dev.sudhanshu.auth_n_authz.libs.mailer.exceptions.EmailSendFailedException;

public interface Mailer {
    void sendEmail(
        String recipientEmail,
        String subject,
        String body
    ) throws EmailSendFailedException;
}
