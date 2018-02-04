package com.link.common.util;

import java.util.List;

/**
 * Created by linkzz on 2017-05-12.
 */
public class Groups {
    public String groupOp;
    public List<Rules> rules;
    public List<Groups> groups;

    public String getGroupOp() {
        return groupOp;
    }

    public void setGroupOp(String groupOp) {
        this.groupOp = groupOp;
    }

    public List<Rules> getRules() {
        return rules;
    }

    public void setRules(List<Rules> rules) {
        this.rules = rules;
    }

    public List<Groups> getGroups() {
        return groups;
    }

    public void setGroups(List<Groups> groups) {
        this.groups = groups;
    }
}
