package TTCS.TTCS_ThayPhuong.Configuration;

import TTCS.TTCS_ThayPhuong.Dto.Response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class JwtAccessDined implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ErrorResponse errorResponse=ErrorResponse.builder()
                .message("Bạn không có quyền truy cập")
                .build();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);//lỗi 403
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();//dùng object mapper để convert từ object sang dạng json
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

        response.flushBuffer();//flushBuffer để gửi về client
    }
}
