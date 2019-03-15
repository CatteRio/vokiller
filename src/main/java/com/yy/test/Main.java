package com.yy.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

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
//


        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("beans.xml");
        UserVO userVO = (UserVO) app.getBean(UserVO.class);
        userVO.getUserNameResponse(new User());
    }
}
