package com.link.api.service;

import com.link.api.service.base.BaseServiceI;
import com.link.model.User;

/**
 * Created by linkzz on 2017-05-26.
 */
public interface UserServiceI extends BaseServiceI {

    /**
     * @Description: 获取用户详情
     * @author: linkzz
     * @data: 2017-05-09 10:56
     */
    public User getUser(String id);

    public User getUserByUserName(String userName);

}
