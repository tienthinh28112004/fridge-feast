package TTCS.TTCS_ThayPhuong.Util;


import TTCS.TTCS_ThayPhuong.Configuration.VnpayConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VNPayUtil {
    private final VnpayConfig vnpayConfig;
    public String getPaymentURL(Long amount, HttpServletRequest request){
        Map<String,String> prams=vnpayConfig.getVNPayConfig();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        String vnp_CreatedAt = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpiredAt = formatter.format(cld.getTime());

        prams.put("vnp_BankCode", "NCB");
        prams.put("vnp_TxnRef",getRandomNumber(8));//mã giao dịch
        prams.put("vnp_OrderInfo","thanh toan don hang cua ban");
        prams.put("vnp_Amount", String.valueOf(amount*100));
        prams.put("vnp_CreateDate", vnp_CreatedAt);
        prams.put("vnp_ExpireDate",vnp_ExpiredAt);
        prams.put("vnp_IpAddr",getIpAddress(request));

        String query = buildQuery(prams,true);

        String checksum=VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(),buildQuery(prams,false));

        query += "&vnp_SecureHash="+checksum;

        return vnpayConfig.getVnp_PayUrl()+ "?"+query;
    }

    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }
    private String buildQuery(Map<String,String> params,boolean encodeKey){
        return params.entrySet().stream()
                .filter(e -> e.getValue()!=null&&!e.getValue().isEmpty())//loại bỏ các value là null hay ""
                .sorted(Map.Entry.comparingByKey())//sắp xếp lại theo key
                .map(e ->{
                    Charset charset = StandardCharsets.US_ASCII;//sử dụng US_ASCII
                    String key = encodeKey? URLEncoder.encode(e.getKey(),charset) : e.getKey();
                    return key+ "="+URLEncoder.encode(e.getValue(),charset);//tạo thành chuỗi key-value
                }).collect(Collectors.joining("&"));
        //tạo ra chuỗi param
    }
    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
