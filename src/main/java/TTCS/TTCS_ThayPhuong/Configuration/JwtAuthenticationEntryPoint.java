package TTCS.TTCS_ThayPhuong.Configuration;

import TTCS.TTCS_ThayPhuong.Dto.Response.ErrorResponse;
import TTCS.TTCS_ThayPhuong.Exception.TokenExpireException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;


public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorResponse errorResponse=ErrorResponse.builder()
                .message("token của bạn bị lôi")
                .build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);//lỗi 401
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();//dùng object mapper để convert từ object sang dạng json
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

        response.flushBuffer();//flushBuffer để gửi về client
    }
}
