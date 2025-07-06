package we.LiteBoard.domain.memberProject.repository;

public interface MemberProjectRepository {
    boolean existsByProjectAndMember(Long projectId, Long memberIdLong);
}
