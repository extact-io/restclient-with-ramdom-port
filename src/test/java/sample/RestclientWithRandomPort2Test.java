package sample;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestclientWithRandomPort2Test {

    private static RestClient restClient;

    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    static class TestConfig {
        @Bean
        HelloController helloController() {
            return new HelloController();
        }
    }

    // 解決策その1：@BeforeAllでRestClientを作る
    @BeforeAll
    static void beforeEach(@Value("${local.server.port}") int port) {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

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
}
