package TTCS.TTCS_ThayPhuong.Repository.HttpClient;

import TTCS.TTCS_ThayPhuong.Dto.Response.GoogleUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "outbound-user-client",url = "http://www.googleapis.com")
public interface OutBoundUserClient {//gửi thông tin để lấy thông tin của người đăng nhập bằng google
    @GetMapping(value = "/oauth2/v1/userinfo")
    GoogleUserResponse getUserInfo(@RequestParam("alt") String alt,//định dạng(thường là json)
                                   @RequestParam("access_token") String accessToken);//accessToken để xác thực
}
