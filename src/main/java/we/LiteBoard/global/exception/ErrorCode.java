package we.LiteBoard.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 에러코드 규약
     * HTTP Status Code는 에러에 가장 유사한 코드를 부여한다.
     * 사용자정의 에러코드는 중복되지 않게 배정한다.
     * 사용자정의 에러코드는 각 카테고리 이름과 숫자를 조합하여 명확성을 더한다.
     * =============================================================
     * 400 : 잘못된 요청
     * 401 : 인증되지 않은 요청
     * 403 : 권한의 문제가 있을때
     * 404 : 요청한 리소스가 존재하지 않음
     * 409 : 현재 데이터와 값이 충돌날 때(ex. 아이디 중복)
     * 412 : 파라미터 값이 뭔가 누락됐거나 잘못 왔을 때
     * 422 : 파라미터 문법 오류
     * 424 : 뭔가 단계가 꼬였을때, 1번안하고 2번하고 그런경우
     * =============================================================
     */


    // Common
    SERVER_UNTRACKED_ERROR("COMMON500", "미등록 서버 에러입니다. 서버 팀에 연락주세요.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST("COMMON400", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("COMMON401", "인증되지 않은 요청입니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("COMMON403", "권한이 부족합니다.", HttpStatus.FORBIDDEN),
    OBJECT_NOT_FOUND("COMMON404", "조회된 객체가 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_PARAMETER("COMMON422", "잘못된 파라미터입니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    PARAMETER_VALIDATION_ERROR("COMMON422", "파라미터 검증 에러입니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    PARAMETER_GRAMMAR_ERROR("COMMON422", "파라미터 문법 에러입니다.", HttpStatus.UNPROCESSABLE_ENTITY),
    AUTH_PARAMETER_VALIDATION_ERROR("COMMON422", "권한 검사를 하려 했지만 projectId가 파라미터에 없습니다.", HttpStatus.UNPROCESSABLE_ENTITY),

    // Token
    TOKEN_INVALID("TOKEN401", "유효하지 않은 Token 입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID_ROLE("TOKEN401", "JWT 토큰에 Role 정보가 없습니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_EXPIRED("TOKEN401", "Access Token 이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_INVALID("TOKEN401", "유효하지 않은 Access Token 입니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND("TOKEN404", "해당 사용자에 대한 Refresh Token 을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REFRESH_TOKEN_MISMATCH("TOKEN401", "Refresh Token 이 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED("TOKEN401", "Refresh Token 이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("TOKEN401", "유효하지 않은 Refresh Token 입니다.", HttpStatus.UNAUTHORIZED),

    // User (회원)
    USER_ALREADY_EXIST("USER400", "이미 회원가입된 유저입니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST("USER404", "존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND),
    USER_EMAIL_NOT_EXIST("USER404", "가입된 이메일이 존재하지 않습니다. 다시 입력해주세요.", HttpStatus.NOT_FOUND),
    USER_NOT_VALID("USER404", "유효한 사용자 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_WRONG_PASSWORD("USER401", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.", HttpStatus.UNAUTHORIZED),
    USER_SAME_PASSWORD("USER400", "동일한 비밀번호로 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),
    PASSWORDS_NOT_MATCH("PASSWORD401", "입력한 두 개의 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    USER_NO_PERMISSION("USER403", "권한이 없습니다.", HttpStatus.FORBIDDEN),
    USER_FORBIDDEN("USER403", "사용자의 권한이 부족합니다.", HttpStatus.FORBIDDEN),

    // Project (프로젝트)
    PROJECT_NOT_FOUND("PROJECT404", "프로젝트를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // MemberProject (프로젝트 멤버)
    MEMBER_ALREADY_IN_PROJECT("MEMBER_PROJECT_400", "멤버가 이미 프로젝트에 속해있습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_FOUND_IN_PROJECT("MEMBER_PROJECT_400", "프로젝트에서 해당 멤버를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),

    // Category (카테고리)
    CATEGORY_NOT_FOUND("CATEGORY404", "카테고리를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // Task (업무)
    TASK_NOT_FOUND("TASK404", "업무를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // TODOs (하위 업무)
    TODO_NOT_FOUND("TODO404", "TODO를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // Request Card (업무 요청)
    REQUEST_CARD_NOT_FOUND("REQUEST_CARD404", "업무 요청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // Request Card Todos (업무 요청 속 Todos)
    REQUEST_CARD_TODO_NOT_FOUND("REQUEST_CARD_TODO404", "업무 요청 속 Todo를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // REDIS
    REDIS_STORE_FAILED("REDIS500", "Redis 저장 중 오류 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    REDIS_READ_FAILED("REDIS500", "Redis 조회 중 오류 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    REDIS_DELETE_FAILED("REDIS500", "Redis 삭제 중 오류 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // EMAIL
    EMAIL_SEND_FAIL("EMAIL500", "메일 전송에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    TEMPLATE_NOT_LOAD("EMAIL500", "이메일 템플릿을 로드할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // FILE
    FILE_UPLOAD_FAIL("FILE500", "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_IO_ERROR("FILE500", "파일 처리 중 I/O 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_INVALID_URL("FILE400", "잘못된 이미지 URL 형식입니다", HttpStatus.BAD_REQUEST),
    FILE_DELETE_FAIL("FILE500", "파일 삭제에 실패하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorCode;
    private final String message;
    private final HttpStatus status;
}