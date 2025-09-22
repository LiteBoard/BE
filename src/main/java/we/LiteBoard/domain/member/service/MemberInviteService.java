package we.LiteBoard.domain.member.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.repository.MemberRepository;
import we.LiteBoard.domain.memberProject.entity.MemberProject;
import we.LiteBoard.domain.memberProject.enumerate.ProjectRole;
import we.LiteBoard.domain.memberProject.repository.MemberProjectRepository;
import we.LiteBoard.domain.project.entity.Project;
import we.LiteBoard.domain.project.repository.ProjectRepository;
import we.LiteBoard.global.auth.jwt.util.InviteJwtProvider;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;
import we.LiteBoard.global.infra.email.MailSender;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberInviteService {

    private final MailSender mailSender;
    private final ProjectRepository projectRepository;
    private final InviteJwtProvider inviteJwtProvider;
    private final TemplateEngine templateEngine;
    private final MemberProjectRepository memberProjectRepository;
    private final MemberRepository memberRepository;

    public void inviteBulk(List<String> emails, Long projectId, ProjectRole role, Member inviter) {
        Project project = projectRepository.getById(projectId);

        final String inviterName = inviter.getName();
        final String projectTitle = project.getTitle();

        // 공백/대소문자 정리 + 중복 제거
        Set<String> normalized = new LinkedHashSet<>();
        for (String raw : emails) {
            String e = Optional.ofNullable(raw).map(String::trim).orElse("");
            if (!e.isEmpty()) normalized.add(e.toLowerCase());
        }

        for (String email : normalized) {
            String token = inviteJwtProvider.createInviteToken(email, projectId, role);
            String inviteLink = "https://liteboard.site/api/v1/accept-invite?token=" + token;

            Context context = new Context();
            context.setVariable("inviterName", inviterName);
            context.setVariable("projectTitle", projectTitle);
            context.setVariable("inviteLink", inviteLink);

            String html = templateEngine.process("invite", context);
            mailSender.send(email, "[LiteBoard] 프로젝트 초대 메일", html);
        }
    }

    public void acceptInvite(String token, Long memberId) {
        Claims claims = inviteJwtProvider.parse(token);

        String tokenEmail = claims.getSubject();
        Long projectId = claims.get("projectId", Long.class);
        ProjectRole role = ProjectRole.valueOf(claims.get("role", String.class));

        Member member = memberRepository.getById(memberId);

        if (!tokenEmail.equals(member.getEmail())) {
            throw new CustomException(ErrorCode.INVITE_EMAIL_MISMATCH);
        }

        Project project = projectRepository.getById(projectId);

        // 중복 가입 방지
        if (memberProjectRepository.existsByProjectAndMember(memberId, project.getId())) {
            throw new CustomException(ErrorCode.MEMBER_ALREADY_IN_PROJECT);
        }

        MemberProject memberProject = MemberProject.builder()
                .member(member)
                .project(project)
                .projectRole(role)
                .build();
        project.addMemberProject(memberProject);
        member.addMemberProject(memberProject);
    }
}
