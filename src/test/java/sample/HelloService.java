package sample;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface HelloService {

    @GetExchange("/hello")
    String hello();
}
