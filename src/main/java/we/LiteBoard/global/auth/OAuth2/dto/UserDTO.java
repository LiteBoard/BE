package we.LiteBoard.global.auth.OAuth2.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {

    private String role;
    private String name;
    private String email;
}
