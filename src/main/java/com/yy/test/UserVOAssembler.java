package com.yy.test;

import com.yy.vokiller.annotation.SelectVO;
import com.yy.vokiller.annotation.VOParam;

import java.util.List;

public interface UserVOAssembler {

    /**
     * 获取对象返回值
     *
     * @param user
     * @param age
     * @param permission
     * @param role
     * @return
     */
    @SelectVO(vql = "user(sex=男,age,permissionList=permission),role")
    Object getUserResponse(@VOParam(value = "user", exclude = {"name"}) User user,
                           @VOParam(value = "age") Integer age,
                           @VOParam(value = "permission") List<String> permission,
                           @VOParam(value = "role") List<String> role);

    /**
     * 获取名称VO
     *
     * @param user
     * @return
     */
    @SelectVO()
    Object getUserNameObjectVO(@VOParam(value = "user", include = {"name"}) User user,
                               @VOParam("height") String height);

    /**
     * 获取名称VO
     *
     * @param user
     * @return
     */
    @SelectVO()
    Object getUserNameObjectVO(@VOParam(include = {"name"}) User user);

    /**
     * 获取名称VO
     *
     * @param user
     * @return
     */
    @SelectVO()
    UserNameVO getUserNameResponse(@VOParam(include = {"name"}) User user);

}
