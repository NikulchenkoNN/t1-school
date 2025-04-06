package ru.home_work.t1_school.service;

import org.springframework.stereotype.Component;

@Component
public class MailNotificator {

    public void sendNotification() {
        System.out.println("send email");
    }
}
