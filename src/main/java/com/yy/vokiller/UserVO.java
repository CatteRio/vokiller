package com.yy.vokiller;

public interface UserVO {

    /**
     * 获取返回值
     * @param user
     * @param age
     * @return
     */
    @SelectVO(vql = "id,name,sex,age")
    Object getUserResponse(User user,Integer age);


    /**
     * 获取名称VO
     * @param user
     * @return
     */
    @SelectVO(vql = "name")
    UserNameVO getUserNameResponse(User user);

}
