/*
 * Copyright (C) 2020 The Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.configurations;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Configuration
@PropertySource(value = {"classpath:rabbitmq.properties"})
public class RabbitConfig {
	
  @Value("${spring.rabbitmq.host}")
  private String host;

  @Value("${spring.rabbitmq.virtual-host}")
  private String virtualHost;

  @Value("${spring.rabbitmq.port}")
  private int port;

  @Value("${spring.rabbitmq.username}")
  private String username;

  @Value("${spring.rabbitmq.password}")
  private String password;	

  public static final String DATA_AGGREGATION_EXCHANGE_NAME = 
			"Data-Aggregation";

  public static final String DATA_AGGREGATION_QUEUE = 
			"Data-Aggregation-Queue";	
	
  public static final String DATA_AGGREGATION_RESULTS_QUEUE =
      "Data-Aggregation-Results-Queue";
	
  public static final String DATA_AGGREGATION_ROUTING_KEY = 
			"Data-Aggregation";


  @Bean
  public ConnectionFactory connectionFactory() {
    CachingConnectionFactory factory = new CachingConnectionFactory();
    factory.setHost(host);
    factory.setVirtualHost(virtualHost);
    factory.setPort(port);
    factory.setUsername(username);
    factory.setPassword(password);
    return factory;
  }

  // <editor-fold defaultstate="collapsed" desc="Topic Exchanges">

  @Bean
  public TopicExchange dataAggregationExchange() {
    return new TopicExchange(DATA_AGGREGATION_EXCHANGE_NAME);
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Queues">

  @Bean
  public Queue dataAggregationQueue() {
    return new Queue(DATA_AGGREGATION_QUEUE);
  }

  @Bean
  public Queue dataAggregationResultsQueue() {
    return new Queue(DATA_AGGREGATION_RESULTS_QUEUE);
  }
  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Topic Exchanges / Queues Bindings">

  @Bean
  public Binding dataAggregationBinding() {
    return BindingBuilder.bind(dataAggregationQueue())
        .to(dataAggregationExchange())
        .with(DATA_AGGREGATION_ROUTING_KEY);
  }
	
	
  @Bean
  public Binding dataAggregationResultsBinding() {
    return BindingBuilder.bind(dataAggregationResultsQueue())
        .to(dataAggregationExchange())
        .with(DATA_AGGREGATION_ROUTING_KEY);
  }	
  // </editor-fold>

  @Bean
  public RabbitTemplate rabbitTemplate() {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
    rabbitTemplate.setMessageConverter(messageConverter());
    return rabbitTemplate;
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
