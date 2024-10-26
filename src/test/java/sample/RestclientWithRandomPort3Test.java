package sample;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestclientWithRandomPort3Test {

    @Autowired
    private RestClient restClient;

    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    static class TestConfig {

        @Bean
        HelloController helloController() {
            return new HelloController();
        }

        // 解決策その2：宛先の決定を遅延させる
        @Bean
        RestClient restClient(Environment env) {
            return RestClient.builder()
                    .uriBuilderFactory(new LocalHostUriBuilderFactory(env))
                    .build();
        }

        @Bean
        HelloService helloService(RestClient restClient) {
            RestClientAdapter adapter = RestClientAdapter.create(restClient);
            HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
            return factory.createClient(HelloService.class);
        }
    }

	@Test
	void testHello() {
	    String actual = restClient
	            .get()
	            .uri("/hello")
	            .retrieve()
	            .body(String.class);
	    assertThat(actual).isEqualTo("hello!");
	}

    @Test
    void testHello2(@Autowired HelloService helloService) {
        String actual = helloService.hello();
        assertThat(actual).isEqualTo("hello!");
    }
}
