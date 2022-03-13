package com.monkeypenthouse.core.service;

import com.monkeypenthouse.core.entity.SmsAuthNum;
import com.monkeypenthouse.core.repository.SmsAuthNumRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Service
@Log4j2
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    private final SmsAuthNumRepository smsAuthNumRepository;

    @Override
    public void sendSMS(String toNumber) throws CoolsmsException {
        String randomNumber = numberGen(6,1);

        Message message = new Message(apiKey, apiSecret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<>();
        params.put("to", toNumber);
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("text", "[몽키펜트하우스] 인증번호 " + randomNumber + " 를 입력하세요.");
        params.put("app_version", "test app 1.2"); // application name and version
        JSONObject obj = message.send(params);

        SmsAuthNum smsAuthNum = new SmsAuthNum(toNumber, randomNumber);
        smsAuthNumRepository.save(smsAuthNum);
    }

    @Override
    public boolean checkAuthNum(String phoneNum, String authNum) throws Exception {
        Optional<SmsAuthNum> numOptional = smsAuthNumRepository.findById(phoneNum);
        return numOptional.filter(smsAuthNum -> authNum.equals(smsAuthNum.getAuthNum())).isPresent();
    }

    //     len : 생성할 난수의 길이
    //     dupCd : 중복 허용 여부 (1: 중복허용, 2:중복제거)
    private static String numberGen(int len, int dupCd) {

        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수

        for(int i = 0; i < len; i++) {

            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));

            if (dupCd == 1) {
                //중복 허용시 numStr에 append
                numStr += ran;
            } else if (dupCd == 2) {
                //중복을 허용하지 않을시 중복된 값이 있는지 검사한다
                if (!numStr.contains(ran)) {
                    //중복된 값이 없으면 numStr에 append
                    numStr += ran;
                } else {
                    //생성된 난수가 중복되면 루틴을 다시 실행한다
                    i -=1 ;
                }
            }
        }
        return numStr;
    }

}
