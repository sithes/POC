package com.shopping.cart.app.queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author Sithes
 *
 */
@Configuration
public class QueueConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueueConfig.class);

	@Value("${activemq.brokerUrl}")
	private String brokerUrl;

	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		LOGGER.debug("Entering QueueConfig.activeMQConnectionFactory");
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);
		LOGGER.debug("Leaving QueueConfig.activeMQConnectionFactory after the successfull connection.");
		return activeMQConnectionFactory;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		LOGGER.debug("Entering QueueConfig.jmsListenerContainerFactory");
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(activeMQConnectionFactory());
		factory.setErrorHandler(t -> {
			LOGGER.error("Error in jmsListener!", t);
		});
		factory.setConcurrency("1");
		LOGGER.debug("Leaving QueueConfig.jmsListenerContainerFactory after the succssfull connection set.");
		return factory;
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		return new CachingConnectionFactory(activeMQConnectionFactory());
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		return new JmsTemplate(cachingConnectionFactory());
	}

	@Bean
	public QueueService queue() {
		return new QueueService();
	}
}
