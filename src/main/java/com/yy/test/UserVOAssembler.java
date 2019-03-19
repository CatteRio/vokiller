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
    @SelectVO(vql = "user(sex=age,age,permissionList=permission),role")
    Object getUserResponse(@VOParam(value = "user", exclude = {"name"}) User user,
                           @VOParam(value = "age") Integer age,
                           @VOParam(value = "permission") List<String> permission,
                           @VOParam(value = "role") List<String> role);

    @SelectVO(vql = "user(sex=ss,age)")
    Object getUserResponse(@VOParam(value = "user", exclude = {"name"}) User user,
                           @VOParam("ss") String sex,
                           @VOParam("age") Integer age);

    /**
     * 获取名称VO
     *
     * @param user
     * @return
     */
    @SelectVO()
    Object getUserNameObjectVO(@VOParam(value = "userInfo", exclude = {"name"}) User user,
                               @VOParam("height") String height,
                               @VOParam("weight") String weight);

    /**
     * 获取名称VO
     *
     * @param user
     * @return
     */
    @SelectVO()
    Object getUserNameObjectVOAll(@VOParam(value = "userInfo") User user,
                                  @VOParam("height") String height,
                                  @VOParam("weight") String weight);

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
    UserNameVO getUserNameResponse(@VOParam(value = "user", include = {"name"}) User user, @VOParam("sex") String sex);

    /**
     * 获取名称VO
     *
     * @param user
     * @return
     */
    @SelectVO()
    UserNameVO getUserNameResponse(@VOParam(include = {"name"}) User user);

    /**
     * 获取名称VO
     *
     * @param user
     * @return
     */
    @SelectVO()
    UserNameVO getUserNameResponseAll(User user);

}
