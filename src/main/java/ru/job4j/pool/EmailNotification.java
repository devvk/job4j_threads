package ru.job4j.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {

    private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void emailTo(User user) {
        pool.submit(() -> {
            String subject = String.format("Notification %s to email %s.", user.username(), user.email());
            String body = String.format("Add a new event to %s", user.username());
            send(subject, body, user.email());
        });
    }

    public void send(String subject, String body, String email) {
        System.out.println(subject);
        System.out.println(body);
        System.out.println(email);
    }

    public void close() {
        pool.shutdown();
    }

    public static void main(String[] args) {
        EmailNotification emailNotification = new EmailNotification();
        emailNotification.emailTo(new User(1, "User1", "user1@mail.com"));
        emailNotification.emailTo(new User(2, "User2", "user2bb@mail.com"));
        emailNotification.close();
    }
}
