package org.pdr;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pdr.AwsLambda;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class AwsLambdaTest {

    private static String privateKey = null;

    @BeforeAll
    static void init() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/bot.properties")) {
            properties.load(fis);
        } catch (IOException e) {
        }
        privateKey = properties.getProperty("liqpay.privateKey");
    }


    @Test
    void testSignatureVerificationFailed() {
        APIGatewayProxyRequestEvent mockEv = Mockito.spy(APIGatewayProxyRequestEvent.class);
        Mockito.when(mockEv.getBody()).thenReturn("c2lnbmF0dXJlPUY4Q3hkUENWbTVKcFUyVmtSJTJCUjdXJTJCSHRYUUElM0QmZGF0YT1leUp3WVhsdFpXNTBYMmxrSWpveU1EWXhNREExTnprMkxDSmhZM1JwYjI0aU9pSndZWGtpTENKemRHRjBkWE1pT2lKellXNWtZbTk0SWl3aWRtVnljMmx2YmlJNk15d2lkSGx3WlNJNkltSjFlU0lzSW5CaGVYUjVjR1VpT2lKbmNHRjVZMkZ5WkNJc0luQjFZbXhwWTE5clpYa2lPaUp6WVc1a1ltOTRYMmszTnpFeE5EUTNOVEF6TlNJc0ltRmpjVjlwWkNJNk5ERTBPVFl6TENKdmNtUmxjbDlwWkNJNklqRXRNaUlzSW14cGNYQmhlVjl2Y21SbGNsOXBaQ0k2SWtvMVZWaEJXVXBTTVRZMk1EazRNakkwTWpRMU9ERTNNeUlzSW1SbGMyTnlhWEIwYVc5dUlqb2lWR1Z6ZEZSbGMzUWlMQ0p6Wlc1a1pYSmZjR2h2Ym1VaU9pSXpPREEyTmpnNE1ETTBPVGtpTENKelpXNWtaWEpmWTJGeVpGOXRZWE5yTWlJNklqUTJNamN3TlNvMk5DSXNJbk5sYm1SbGNsOWpZWEprWDJKaGJtc2lPaUp3WWlJc0luTmxibVJsY2w5allYSmtYM1I1Y0dVaU9pSjJhWE5oSWl3aWMyVnVaR1Z5WDJOaGNtUmZZMjkxYm5SeWVTSTZPREEwTENKcGNDSTZJakl4TXk0eE1Ea3VNakkwTGpJd05DSXNJbUZ0YjNWdWRDSTZNVFE0T0M0d0xDSmpkWEp5Wlc1amVTSTZJbFZUUkNJc0luTmxibVJsY2w5amIyMXRhWE56YVc5dUlqb3dMakFzSW5KbFkyVnBkbVZ5WDJOdmJXMXBjM05wYjI0aU9qZ3VPVE1zSW1GblpXNTBYMk52YlcxcGMzTnBiMjRpT2pBdU1Dd2lZVzF2ZFc1MFgyUmxZbWwwSWpvMU5UVXlNaTR6TkN3aVlXMXZkVzUwWDJOeVpXUnBkQ0k2TlRVMU1qSXVNelFzSW1OdmJXMXBjM05wYjI1ZlpHVmlhWFFpT2pBdU1Dd2lZMjl0YldsemMybHZibDlqY21Wa2FYUWlPak16TXk0eE15d2lZM1Z5Y21WdVkzbGZaR1ZpYVhRaU9pSlZRVWdpTENKamRYSnlaVzVqZVY5amNtVmthWFFpT2lKVlFVZ2lMQ0p6Wlc1a1pYSmZZbTl1ZFhNaU9qQXVNQ3dpWVcxdmRXNTBYMkp2Ym5Weklqb3dMakFzSW0xd2FWOWxZMmtpT2lJM0lpd2lhWE5mTTJSeklqcG1ZV3h6WlN3aWJHRnVaM1ZoWjJVaU9pSjFheUlzSW1OeVpXRjBaVjlrWVhSbElqb3hOall3T1RneU1qUXlORFU1TENKbGJtUmZaR0YwWlNJNk1UWTJNRGs0TWpJME1qVXhOaXdpZEhKaGJuTmhZM1JwYjI1ZmFXUWlPakl3TmpFd01EVTNPVFo5");
        Context mockContext = Mockito.spy(Context.class);
        LambdaLogger mockLogger = Mockito.spy(LambdaLogger.class);
        Mockito.when(mockContext.getLogger()).thenReturn(mockLogger);
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new AwsLambda(privateKey + "a").handleRequest(mockEv, mockContext);
        Assertions.assertNotEquals(200, apiGatewayProxyResponseEvent.getStatusCode());
    }

    @Test
    void testSignatureVerification() {
        APIGatewayProxyRequestEvent mockEv = Mockito.spy(APIGatewayProxyRequestEvent.class);
        Mockito.when(mockEv.getBody()).thenReturn("c2lnbmF0dXJlPUY4Q3hkUENWbTVKcFUyVmtSJTJCUjdXJTJCSHRYUUElM0QmZGF0YT1leUp3WVhsdFpXNTBYMmxrSWpveU1EWXhNREExTnprMkxDSmhZM1JwYjI0aU9pSndZWGtpTENKemRHRjBkWE1pT2lKellXNWtZbTk0SWl3aWRtVnljMmx2YmlJNk15d2lkSGx3WlNJNkltSjFlU0lzSW5CaGVYUjVjR1VpT2lKbmNHRjVZMkZ5WkNJc0luQjFZbXhwWTE5clpYa2lPaUp6WVc1a1ltOTRYMmszTnpFeE5EUTNOVEF6TlNJc0ltRmpjVjlwWkNJNk5ERTBPVFl6TENKdmNtUmxjbDlwWkNJNklqRXRNaUlzSW14cGNYQmhlVjl2Y21SbGNsOXBaQ0k2SWtvMVZWaEJXVXBTTVRZMk1EazRNakkwTWpRMU9ERTNNeUlzSW1SbGMyTnlhWEIwYVc5dUlqb2lWR1Z6ZEZSbGMzUWlMQ0p6Wlc1a1pYSmZjR2h2Ym1VaU9pSXpPREEyTmpnNE1ETTBPVGtpTENKelpXNWtaWEpmWTJGeVpGOXRZWE5yTWlJNklqUTJNamN3TlNvMk5DSXNJbk5sYm1SbGNsOWpZWEprWDJKaGJtc2lPaUp3WWlJc0luTmxibVJsY2w5allYSmtYM1I1Y0dVaU9pSjJhWE5oSWl3aWMyVnVaR1Z5WDJOaGNtUmZZMjkxYm5SeWVTSTZPREEwTENKcGNDSTZJakl4TXk0eE1Ea3VNakkwTGpJd05DSXNJbUZ0YjNWdWRDSTZNVFE0T0M0d0xDSmpkWEp5Wlc1amVTSTZJbFZUUkNJc0luTmxibVJsY2w5amIyMXRhWE56YVc5dUlqb3dMakFzSW5KbFkyVnBkbVZ5WDJOdmJXMXBjM05wYjI0aU9qZ3VPVE1zSW1GblpXNTBYMk52YlcxcGMzTnBiMjRpT2pBdU1Dd2lZVzF2ZFc1MFgyUmxZbWwwSWpvMU5UVXlNaTR6TkN3aVlXMXZkVzUwWDJOeVpXUnBkQ0k2TlRVMU1qSXVNelFzSW1OdmJXMXBjM05wYjI1ZlpHVmlhWFFpT2pBdU1Dd2lZMjl0YldsemMybHZibDlqY21Wa2FYUWlPak16TXk0eE15d2lZM1Z5Y21WdVkzbGZaR1ZpYVhRaU9pSlZRVWdpTENKamRYSnlaVzVqZVY5amNtVmthWFFpT2lKVlFVZ2lMQ0p6Wlc1a1pYSmZZbTl1ZFhNaU9qQXVNQ3dpWVcxdmRXNTBYMkp2Ym5Weklqb3dMakFzSW0xd2FWOWxZMmtpT2lJM0lpd2lhWE5mTTJSeklqcG1ZV3h6WlN3aWJHRnVaM1ZoWjJVaU9pSjFheUlzSW1OeVpXRjBaVjlrWVhSbElqb3hOall3T1RneU1qUXlORFU1TENKbGJtUmZaR0YwWlNJNk1UWTJNRGs0TWpJME1qVXhOaXdpZEhKaGJuTmhZM1JwYjI1ZmFXUWlPakl3TmpFd01EVTNPVFo5");
        Context mockContext = Mockito.spy(Context.class);
        LambdaLogger mockLogger = Mockito.spy(LambdaLogger.class);
        Mockito.when(mockContext.getLogger()).thenReturn(mockLogger);
        APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new AwsLambda(privateKey).handleRequest(mockEv, mockContext);
        Assertions.assertEquals(200, apiGatewayProxyResponseEvent.getStatusCode());
    }


}