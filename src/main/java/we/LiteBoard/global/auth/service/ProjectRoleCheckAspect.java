package we.LiteBoard.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import we.LiteBoard.domain.memberProject.enumerate.ProjectRole;
import we.LiteBoard.domain.memberProject.service.ProjectAuthorizationService;
import we.LiteBoard.global.auth.OAuth2.CustomOAuth2User;
import we.LiteBoard.global.common.annotation.ProjectRoleRequired;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

@Aspect
@Component
@RequiredArgsConstructor
public class ProjectRoleCheckAspect {

    private final ProjectAuthorizationService authorizationService;

    @Before("@annotation(projectRoleRequired)")
    public void checkPermission(JoinPoint joinPoint, ProjectRoleRequired projectRoleRequired) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();  // 파라미터 이름들
        Object[] args = joinPoint.getArgs();                 // 실제 인자들

        Long projectId = null;

        for (int i = 0; i < paramNames.length; i++) {
            if ("projectId".equals(paramNames[i])) {
                projectId = (Long) args[i];
                break;
            }
        }

        if (projectId == null) {
            throw new CustomException(ErrorCode.AUTH_PARAMETER_VALIDATION_ERROR);
        }

        Long memberId = getCurrentMemberId();
        ProjectRole requiredRole = projectRoleRequired.value();

        boolean allowed = authorizationService.hasProjectRole(memberId, projectId, requiredRole);
        if (!allowed) {
            throw new CustomException(ErrorCode.USER_FORBIDDEN);
        }
    }


    private Long getCurrentMemberId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Object principal = auth.getPrincipal();
        if (!(principal instanceof CustomOAuth2User user)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return user.getUserDTO().getId();
    }


//    private Long getCurrentMemberId() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        CustomOAuth2User customUser = (CustomOAuth2User) auth.getPrincipal();
//        return customUser.getUserDTO().getId();
//    }

}
