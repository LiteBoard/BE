package we.LiteBoard.global.auth.service;

public interface AuthService {
    String reissueAccessToken(String refreshToken);
}
