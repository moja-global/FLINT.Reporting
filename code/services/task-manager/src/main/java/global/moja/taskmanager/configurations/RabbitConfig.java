/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.configurations;


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

  public static final String RAW_DATA_PROCESSING_EXCHANGE_NAME = "Raw-Data-Processing";
  public static final String RAW_DATA_PROCESSING_QUEUE = "Raw-Data-Processing-Queue";
  public static final String RAW_DATA_PROCESSING_RESULTS_QUEUE = "Raw-Data-Processing-Results-Queue";
  public static final String RAW_DATA_PROCESSING_ROUTING_KEY = "Raw-Data-Processing";

  public static final String PROCESSED_DATA_AGGREGATION_EXCHANGE_NAME = "Processed-Data-Aggregation";
  public static final String PROCESSED_DATA_AGGREGATION_QUEUE = "Processed-Data-Aggregation-Queue";
  public static final String PROCESSED_DATA_AGGREGATION_RESULTS_QUEUE = "Processed-Data-Aggregation-Results-Queue";
  public static final String PROCESSED_DATA_AGGREGATION_ROUTING_KEY = "Processed-Data-Aggregation";

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
  public TopicExchange rawDataProcessingExchange() {
    return new TopicExchange(RAW_DATA_PROCESSING_EXCHANGE_NAME);
  }

  @Bean
  public TopicExchange processedDataAggregationExchange() {
    return new TopicExchange(PROCESSED_DATA_AGGREGATION_EXCHANGE_NAME);
  }

  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Queues">

  @Bean
  public Queue rawDataProcessingQueue() {
    return new Queue(RAW_DATA_PROCESSING_QUEUE);
  }

  @Bean
  public Queue rawDataProcessingResultsQueue() {
    return new Queue(RAW_DATA_PROCESSING_RESULTS_QUEUE);
  }

  @Bean
  public Queue processedDataAggregationQueue() {
    return new Queue(PROCESSED_DATA_AGGREGATION_QUEUE);
  }

  @Bean
  public Queue processedDataAggregationResultsQueue() {
    return new Queue(PROCESSED_DATA_AGGREGATION_RESULTS_QUEUE);
  }

  // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="Topic Exchanges / Queues Bindings">

  @Bean
  public Binding rawDataProcessingBinding() {
    return BindingBuilder.bind(rawDataProcessingQueue())
            .to(rawDataProcessingExchange())
            .with(RAW_DATA_PROCESSING_ROUTING_KEY);
  }


  @Bean
  public Binding rawDataProcessingResultsBinding() {
    return BindingBuilder.bind(rawDataProcessingResultsQueue())
            .to(rawDataProcessingExchange())
            .with(RAW_DATA_PROCESSING_ROUTING_KEY);
  }

  @Bean
  public Binding processedDataAggregationBinding() {
    return BindingBuilder.bind(processedDataAggregationQueue())
            .to(processedDataAggregationExchange())
            .with(PROCESSED_DATA_AGGREGATION_ROUTING_KEY);
  }


  @Bean
  public Binding processedDataAggregationResultsBinding() {
    return BindingBuilder.bind(processedDataAggregationResultsQueue())
            .to(processedDataAggregationExchange())
            .with(PROCESSED_DATA_AGGREGATION_ROUTING_KEY);
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
