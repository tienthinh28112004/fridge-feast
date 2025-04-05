package TTCS.TTCS_ThayPhuong.Configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final CustomJwtDecoder customJwtDecoder;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //xử lý các endpoint public
        http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults());
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
        ).sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS));

        //xử lý các endpoint cần token
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)//giải mã token
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))//phân quyền
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())//bắt lỗi 401(đăng nhâp)
                .accessDeniedHandler(new JwtAccessDined()));//bắt lỗi 403(không có quyền)
        return http.build();
    }
    @Bean
    public PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("Authority");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");//loại bỏ tiền tố scope

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);//set quyền vừa tạo
        return jwtAuthenticationConverter;
    }
}
