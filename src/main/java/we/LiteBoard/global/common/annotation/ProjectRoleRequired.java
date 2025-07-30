package we.LiteBoard.global.common.annotation;

import we.LiteBoard.domain.memberProject.enumerate.ProjectRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProjectRoleRequired {
    ProjectRole value();
}
