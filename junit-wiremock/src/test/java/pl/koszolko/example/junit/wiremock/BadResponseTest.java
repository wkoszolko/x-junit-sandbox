package pl.koszolko.example.junit.wiremock;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.web.client.RestTemplate;
import pl.koszolko.example.junit.wiremock.exception.ServerErrorException;

import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(Parameterized.class)
public class BadResponseTest {

    private final static String HOST = "http://localhost:8089";
    private final static String URL = "/example/entities/";
    private final static String ID = "1";
    private RestClient client = new RestClient(new RestTemplate(), HOST + URL + "{ID}");

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Parameterized.Parameter
    public Fault fault;

    @Parameterized.Parameter(value = 1)
    public Class<? extends Throwable> expectedExceptionClass;

    @Parameterized.Parameters(name = "{index}: fault={0}, expected exception={1}")
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                {Fault.EMPTY_RESPONSE, ServerErrorException.class},
                {Fault.MALFORMED_RESPONSE_CHUNK, ServerErrorException.class},
                {Fault.RANDOM_DATA_THEN_CLOSE, ServerErrorException.class}
        });
    }

    @Test
    public void testResponseCode() {
        exception.expect(expectedExceptionClass);

        //given
        stubFor(get(urlEqualTo(URL + ID))
                .willReturn(aResponse()
                        .withFault(fault)));

        //when
        client.getEntityBy(ID);
    }
}
