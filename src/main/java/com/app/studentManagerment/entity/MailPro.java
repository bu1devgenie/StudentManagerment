package com.app.studentManagerment.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mailPro")
public class MailPro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "host", nullable = false)
    private String host;

    @Column(name = "port", nullable = false)
    private int port;
    @Column(name = "senderName", nullable = false)
    private String senderName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "smtp_auth", nullable = false)
    private String smtp_auth;
    @Column(name = "smtp_starttls_enable", nullable = false)
    private String smtp_starttls_enable;

    @Enumerated(EnumType.STRING)
    private Working working;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public enum Working {
        ACTIVITY, STOP
    }

    public MailPro() {
    }

    public MailPro(Long id, String host, int port, String senderName, String username, String password, String smtp_auth, String smtp_starttls_enable, Role role) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.senderName = senderName;
        this.username = username;
        this.password = password;
        this.smtp_auth = smtp_auth;
        this.smtp_starttls_enable = smtp_starttls_enable;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmtp_auth() {
        return smtp_auth;
    }

    public void setSmtp_auth(String smtp_auth) {
        this.smtp_auth = smtp_auth;
    }

    public String getSmtp_starttls_enable() {
        return smtp_starttls_enable;
    }

    public void setSmtp_starttls_enable(String smtp_starttls_enable) {
        this.smtp_starttls_enable = smtp_starttls_enable;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Working getWorking() {
        return working;
    }

    public void setWorking(Working working) {
        this.working = working;
    }

    @Override
    public String toString() {
        return "MailPro{" +
               "id=" + id +
               ", host='" + host + '\'' +
               ", port=" + port +
               ", senderName='" + senderName + '\'' +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", smtp_auth='" + smtp_auth + '\'' +
               ", smtp_starttls_enable='" + smtp_starttls_enable + '\'' +
               ", working=" + working +
               ", role=" + role +
               '}';
    }
}
