//package com.example.demo.config;
//
//
//
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitConfig {
//
//    @Bean
//     Queue queue() {
//        return new Queue("myQueue", false);
//    }
//
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange("myExchange");
//    }
//
//    @Bean
//    public Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("routing.key");
//    }
//}
//
