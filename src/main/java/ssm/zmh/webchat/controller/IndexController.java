// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IndexController.java

package ssm.zmh.webchat.controller;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController
{

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    @RequiresUser
    public ModelAndView indexView()
    {
        return new ModelAndView("index");
    }
}
