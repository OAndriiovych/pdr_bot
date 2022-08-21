package org.pdr;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.liqpay.LiqPayUtil;
import software.amazon.lambda.powertools.parameters.ParamManager;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AwsLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final String privateKey;

    public AwsLambda() {
        privateKey = ParamManager.getSsmProvider().withDecryption().get("liqpay.privateKey.cry");
    }

    public AwsLambda(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setStatusCode(dec(input.getBody(), context.getLogger()));
        return responseEvent;
    }

    private int dec(String inputMessage, LambdaLogger logger) {
        logger.log("input -> " + inputMessage);
        String decodedString = decodeText(inputMessage);
        String[] signatureAndData = decodedString.split("&");
        String codeData = signatureAndData[1].split("=")[1];
        String trueSignature = createSignature(codeData);
        String signatureOfMessage = URLDecoder.decode(signatureAndData[0].split("=")[1], StandardCharsets.UTF_8);
        int statusCode;
        if (trueSignature.equals(signatureOfMessage)) {
            String decodeData = decodeText(codeData);
            //#TODO add activate subscription
            statusCode = 200;
        } else {
            logger.log("is sing trueSignature -> " + trueSignature);
            logger.log("is sing signatureOfMessage -> " + signatureOfMessage);
            statusCode = 500;
        }
        return statusCode;
    }

    private static String decodeText(String s) {
        return new String(Base64.getDecoder().decode(s.getBytes(StandardCharsets.UTF_8)));
    }

    private String createSignature(String s) {
        return LiqPayUtil.base64_encode(LiqPayUtil.sha1(privateKey + s + privateKey));
    }
}

