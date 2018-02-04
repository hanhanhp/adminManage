package com.link.core;

import com.jfinal.kit.StrKit;
import com.link.api.service.UserServiceI;
import com.link.common.util.ResultJson;
import com.link.core.base.BaseServiceImpl;
import com.link.model.User;

/**
 * Created by linkzz on 2017-05-26.
 */
public class UserServiceImpl extends BaseServiceImpl implements UserServiceI{
    @Override
    public User getUser(String id) {
        if (!StrKit.isBlank(id)){
            User user = User.dao.findById(id);
            return user;
        }else {
            return null;
        }
    }

    @Override
    public User getUserByUserName(String userName) {
        if (!StrKit.isBlank(userName)){
            User user = User.dao.findFirst("select * from t_user t where t.username = ?",userName);
            return user;
        }else {
            return null;
        }
    }

}
