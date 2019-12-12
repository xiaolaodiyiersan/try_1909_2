package com.qf.shop_sso;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public FanoutExchange getExchange(){
        return new FanoutExchange("mail_exchange");
    }
    @Bean
    public Queue getQueue(){
        return new Queue("mail_queue");
    }
    @Bean
    public Binding getBinding(FanoutExchange getExchange,Queue getQueue){
        return BindingBuilder.bind(getQueue).to(getExchange);
    }

}
