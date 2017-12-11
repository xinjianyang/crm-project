package com.kaishengit.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class UserDeptKey implements Serializable {
    private Integer did;

    private Integer uid;

    private static final long serialVersionUID = 1L;

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}