package com.dama.service.social;

import com.dama.controller.api.StompController;
import com.dama.model.dto.request.kakaopay.ApproveRequest;
import com.dama.model.dto.request.kakaopay.CompletedRequestDto;
import com.dama.model.dto.request.kakaopay.ReadyRequest;
import com.dama.model.dto.response.kakaopay.ApproveResponse;
import com.dama.model.dto.response.kakaopay.ReadyResponse;
import com.dama.principal.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final StompController stompController;

    private final SecurityUtil securityUtil;

    private final String ADMIN_KEY="75f98f6d6e46a32d16d274bd51d72eb4";

    public ReadyResponse payReady(ApproveRequest approveRequest) {

        // 카카오가 요구한 결제요청request값을 담아줍니다.
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid", approveRequest.getCid());
        parameters.add("partner_order_id", approveRequest.getPartner_order_id());
        parameters.add("partner_user_id", approveRequest.getPartner_user_id());
        int count = stompController.returnHashmap().size() - 1;
        parameters.add("item_name", approveRequest.getItem_name()+" 외 "+ count +"개");
        parameters.add("item_code", approveRequest.getItem_code());
        parameters.add("quantity", String.valueOf(stompController.returnHashmap().size()-1));
        parameters.add("total_amount", String.valueOf(approveRequest.getTotal_amount()));
        parameters.add("tax_free_amount", String.valueOf(approveRequest.getTax_free_amount()));
        parameters.add("approval_url", approveRequest.getApproval_url()); // 결제승인시 넘어갈 url
        parameters.add("cancel_url", approveRequest.getCancel_url()); // 결제취소시 넘어갈 url
        parameters.add("fail_url", approveRequest.getFail_url()); // 결제 실패시 넘어갈 url

        log.info("파트너주문아이디:"+ parameters.get("partner_order_id")) ;
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        // 외부url요청 통로 열기.
        RestTemplate template = new RestTemplate();
        template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        String url = "https://kapi.kakao.com/v1/payment/ready";
        // template으로 값을 보내고 받아온 ReadyResponse값 readyResponse에 저장.
        ReadyResponse readyResponse = template.postForObject(url, requestEntity, ReadyResponse.class);
        log.info("결재준비 응답객체: " + readyResponse);

        readyResponse.setPartner_order_id(approveRequest.getPartner_order_id());
        // 받아온 값 return
        return readyResponse;
    }

    // 결제 승인요청 메서드
    public ApproveResponse payApprove(CompletedRequestDto completedRequestDto) {
        // request값 담기.
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        parameters.add("cid",completedRequestDto.getCid() );
        parameters.add("tid", completedRequestDto.getTid());
        parameters.add("partner_order_id", completedRequestDto.getPartner_order_id()); // 주문명
        parameters.add("partner_user_id", completedRequestDto.getPartner_user_id());
        parameters.add("pg_token", completedRequestDto.getPg_token());

        // 하나의 map안에 header와 parameter값을 담아줌.
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부url 통신
        RestTemplate template = new RestTemplate();
        template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        String url = "https://kapi.kakao.com/v1/payment/approve";
        // 보낼 외부 url, 요청 메시지(header,parameter), 처리후 값을 받아올 클래스.
        ApproveResponse approveResponse = template.postForObject(url, requestEntity, ApproveResponse.class);
        log.info("결재승인 응답객체: " + approveResponse);

        return approveResponse;
    }
    // header() 셋팅
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK "+ ADMIN_KEY); //커밋용 주석
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }
}
