package we.LiteBoard.global.common.annotation.resolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.service.MemberService;
import we.LiteBoard.global.auth.jwt.util.JWTUtil;
import we.LiteBoard.global.common.annotation.CurrentMember;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticatedMemberResolver implements HandlerMethodArgumentResolver {

    private final JWTUtil jwtUtil;
    private final MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentMember.class) && parameter.getParameterType().isAssignableFrom(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String header = webRequest.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            log.warn("Authorization header is missing or malformed");
            return null;
        }

        String token = header.substring(7);
        String email = jwtUtil.getEmail(token);
        return memberService.findByEmail(email);
    }
}
