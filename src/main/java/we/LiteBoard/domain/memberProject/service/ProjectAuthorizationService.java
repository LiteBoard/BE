package we.LiteBoard.domain.memberProject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import we.LiteBoard.domain.memberProject.enumerate.ProjectRole;
import we.LiteBoard.domain.memberProject.repository.MemberProjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectAuthorizationService {

    private final MemberProjectRepository memberProjectRepository;

    public boolean hasProjectRole(Long memberId, Long projectId, ProjectRole required) {
        return memberProjectRepository.getMemberProjectById(projectId, memberId)
                .map(mp -> isHigherOrEqual(mp.getProjectRole(), required))
                .orElse(false);
    }

    private boolean isHigherOrEqual(ProjectRole actual, ProjectRole required) {
        List<ProjectRole> hierarchy = List.of(ProjectRole.VIEWER, ProjectRole.EDITOR, ProjectRole.ADMIN);
        return hierarchy.indexOf(actual) >= hierarchy.indexOf(required);
    }
}

