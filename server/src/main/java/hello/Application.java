
package hello;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.util.FileSystemUtils;

import javax.jms.ConnectionFactory;
import java.io.File;

@SpringBootApplication
@EnableJms
public class Application {

    @Bean
        // Strictly speaking this bean is not necessary as boot creates a default
    JmsListenerContainerFactory<?> topicJmsContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean
        // Strictly speaking this bean is not necessary as boot creates a default
    JmsListenerContainerFactory<?> queueJmsContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("1-20");
        return factory;
    }

    @Bean
    public ActiveMQQueue sum_queue() {
        return new ActiveMQQueue("sum_queue");
    }

    @Bean
    public ActiveMQQueue sub_queue() {
        return new ActiveMQQueue("sub_queue");
    }

    @Bean
    public ActiveMQQueue mul_queue() {
        return new ActiveMQQueue("mul_queue");
    }

    @Bean
    public ActiveMQQueue div_queue() {
        return new ActiveMQQueue("div_queue");
    }

    @Bean
    public ActiveMQTopic sum_response_topic() {
        return new ActiveMQTopic("sum_response_topic");
    }

    @Bean
    public ActiveMQTopic sub_response_topic() {
        return new ActiveMQTopic("sub_response_topic");
    }

    @Bean
    public ActiveMQTopic mul_response_topic() {
        return new ActiveMQTopic("mul_response_topic");
    }

    @Bean
    public ActiveMQTopic div_response_topic() {
        return new ActiveMQTopic("div_response_topic");
    }

    public static void main(String[] args) {
        // Clean out any ActiveMQ data from a previous run
        FileSystemUtils.deleteRecursively(new File("activemq-data"));

        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
    }
}

