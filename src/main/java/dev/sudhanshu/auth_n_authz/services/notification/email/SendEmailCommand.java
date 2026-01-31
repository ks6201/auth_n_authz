package dev.sudhanshu.auth_n_authz.services.notification.email;

public record SendEmailCommand(
    String recipientEmail,
    String subject,
    String body
) {

}
