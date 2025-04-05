package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.IntrospectRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.LogInRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.LogOutRequest;
import TTCS.TTCS_ThayPhuong.Dto.Request.UserCreateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.IntrospectResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.RefreshTokenResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.SignInResponse;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import TTCS.TTCS_ThayPhuong.Entity.User;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;

import java.text.ParseException;

public interface AuthenticationService {
    UserResponse register(UserCreateRequest request);
    SignInResponse logIn(LogInRequest request, HttpServletResponse response);
    void logOut(LogOutRequest request, HttpServletResponse response) throws ParseException, JOSEException;
    RefreshTokenResponse refreshToken(@CookieValue(name = "refreshToken") String refreshToken) throws ParseException, JOSEException;
    String verifyEmail(String token);
    IntrospectResponse introspect(IntrospectRequest request) throws ParseException;
    SignInResponse loginWithGoogle(String code,HttpServletResponse response);
}
