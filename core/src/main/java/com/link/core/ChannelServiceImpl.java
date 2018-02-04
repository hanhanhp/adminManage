package com.link.core;

import com.jfinal.kit.StrKit;
import com.link.api.service.ChannelServiceI;
import com.link.core.base.BaseServiceImpl;
import com.link.model.Channel;

import java.util.List;

/**
 * Created by linkzz on 2017-07-06.
 */
public class ChannelServiceImpl extends BaseServiceImpl implements ChannelServiceI {
    @Override
    public List<Channel> findChannelList() {
        return Channel.dao.find("select * from t_channel t");
    }

    @Override
    public List<Channel> findNavList() {
        List<Channel> list = Channel.dao.find("SELECT * FROM t_channel t WHERE t.type = 'true' AND t.hide = 'true' ORDER BY t.sorter");
        return list;
    }

    @Override
    public Channel getChannal(String id) {
        if (!StrKit.isBlank(id)){
            Channel channel = Channel.dao.findById(id);
            return channel;
        }
        return null;
    }
}
