package TTCS.TTCS_ThayPhuong.Configuration;

import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import TTCS.TTCS_ThayPhuong.Service.JwtService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${app.secret}")
    private String appSecret;
    private NimbusJwtDecoder nimbusJwtDecoder=null;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Override
    public Jwt decode(String token) throws JwtException {

        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec secretKeySpec = new SecretKeySpec(appSecret.getBytes(),"HS512");//thuật toán mã hóa
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec) //truyền secretKeySpec vào
                    .macAlgorithm(MacAlgorithm.HS512) //thuật toán mã hóa
                    .build();
        }

        try {
            String email = jwtService.extractEmail(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(()->new NotFoundException("User not found"));
            if(jwtService.verificationToken(token,user)){
                nimbusJwtDecoder.decode(token);
            }
        } catch (ParseException | JOSEException e) {
            log.error("token hết hạn");
            throw new RuntimeException(e);
        }
        throw new JwtException("token invalid");
    }
}
