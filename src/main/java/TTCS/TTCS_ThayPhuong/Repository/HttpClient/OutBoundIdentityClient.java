package TTCS.TTCS_ThayPhuong.Repository.HttpClient;

import TTCS.TTCS_ThayPhuong.Dto.Request.ExchangeTokenRequest;
import TTCS.TTCS_ThayPhuong.Dto.Response.ExchangeTokenResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
@FeignClient(name="outbound-identity",url = "https://oauth2.googleapis.com")
public interface OutBoundIdentityClient {//gửi dữ liệu đến https://oauth2.googleapis.com để lâấy lại dữ liệu
    @PostMapping(value = "/token",produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResponse exchangeToken(@QueryMap ExchangeTokenRequest request);
}
