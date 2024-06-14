package br.com.picpaychlng.services;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class AuthorizationService {

    private final RestTemplate restTemplate;

    @Autowired
    public AuthorizationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isAuthorized() {
        String url = "https://util.devi.tools/api/v2/authorize";
        AuthorizationResponse response = restTemplate.getForObject(url, AuthorizationResponse.class);
        return response != null && response.getStatus().equals("success") && response.getData().isAuthorized();
    }

    private static class AuthorizationResponse {
        private String status;
        private AuthorizationData data;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public AuthorizationData getData() {
            return data;
        }

        public void setData(AuthorizationData data) {
            this.data = data;
        }

        private static class AuthorizationData {
            private boolean authorization;

            public boolean isAuthorized() {
                return authorization;
            }

            public void setAuthorization(boolean authorization) {
                this.authorization = authorization;
            }
        }
    }


}