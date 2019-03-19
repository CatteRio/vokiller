package com.yy.test;

import com.yy.vokiller.annotation.SelectVO;
import com.yy.vokiller.annotation.VOParam;

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

}
