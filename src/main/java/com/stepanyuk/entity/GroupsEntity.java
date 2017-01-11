package com.stepanyuk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "groups", schema = "library")
public class GroupsEntity implements Serializable {
    private String groupid;

    @Id
    @Column(name = "GROUPID", nullable = false, length = 20)
    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupsEntity that = (GroupsEntity) o;

        if (groupid != null ? !groupid.equals(that.groupid) : that.groupid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return groupid != null ? groupid.hashCode() : 0;
    }
}
