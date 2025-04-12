package TTCS.TTCS_ThayPhuong.Configuration;

import TTCS.TTCS_ThayPhuong.Dto.Response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;


public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
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
