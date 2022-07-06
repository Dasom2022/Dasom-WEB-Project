package com.dama.service.social;

import com.dama.model.dto.request.kakaopay.ApproveRequest;
import com.dama.model.dto.request.kakaopay.ReadyRequest;
import com.dama.principal.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final SecurityUtil securityUtil;

    public ReadyRequest payReady(ApproveRequest approveRequest) {

        // 카카오가 요구한 결제요청request값을 담아줍니다.
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", "TC0ONETIME");
        parameters.add("partner_order_id", approveRequest.getPartner_order_id());
        parameters.add("partner_user_id", "inflearn");
        parameters.add("item_name", approveRequest.getItem_name());
        parameters.add("quantity", String.valueOf(approveRequest.getQuantity()));
        parameters.add("total_amount", String.valueOf(approveRequest.getTotal_amount()));
        parameters.add("tax_free_amount", String.valueOf(approveRequest.getTax_free_amount()));
        parameters.add("approval_url", "http://localhost/order/pay/completed"); // 결제승인시 넘어갈 url
        parameters.add("cancel_url", "http://localhost/order/pay/cancel"); // 결제취소시 넘어갈 url
        parameters.add("fail_url", "http://localhost/order/pay/fail"); // 결제 실패시 넘어갈 url

        log.info("파트너주문아이디:"+ parameters.get("partner_order_id")) ;
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        // 외부url요청 통로 열기.
        RestTemplate template = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/ready";
        // template으로 값을 보내고 받아온 ReadyResponse값 readyResponse에 저장.
        ReadyRequest readyResponse = template.postForObject(url, requestEntity, ReadyRequest.class);
        log.info("결재준비 응답객체: " + readyResponse);
        // 받아온 값 return
        return readyResponse;
    }

    // 결제 승인요청 메서드
    /*public ApproveRequest payApprove(String tid, String pgToken) {
        User user =  (User)SessionUtils.getAttribute("LOGIN_USER");
        List<CartDto> carts = cartMapper.getCartByUserNo(user.getNo());
        // 주문명 만들기.
        String[] cartNames = new String[carts.size()];
        for(CartDto cart: carts) {
            for(int i=0; i< carts.size(); i++) {
                cartNames[i] = cart.getClassTitle();
            }
        }
        String itemName = cartNames[0] + " 그외" + (carts.size()-1);

        String order_id = user.getNo() + itemName;

        // request값 담기.
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", "TC0ONETIME");
        parameters.add("tid", tid);
        parameters.add("partner_order_id", order_id); // 주문명
        parameters.add("partner_user_id", "회사명");
        parameters.add("pg_token", pgToken);

        // 하나의 map안에 header와 parameter값을 담아줌.
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부url 통신
        RestTemplate template = new RestTemplate();
        String url = "https://kapi.kakao.com/v1/payment/approve";
        // 보낼 외부 url, 요청 메시지(header,parameter), 처리후 값을 받아올 클래스.
        ApproveRequest approveResponse = template.postForObject(url, requestEntity, ApproveRequest.class);
        log.info("결재승인 응답객체: " + approveResponse);

        return approveResponse;
    }*/
    // header() 셋팅
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK 어드민키");
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return headers;
    }
}
