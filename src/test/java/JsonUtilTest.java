import java.nio.charset.Charset;

import hr.iivanovic.psyedu.util.JsonUtil;

/**
 * @author iivanovic
 * @date 19.09.16.
 */
public class JsonUtilTest {
    public static void main(String[] args) {
        String expectedReqBody = "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:SMS\"><soapenv:Header/><soapenv:Body><urn:getSMSext soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><date xsi:type=\"xsd:string\">%timestamp_yyyy-MM-dd%</date><start xsi:type=\"xsd:string\"></start><end xsi:type=\"xsd:string\"></end><rc xsi:type=\"xsd:string\">5145</rc><called xsi:type=\"xsd:string\">812001</called><has_keyw xsi:type=\"xsd:string\">0</has_keyw><keyword xsi:type=\"xsd:string\"></keyword><last_id xsi:type=\"xsd:string\">0</last_id></urn:getSMSext></soapenv:Body></soapenv:Envelope>";

        String s = JsonUtil.dataToJson(expectedReqBody.getBytes());
        String s1 = JsonUtil.dataToJson(expectedReqBody.getBytes(Charset.defaultCharset()));
        String s2 = JsonUtil.dataToJson(expectedReqBody.getBytes(Charset.forName("US-ASCII")));
        System.out.println(s);
        System.out.println("\n" + s1);
        System.out.println("\n" + s2);
    }
}
