package we.LiteBoard.global.auth.OAuth2.dto;

/**
 * Google 외 소셜 로그인에 대해 확장성 확보를 위한 인터페이스
 */
public interface OAuth2Response {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
