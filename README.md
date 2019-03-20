# vokiller
## 介绍
在java接口开发中需要根据前端需求，对不同接口需要增设不同类型的VO类，十分繁琐，代码累赘，本项目为了解决这个问题参考了mybatis的实现，采用了动态代理的方式来解决代码冗余，可以使VO类最终达到用注解配置，方便代码量，易于代码维护。
## 如何使用
#### 1.Spring集成
添加maven依赖项
```html
<dependency>
    <groupId>com.github.kyrotiko</groupId>
    <artifactId>vokiller</artifactId>
    <version>${version}</version>
</dependency>
```
引入项目后集成Spring框架，配置扫描包路径，该包下存放动态VO接口类
```html
<bean id="voScannerConfigurer" class="com.yy.vokiller.core.VoScannerConfigurer">
        <property name="basePackage" value="com.yy.test"/>
</bean>
```
#### 2.添加动态接口类
根据接口需求添加不同的接口方法，采用注解的方式声明接口变量参数值等信息，使用注解设置vql可以自定义结构，具体语法参看下例，基本涵盖所有语法。
```java
package com.yy.test;

import com.yy.vokiller.annotation.SelectVO;
import com.yy.vokiller.annotation.VoParam;

import java.util.List;

public interface UserVOAssembler {

    @SelectVO(vql = "user(sex=age,age,permissionList=permission),role")
    Object getUserResponse(@VOParam(value = "user", exclude = {"name"}) User user,
                           @VOParam(value = "age") Integer age,
                           @VOParam(value = "permission") List<String> permission,
                           @VOParam(value = "role") List<String> role);

    @SelectVO(vql = "user(sex=ss,age)")
    Object getUserResponse(@VOParam(value = "user", exclude = {"name"}) User user,
                           @VOParam("ss") String sex,
                           @VOParam("age") Integer age);

    @SelectVO()
    Object getUserNameObjectVO(@VOParam(value = "userInfo", exclude = {"name"}) User user,
                               @VOParam("height") String height,
                               @VOParam("weight") String weight);

    @SelectVO()
    Object getUserNameObjectVOAll(@VOParam(value = "userInfo") User user,
                                  @VOParam("height") String height,
                                  @VOParam("weight") String weight);

    @SelectVO()
    Object getUserNameObjectVO(@VOParam(include = {"name"}) User user);

    @SelectVO()
    UserNameVO getUserNameResponse(@VOParam(value = "user", include = {"name"}) User user, @VOParam("sex") String sex);

    @SelectVO()
    UserNameVO getUserNameResponse(@VOParam(include = {"name"}) User user);

    @SelectVO()
    UserNameVO getUserNameResponseAll(User user);


    @SelectVO()
    Object getUserNameResponseAllObject(User user);

}


```
#### 3.使用
根据需求调用不用的接口方法，在代码中使用即可。
```java
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("beans.xml");
        UserVOAssembler userVO = (UserVOAssembler) app.getBean(UserVOAssembler.class);
        List<String> list = new ArrayList<String>();
        list.add("123");
        list.add("321");
        User user = new User();
        user.setBirthday(new Date());
        user.setId(1);
        user.setName("yuanyang");
        user.setSex("nan");
        user.setPermissionList(list);
        System.out.println(JSON.toJSONString(userVO.getUserResponse(user, 123, list, list)));
        System.out.println(JSON.toJSONString(userVO.getUserResponse(user, "123", 123)));
        System.out.println(JSON.toJSONString(userVO.getUserNameResponseAll(user)));
        System.out.println(JSON.toJSONString(userVO.getUserNameResponse(user)));
        System.out.println(JSON.toJSONString(userVO.getUserNameObjectVOAll(user, "123", "234")));
        System.out.println(JSON.toJSONString(userVO.getUserNameObjectVO(user, "123", "234")));
        System.out.println(JSON.toJSONString(userVO.getUserNameObjectVO(user)));
        System.out.println(JSON.toJSONString(userVO.getUserNameResponseAllObject(user)));
```
输出示例
```json
{"role":["123","321"],"user":{"age":123,"birthday":1553065635955,"id":1,"permissionList":["123","321"],"sex":123}}
{"age":123,"birthday":1553065635955,"id":1,"permissionList":["123","321"],"sex":"123"}
{"name":"yuanyang","sex":"nan"}
{"name":"yuanyang"}
{"height":"123","userInfo":{"birthday":1553065635955,"id":1,"name":"yuanyang","permissionList":["123","321"],"sex":"nan"},"weight":"234"}
{"height":"123","userInfo":{"birthday":1553065635955,"id":1,"permissionList":["123","321"],"sex":"nan"},"weight":"234"}
{"name":"yuanyang"}
{"birthday":1553065635955,"id":1,"name":"yuanyang","permissionList":["123","321"],"sex":"nan"}
```

## 后续问题
在分布式应用中还需要解决序列化问题，待以后再来处理


有问题可以联系我：417168602@qq.com
