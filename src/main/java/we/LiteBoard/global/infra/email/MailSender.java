package we.LiteBoard.global.infra.email;

public interface MailSender {
    void send(String to, String subject, String body);
}
