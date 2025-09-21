package we.LiteBoard.domain.memberProject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.member.dto.MemberResponseDTO;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.repository.MemberRepository;
import we.LiteBoard.domain.memberProject.dto.MemberProjectRequestDTO;
import we.LiteBoard.domain.memberProject.entity.MemberProject;
import we.LiteBoard.domain.memberProject.repository.MemberProjectRepository;
import we.LiteBoard.domain.project.entity.Project;
import we.LiteBoard.domain.project.repository.ProjectRepository;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberProjectServiceImpl implements MemberProjectService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponseDTO.ProjectMemberResponse> getProjectMembers(Long projectId) {
        List<MemberProject> memberProjects = memberProjectRepository.findAllByProjectId(projectId);
        return memberProjects.stream()
                .map(MemberResponseDTO.ProjectMemberResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public void addMemberToProject(Long projectId, MemberProjectRequestDTO.AddMember request) {
        Project project = projectRepository.getById(projectId);
        Member member = memberRepository.getById(request.memberId());

        if (memberProjectRepository.existsByProjectAndMember(project.getId(), member.getId())) {
            throw new CustomException(ErrorCode.MEMBER_ALREADY_IN_PROJECT);
        }

        MemberProject memberProject = MemberProject.builder()
                .projectRole(request.projectRole())
                .build();

        project.addMemberProject(memberProject);
        member.addMemberProject(memberProject);
    }

    @Override
    @Transactional
    public void changeMemberRole(Long projectId, MemberProjectRequestDTO.ChangeRole request) {
        MemberProject memberProject = memberProjectRepository.getMemberProjectById(projectId, request.memberId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND_IN_PROJECT));

        memberProject.changeProjectRole(request.newRole());
    }

    @Override
    @Transactional
    public void removeMemberFromProject(Long projectId, Long memberId) {
        MemberProject memberProject = memberProjectRepository.getMemberProjectById(projectId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND_IN_PROJECT));

        memberProjectRepository.delete(memberProject);
    }

}
