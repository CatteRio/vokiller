package com.yy.test;

import com.yy.vokiller.annotation.SelectVO;
import com.yy.vokiller.annotation.VoParam;

import java.util.List;

public interface UserVOAssembler {

    @SelectVO(vql = "user(sex=age,age,permissionList=permission),role")
    Object getUserResponse(@VoParam(value = "user", exclude = {"name"}) User user,
                           @VoParam(value = "age") Integer age,
                           @VoParam(value = "permission") List<String> permission,
                           @VoParam(value = "role") List<String> role);

    @SelectVO(vql = "user(sex=ss,age)")
    Object getUserResponse(@VoParam(value = "user", exclude = {"name"}) User user,
                           @VoParam("ss") String sex,
                           @VoParam("age") Integer age);

    @SelectVO()
    Object getUserNameObjectVO(@VoParam(value = "userInfo", exclude = {"name"}) User user,
                               @VoParam("height") String height,
                               @VoParam("weight") String weight);

    @SelectVO()
    Object getUserNameObjectVOAll(@VoParam(value = "userInfo") User user,
                                  @VoParam("height") String height,
                                  @VoParam("weight") String weight);

    @SelectVO()
    Object getUserNameObjectVO(@VoParam(include = {"name"}) User user);

    @SelectVO()
    UserNameVO getUserNameResponse(@VoParam(value = "user", include = {"name"}) User user,
                                   @VoParam("sex") String sex);

    @SelectVO()
    UserNameVO getUserNameResponse(@VoParam(include = {"name"}) User user);

    @SelectVO()
    UserNameVO getUserNameResponseAll(User user);


    @SelectVO()
    Object getUserNameResponseAllObject(User user);

}
