package br.com.picpaychlng.services;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final RestTemplate restTemplate;

    @Autowired
    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void notifyUser(Long userId, String message) {
        String url = "https://util.devi.tools/api/v1/notify";
        NotificationRequest notificationRequest = new NotificationRequest(userId, message);
        restTemplate.postForObject(url, notificationRequest, Void.class);
    }

    private static class NotificationRequest {
        private Long userId;
        private String message;

        public NotificationRequest(Long userId, String message) {
            this.userId = userId;
            this.message = message;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
