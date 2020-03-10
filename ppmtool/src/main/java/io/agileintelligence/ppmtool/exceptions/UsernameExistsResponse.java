package io.agileintelligence.ppmtool.exceptions;

public class UsernameExistsResponse {
    public String username;

    public UsernameExistsResponse(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
