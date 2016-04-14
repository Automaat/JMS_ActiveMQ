package hello.processors;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class SumProcessor {

    /**
     * Get a copy of the application context
     */
    @Autowired
    ConfigurableApplicationContext context;

    /**
     * When you receive a message, print it out, then shut down the application.
     * Finally, clean up any ActiveMQ server stuff.
     */
    @JmsListener(destination = "sum_queue", containerFactory = "queueJmsContainerFactory")
    public void receiveMessage(String message) {
        System.out.println("Processing : " + message);

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        String[] split = message.split("\\+");

        try {

            Integer a = Integer.parseInt(split[0]);
            Integer b = Integer.parseInt(split[1]);

            Integer result = a + b;
            jmsTemplate.convertAndSend(new ActiveMQTopic("sum_response_topic"), message + " = " + result.toString());

        } catch (NumberFormatException e) {
            jmsTemplate.convertAndSend(new ActiveMQTopic("sum_response_topic"), "Cannot parse request: " + message);
        }
    }
}
