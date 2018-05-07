package de.afb.authorization;

public class AuthSession {
    private String accessToken;
    private String refreshToken;
    private String name;
    private String email;
    private Integer userId;
    private Integer employeeId;
    private String role;

    public AuthSession(String accessToken, String refreshToken, String name, String email, Integer userId,
                       Integer employeeId, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.name = name;
        this.email = email;
        this.userId = userId;
        this.employeeId = employeeId;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public String getRole() {
        return role;
    }
}
