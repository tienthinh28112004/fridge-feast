package TTCS.TTCS_ThayPhuong.Util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public class SecurityUtils {
    private SecurityUtils(){
    }
    public static Optional<String> getCurrentLogin(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if(authentication.getPrincipal() instanceof Jwt jwt){
            return Optional.ofNullable(jwt.getSubject());//lấy ra email
        }
        return Optional.empty();
    }
}
