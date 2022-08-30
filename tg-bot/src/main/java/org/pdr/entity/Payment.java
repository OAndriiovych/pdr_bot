package org.pdr.entity;

import org.pdr.utils.DataStructure;

@DataStructure
public class Payment {
    private Integer id;

    private User linkUser;

    private boolean isPaid =  false;


    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getLinkUser() {
        return linkUser;
    }

    public void setLinkUser(User linkUser) {
        this.linkUser = linkUser;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
