package TTCS.TTCS_ThayPhuong.Service;

import TTCS.TTCS_ThayPhuong.Dto.Request.UserCreateRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.UserResponse;
import TTCS.TTCS_ThayPhuong.Entity.User;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    String extractEmail(String accessToken) throws ParseException;
    boolean verificationToken(String token,User user) throws ParseException, JOSEException;
    long extractTokenExpired(String token);
}
