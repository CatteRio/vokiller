package com.yy.test;

import com.alibaba.fastjson.JSON;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//        @VoConfigure({"id","name","sex","permissionList"})
//        User user = new User();
//        user.setName("yuanyang");
//        user.setId(1);
//        user.setSex("11");
//        user.setBirthday(new Date());
//        List<String> list = new ArrayList<String>();
//        user.setPermissionList(list);
//        list.add("123");
//        list.add("321");
//        Map<String, Object> propertyMap = new HashMap<String, Object>(16);
//        propertyMap.put("age", 7);
//
//
//
//        System.out.println(JSON.toJSONString(VoKiller.getVO(User.class, user, "response1")));
//        System.out.println(JSON.toJSONString(VoKiller.getVO(User.class, user, "response2")));
//        System.out.println(JSON.toJSONString(VoKiller.getVO(User.class, user, "response2", propertyMap)));

        List<String> list = new ArrayList<String>();
        list.add("123");
        list.add("321");
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("beans.xml");
        UserVOAssembler userVO = (UserVOAssembler) app.getBean(UserVOAssembler.class);
        User user = new User();
        user.setBirthday(new Date());
        user.setId(1);
        user.setName("yuanyang");
        user.setSex("nan");
//        System.out.println(userVO.getUserResponse(new User(), 1, list, list));
        System.out.println(JSON.toJSONString(userVO.getUserNameObjectVO(user, "123","234")));
        System.out.println(JSON.toJSONString(userVO.getUserNameObjectVO(user)));
        System.out.println(JSON.toJSONString(userVO.getUserNameResponse(user)));
    }
}
