package com.retro_rent.managerApplication.modle;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "message_box")
public class MessageBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private long id;

    @ManyToOne
    private User user;

    @Column(name = "text")
    private String text;

    @Column(name = "status")
    private MessageBoxStatus status;

    @Column(name = "sendTime")
    private Date date;

    @Column(name = "messageTitle")
    private String messageTitle;

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageBoxStatus getStatus() {
        return status;
    }

    public void setStatus(MessageBoxStatus status) {
        this.status = status;
    }
}
