package pl.koszolko.example.junit.wiremock;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.*;
import pl.koszolko.example.junit.wiremock.exception.NotFoundEntityException;
import pl.koszolko.example.junit.wiremock.exception.ServerErrorException;

public class RestClient {

    private final RestTemplate restTemplate;
    private final String url;

    public RestClient(RestTemplate restTemplate, String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public Entity getEntityBy(String... params) {
        Entity response;
        try {
            response  = restTemplate.getForObject(url, Entity.class, params);
        } catch (HttpClientErrorException e) {
            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                throw new NotFoundEntityException();
            } else {
                throw new ServerErrorException();
            }
        } catch (ResourceAccessException | UnknownHttpStatusCodeException | HttpServerErrorException e) {
            throw new ServerErrorException();
        }
        return response;
    }
}
