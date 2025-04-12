package TTCS.TTCS_ThayPhuong.Service.Impl;

import TTCS.TTCS_ThayPhuong.Dto.Request.*;
import TTCS.TTCS_ThayPhuong.Dto.Response.*;
import TTCS.TTCS_ThayPhuong.Entity.Roles;
import TTCS.TTCS_ThayPhuong.Entity.User;
import TTCS.TTCS_ThayPhuong.Entity.UserHasRole;
import TTCS.TTCS_ThayPhuong.Enums.Role;
import TTCS.TTCS_ThayPhuong.Exception.BadRequestException;
import TTCS.TTCS_ThayPhuong.Exception.NotFoundException;
import TTCS.TTCS_ThayPhuong.Exception.RefreshTokenExpireException;
import TTCS.TTCS_ThayPhuong.Exception.TokenExpireException;
import TTCS.TTCS_ThayPhuong.Repository.HttpClient.OutBoundIdentityClient;
import TTCS.TTCS_ThayPhuong.Repository.HttpClient.OutBoundUserClient;
import TTCS.TTCS_ThayPhuong.Repository.RolesRepository;
import TTCS.TTCS_ThayPhuong.Repository.UserRepository;
import TTCS.TTCS_ThayPhuong.Service.AuthenticationService;
import TTCS.TTCS_ThayPhuong.Service.EmailVerificationTokenService;
import TTCS.TTCS_ThayPhuong.Service.JwtService;
import TTCS.TTCS_ThayPhuong.Service.RedisService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final RolesRepository rolesRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final OutBoundIdentityClient outBoundIdentityClient;
    private final OutBoundUserClient outBoundUserClient;

    @Value("${app.jwt.token.expires-in}")
    private Long accessTokenExpireIn;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private final String grant_Type ="authorization_code";


    @Override
    public UserResponse register(UserCreateRequest request) {
        return null;
    }

    @Override
    public SignInResponse logIn(LogInRequest request, HttpServletResponse response) {
        User user=userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new NotFoundException("User not found"));

        if(user.getEmailVerifiedAt()==null){
            throw new BadRequestException("Tài khoản của bạn chưa ược kích hoạt,vui lòng xác nhận mã OTP được gửi về mail");
        }

        if(!passwordEncoder.matches(user.getPassword(), request.getPassword())){
            throw new BadCredentialsException("Thông tin đăng nhập không hợp lệ,mật khẩu không trùng với mật khẩu của hệ thống");
        }
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);
        userRepository.save(user);

        Cookie cookie=new Cookie("refreshToken",refreshToken);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setHttpOnly(true);//để true
        cookie.setMaxAge(60*60*24);
        cookie.setSecure(false);//true thì chỉ gửi qua https thôi

        response.addCookie(cookie);
        return SignInResponse.builder()
                .userid(user.getId())
                .accessToken(accessToken)
                .accessTokenExpireIn(accessTokenExpireIn)
                .roles(user.getUserHasRoles().stream().map(userHasRole -> userHasRole.getRole().getName()).collect(Collectors.toList()))
                .build();
    }

    @Override
    public void logOut(LogOutRequest request, HttpServletResponse response) throws ParseException {
        if(StringUtils.isBlank(request.getAccessToken())){
            throw new TokenExpireException("Token không hợp lệ");
        }
        String email = jwtService.extractEmail(request.getAccessToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        long accessTokenExpireIn = jwtService.extractTokenExpired(request.getAccessToken());
        if(accessTokenExpireIn > 0){
            String jwtId= SignedJWT.parse(request.getAccessToken()).getJWTClaimsSet().getJWTID();
            redisService.save(jwtId,request.getAccessToken(),accessTokenExpireIn, TimeUnit.MILLISECONDS);
            user.setRefreshToken(null);
            userRepository.save(user);
        }

        Cookie cookie=new Cookie("refreshToken","");
        cookie.setSecure(true);//chỉ https mới truyền được
        cookie.setPath("/");
        cookie.setHttpOnly(true);//đánh dấu httpOnly
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) throws ParseException, JOSEException {
        if(StringUtils.isBlank(refreshToken)){
            throw new BadCredentialsException("RefreshToken không hợp lệ");
        }
        String email= jwtService.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("User not found"));
        if(!Objects.equals(user.getRefreshToken(),refreshToken)||StringUtils.isBlank(user.getRefreshToken())){
            throw new BadCredentialsException("RefreshToken không hợp lệ");
        }
        if(!jwtService.verificationToken(refreshToken,user)){
            throw new RefreshTokenExpireException("RefreshToken hết hạn");
        }
        String accessToken= jwtService.generateAccessToken(user);
        return RefreshTokenResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .build();
    }

    @Override
    public String verifyEmail(String token) {
        User user= emailVerificationTokenService.getUserByToken(token);
        user.setEmailVerifiedAt(LocalDateTime.now());
        user.setActive(true);
        userRepository.save(user);

        emailVerificationTokenService.deleteByUserId(user.getId());//xóa token
        log.info("E-mail verified with token: {}",token);
        return "Xác thực thành công";
    }


    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException {
        String email= jwtService.extractEmail(request.getAccessToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("user not found"));
        boolean valid=true;
        List<String> roles=new ArrayList<>();
        try {
            roles = (List<String>) SignedJWT.parse(request.getAccessToken()).getJWTClaimsSet().getClaim("Authority");
            jwtService.verificationToken(request.getAccessToken(),user);
        } catch (JOSEException e) {
            valid=false;
        }
        return IntrospectResponse.builder()
                .valid(valid)
                .role(roles)
                .build();
    }

    @Override
    public SignInResponse loginWithGoogle(String code, HttpServletResponse response) {

        ExchangeTokenResponse result = outBoundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                        .code(code)
                        .clientId(clientId)
                        .clientSecret(clientSecret)
                        .redirectUri(redirectUri)
                        .grantType(grant_Type)
                .build());

        GoogleUserResponse getUserInfo = outBoundUserClient.getUserInfo("json",result.getAccessToken());

        User user=User.builder()
                .email(getUserInfo.getEmail())
                .fullName(getUserInfo.getName())
                .avatarUrl(getUserInfo.getPicture())
                .emailVerifiedAt(getUserInfo.isVerified()? LocalDateTime.now():null)
                .build();
        Roles roles=rolesRepository.findByName(String.valueOf(Role.USER))
                .orElseThrow(()->new NotFoundException("Role not found"));

        Set<UserHasRole> roleSet=new HashSet<>();
        UserHasRole userHasRole=UserHasRole.builder()
                .user(user)
                .role(roles)
                .build();
        roleSet.add(userHasRole);

        user.setUserHasRoles(roleSet);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);
        Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(24*60*60);
        cookie.setPath("/");
        cookie.setSecure(false);//true để tránh https
        cookie.setDomain("localhost");

        response.addCookie(cookie);

        ArrayList<String> roleList=new ArrayList<>();
        roleList.add(String.valueOf(Role.USER));

        return SignInResponse.builder()
                .userid(user.getId())
                .accessToken(accessToken)
                .accessTokenExpireIn(accessTokenExpireIn)
                .roles(roleList)
                .build();
    }
}
