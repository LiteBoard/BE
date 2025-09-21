package we.LiteBoard.global.infra.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpMailSender implements MailSender {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailSenderAddress;

    @Override
    @Async("taskExecutor")
    public void send(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.setFrom(mailSenderAddress);

            mailSender.send(message);
            log.info("메일 전송 완료: {}", to);
        } catch (MessagingException e) {
            log.error("메일 전송 실패: {}", to, e);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAIL);
        }
    }
}
