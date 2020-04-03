package org.ruan.cb.controller;

import org.ruan.cb.model.Channel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ManagerController {

    /**
     * 新增频道
     *
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/channel.add",method = RequestMethod.GET)
    public Object addChannel(HttpServletRequest httpServletRequest){
        Integer id=Integer.parseInt(httpServletRequest.getParameter("id"));
        String name=httpServletRequest.getParameter("name");
        String notice=httpServletRequest.getParameter("notice");
        Channel channel=new Channel(id,name,0,notice);
        //将频道放入redis中

        return true;
    }

    @RequestMapping(value = "/channel.del",method = RequestMethod.GET)
    public Object delChannel(HttpServletRequest httpServletRequest){
        Integer id=Integer.parseInt(httpServletRequest.getParameter("id"));

        return true;
    }
}
