package com.yy.test;

import com.yy.annotation.SelectVO;
import com.yy.vokiller.UserNameVO;

public interface UserVO {

    /**
     * 获取返回值
     * @param user
     * @param age
     * @return
     */
    @SelectVO(vql = "id,name,sex,age")
    Object getUserResponse(User user, Integer age);


    /**
     * 获取名称VO
     * @param user
     * @return
     */
    @SelectVO(vql = "name")
    UserNameVO getUserNameResponse(User user);

}
