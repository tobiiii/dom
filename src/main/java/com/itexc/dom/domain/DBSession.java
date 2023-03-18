package com.itexc.dom.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class DBSession extends CommonEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_dbsession_user"))
    private User user;

    private String email;

    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String token;

    private String ipAddress;

    private Date logonTime;

    private String language;

    private boolean isActive;

    private boolean isBlocked;


    private Date logoutTime;

    private String fullName;

    @Override
    public String toString() {
        return "DBSession{" +
                "email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", logonTime=" + logonTime +
                ", language='" + language + '\'' +
                ", isActive=" + isActive +
                ", isBlocked=" + isBlocked +
                ", logoutTime=" + logoutTime +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
