package com.nrifintech.cms.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Entity
public class ResetPasswordUUID {
    private String username;
    @Id
    private String uuid;


    
    @Override
    public String toString() {
        return "ResetPasswordUUID [username=" + username + ", uuid=" + uuid + "]";
    }

    
}
