package dev.sudhanshu.auth_n_authz.services.notification;

public interface NotificationService<ServiceCommand> {
    boolean send(ServiceCommand command);
}
