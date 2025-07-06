package we.LiteBoard.domain.memberProject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.repository.MemberRepository;
import we.LiteBoard.domain.memberProject.dto.MemberProjectRequestDTO;
import we.LiteBoard.domain.memberProject.entity.MemberProject;
import we.LiteBoard.domain.memberProject.repository.MemberProjectRepository;
import we.LiteBoard.domain.project.entity.Project;
import we.LiteBoard.domain.project.repository.ProjectRepository;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberProjectServiceImpl implements MemberProjectService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Override
    @Transactional
    public void addMemberToProject(Long projectId, MemberProjectRequestDTO.AddMember request) {
        Project project = projectRepository.getById(projectId);
        Member member = memberRepository.findById(request.memberId()).get();

        if (memberProjectRepository.existsByProjectAndMember(project.getId(), member.getId())) {
            throw new CustomException(ErrorCode.MEMBER_ALREADY_IN_PROJECT);
        }

        MemberProject memberProject = MemberProject.builder()
                .projectRole(request.projectRole())
                .build();

        project.addMemberProject(memberProject);
        member.addMemberProject(memberProject);
    }
}
