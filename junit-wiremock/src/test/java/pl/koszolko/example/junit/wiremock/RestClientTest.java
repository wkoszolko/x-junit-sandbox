package pl.koszolko.example.junit.wiremock;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import pl.koszolko.example.junit.wiremock.exception.ServerErrorException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class RestClientTest {

    private final static String HOST = "http://localhost:8089";
    private final static String URL = "/example/entities/";
    private final static String ID = "1";
    private RestClient client = new RestClient(new RestTemplate(), HOST + URL + "{ID}");

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void exampleTest() {
        //given
        stubFor(get(urlEqualTo(URL + ID))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"id\":1, \"data\": \"same date\"}")));

        //when
        Entity response = client.getEntityBy(ID);

        //then
        Assert.notNull(response);
        Assert.notNull(response.getId());
        Assert.notNull(response.getData());
    }

    @Test(expected = ServerErrorException.class)
    public void  serverFaultTest() {
        //given
        wireMockRule.stop();

        //when
        client.getEntityBy(ID);

        //then
        //should throw exception
    }
}