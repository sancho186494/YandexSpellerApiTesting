import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SpellerTest {

    public static final String URL = "https://speller.yandex.net/services/spellservice";

    public static String checkWords = "лол кек чебурк";         //set check words
    public static String lang = "ru";                           //set language

    public static void main(String[] args) {
        CloseableHttpResponse httpResponse = null;

        //TEST REST API via GET-REQUEST
        System.out.println("Set language: " + lang);
        System.out.println("checkWords: " + checkWords);
        System.out.println();

        System.out.println("TEST REST API via GET-REQUEST");
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(URL + "/checkText?text=" +
                    URLEncoder.encode(checkWords, "utf-8") + "&lang=" + lang);
            System.out.println("-------------------------------------------------");
            httpResponse = httpClient.execute(httpGet);
            System.out.println(EntityUtils.toString(httpResponse.getEntity()));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                assert httpResponse != null;
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("-------------------------------------------------");
        System.out.println("-------------------------------------------------");
        System.out.println();


        //TEST SOAP API via POST-REQUEST
        String inputXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:spel=\"" + URL + "\">\n" +
                            "   <soapenv:Header/>\n" +
                            "   <soapenv:Body>\n" +
                            "      <spel:CheckTextRequest lang=\"" + lang + "\" options=\"0\" format=\"\">\n" +
                            "         <spel:text>" + checkWords + "</spel:text>\n" +
                            "      </spel:CheckTextRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>";

        StringEntity entity = new StringEntity(inputXML, ContentType.create(
                "text/xml", StandardCharsets.UTF_8));

        System.out.println("TEST SOAP API via POST-REQUEST");
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL);
            httpPost.addHeader("SOAPAction", "/checkText");
            httpPost.setEntity(entity);
            System.out.println("-------------------------------------------------");
            httpResponse = httpClient.execute(httpPost);
            System.out.println(EntityUtils.toString(httpResponse.getEntity()));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                assert httpResponse != null;
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
