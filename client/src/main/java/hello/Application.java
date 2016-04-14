
package hello;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.FileSystemUtils;

import javax.jms.ConnectionFactory;
import java.io.File;
import java.util.Scanner;

@SpringBootApplication
@EnableJms
public class Application {

    private static JmsTemplate jmsTemplate;

    @Bean
        // Strictly speaking this bean is not necessary as boot creates a default
    JmsListenerContainerFactory<?> topicJmsContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean
    JmsListenerContainerFactory<?> queueJmsContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }

    public static void main(String[] args) {
        // Clean out any ActiveMQ data from a previous run
        FileSystemUtils.deleteRecursively(new File("activemq-data"));

        // Launch the application
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        jmsTemplate = context.getBean(JmsTemplate.class);

        Scanner sc = new Scanner(System.in);

        System.out.println("Operation: ");
        String operation = sc.nextLine();
        while (!"stop".equals(operation)) {
            int a, b;

            switch (operation) {
                case "+":
                    a = sc.nextInt();
                    b = sc.nextInt();
                    requestSum(a, b);
                    break;
                case "/":
                    a = sc.nextInt();
                    b = sc.nextInt();
                    requestDiv(a, b);
                    break;
                default:
                    System.out.println("Not supported operation:");
            }
            System.out.println("Operation: ");
            operation = sc.nextLine();
        }
    }

    private static void requestDiv(int a, int b) {
        String toSend = a + "/" + b;
        System.out.println("Sending: " + toSend);
        jmsTemplate.convertAndSend(new ActiveMQQueue("div_queue"), toSend);
    }

    private static void requestSum(int a, int b) {
        String toSend = a + "+" + b;
        System.out.println("Sending: " + toSend);
        jmsTemplate.convertAndSend(new ActiveMQQueue("sum_queue"), toSend);
    }
}

