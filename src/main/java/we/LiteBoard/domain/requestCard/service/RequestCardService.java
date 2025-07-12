package we.LiteBoard.domain.requestCard.service;

import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.requestCard.dto.RequestCardRequestDTO;
import we.LiteBoard.domain.requestCard.dto.RequestCardResponseDTO;

import java.util.List;

public interface RequestCardService {
    RequestCardResponseDTO.Upsert create(Member currentMember, Long taskId, RequestCardRequestDTO.Create request);
    RequestCardResponseDTO.Upsert update(Long requestCardId, RequestCardRequestDTO.Update request);
    List<RequestCardResponseDTO.Detail> getAllByTaskId(Long taskId);
    void deleteById(Long requestCardId);
}
