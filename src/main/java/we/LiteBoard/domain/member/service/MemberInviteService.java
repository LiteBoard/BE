package we.LiteBoard.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.project.entity.Project;
import we.LiteBoard.domain.project.repository.ProjectRepository;
import we.LiteBoard.global.infra.email.MailSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberInviteService {

    private final MailSender mailSender;
    private final ProjectRepository projectRepository;
    private final TemplateEngine templateEngine;

    public void invite(String toEmail, Long projectId, Member inviter) {
        Project project = projectRepository.getById(projectId);
        String subject = "[LiteBoard] 프로젝트 초대 메일";

        String inviteLink = "http://localhost:8080/invite?projectId=" + project.getId();

        // 템플릿 렌더링
        Context context = new Context();
        context.setVariable("inviterName", inviter.getName());
        context.setVariable("projectTitle", project.getTitle());
        context.setVariable("inviteLink", inviteLink);

        String html = templateEngine.process("invite", context);
        mailSender.send(toEmail, subject, html);
    }
}
