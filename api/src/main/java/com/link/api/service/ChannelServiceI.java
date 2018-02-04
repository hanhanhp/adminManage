package com.link.api.service;

import com.link.api.service.base.BaseServiceI;
import com.link.model.Channel;

import java.util.List;

/**
 * Created by linkzz on 2017-07-06.
 */
public interface ChannelServiceI extends BaseServiceI {
    public List<Channel> findChannelList();
    public List<Channel> findNavList();
    public Channel getChannal(String id);
}
