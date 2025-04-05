package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Entity.Roles;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Entity.UserHasRole;
import TTCS.TTCS_ThayPhuong.Exception.TokenExpireException;
import TTCS.TTCS_ThayPhuong.Service.JwtService;
import TTCS.TTCS_ThayPhuong.Service.RedisService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final RedisService redisService;

    @Value("${app.secret}")
    private String secretKey;

    @Value("${app.jwt.token.expires-in}")
    private Long accessTokenExpireIn;

    @Value("${app.jet.refresh-token.expires-in}")
    private Long refreshTokenExpireIn;

    @Override
    public String generateAccessToken(User user) {
        JWSHeader header = new JWSHeader((JWSAlgorithm.HS512));

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("Tiến Thịnh")
                .issueTime(new Date())
                .expirationTime(new Date(new Date().getTime() + accessTokenExpireIn))
                .jwtID(UUID.randomUUID().toString())
                .claim("Authority",buildAuthority(user))
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());//chuyển về dạng json

        JWSObject jwsObject = new JWSObject(header,payload);

        try{
            jwsObject.sign(new MACSigner(secretKey));//ký chữ ký của mình
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    @Override
    public String generateRefreshToken(User user) {
        JWSHeader header=new JWSHeader(JWSAlgorithm.HS512);//mã hóa header

        JWTClaimsSet claimsSet=new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("Tiến Thịnh")
                .issueTime(new Date())
                .expirationTime(new Date(new Date().getTime() + refreshTokenExpireIn))
                .jwtID(UUID.randomUUID().toString())//có cái này để verify đỡ bị lỗi
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);

        try{
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }

    @Override
    public String extractEmail(String accessToken) throws ParseException {
        SignedJWT signedJWT=SignedJWT.parse(accessToken);
        return signedJWT.getJWTClaimsSet().toString();
    }

    @Override
    public boolean verificationToken(String token, User user) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        if(StringUtils.isNotBlank(redisService.get(jwtId))){
            //kiểm tra jwtId tồn tại trong redis và không rỗng thì đưa ra lỗi
            throw new TokenExpireException("Xác thực token không thành công do vẫn còn JwtId trong redis");
        }
        String email = signedJWT.getJWTClaimsSet().getSubject();
        var expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        if(!Objects.equals(email,user.getEmail())){
            //nếu email không trùng với email của token thì thông báo lỗi
            throw new TokenExpireException("Email của token không trùng email của hệ thống");
        }
        if(expiration.before(new Date())){
            //nếu như thời gian hết hạn trước thời điểm hiêện tại thì đưa ra lỗi
            throw new TokenExpireException("Token của bạn đã hết hạn");
        }

        return signedJWT.verify(new MACVerifier(secretKey));//xác thực token bước cuối
    }

    @Override
    public long extractTokenExpired(String token) {
        //thời gian còn lại của token dùng để lưu vào redis
        try{
            long expirationTime = SignedJWT.parse(token)
                    .getJWTClaimsSet().getExpirationTime().getTime();
            long currentTime = System.currentTimeMillis();
            return Math.max(expirationTime -currentTime,0);//tính thời gian còn lại của token,nếu hết thời gian thì trả về 0
        } catch (ParseException e) {
            throw new TokenExpireException("Token hết hiệu lực");
        }
    }

    private List<String> buildAuthority(User user){
        List<String> Authority= new ArrayList<>();
        user.getUserHasRoles().stream().map(UserHasRole::getRole).map(Roles::getName).forEach(Authority::add);
        return Authority;
    }
}
