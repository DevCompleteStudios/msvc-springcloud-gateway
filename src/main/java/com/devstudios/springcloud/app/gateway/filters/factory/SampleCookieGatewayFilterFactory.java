package com.devstudios.springcloud.app.gateway.filters.factory;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;




@Component
public class SampleCookieGatewayFilterFactory extends AbstractGatewayFilterFactory<SampleCookieGatewayFilterFactory.ConfigurationCookie> {

    private final Logger logger = LoggerFactory.getLogger(SampleCookieGatewayFilterFactory.class);


    public SampleCookieGatewayFilterFactory(){
        super(ConfigurationCookie.class);
    }

    @Override
    public GatewayFilter apply(ConfigurationCookie config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            logger.info("Ejecutando pre gateway filter factory " + config.message);

            return chain.filter(exchange)
                .then( Mono.fromRunnable(() -> {
                    Optional.ofNullable(
                        config.value
                    ).ifPresent((c) -> exchange.getResponse()
                        .addCookie(ResponseCookie.from(config.name, c).build())
                    );

                    logger.info("Ejecutando post gateway filter factory " + config.message);
                }));
        }, 100);
    }

    public static class ConfigurationCookie{
        private String name;
        private String value;
        private String message;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
    }

    // @Override
    // public List<String> shortcutFieldOrder() {
    //     return Arrays.asList("message", "name", "value");
    // }

    // @Override
    // public String name(){
    //     return "EjemploCookie";
    // }

}
