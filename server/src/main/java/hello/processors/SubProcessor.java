/*
 * Copyright (c) 2016 by Sabre Holdings Corp.
 * 3150 Sabre Drive, Southlake, TX 76092 USA
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sabre Holdings Corporation ("Confidential Information").
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with Sabre Holdings Corporation.
 */
package hello.processors;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class SubProcessor {

    @Autowired
    ConfigurableApplicationContext context;

    /**
     * When you receive a message, print it out, then shut down the application.
     * Finally, clean up any ActiveMQ server stuff.
     */
    @JmsListener(destination = "sub_queue", containerFactory = "queueJmsContainerFactory")
    public void receiveMessage(String message) {
        System.out.println("Processing : " + message);

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        String[] split = message.split("-");
        try {

            Integer a = Integer.parseInt(split[0]);
            Integer b = Integer.parseInt(split[1]);

            Integer result = a - b;
            jmsTemplate.convertAndSend(new ActiveMQTopic("sub_response_topic"), message + " = " + result.toString());

        } catch (NumberFormatException e) {
            jmsTemplate.convertAndSend(new ActiveMQTopic("sub_response_topic"), "Cannot parse request: " + message);
        }
    }
}
