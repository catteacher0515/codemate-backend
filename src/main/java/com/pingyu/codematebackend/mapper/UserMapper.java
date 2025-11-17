package com.pingyu.codematebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pingyu.codematebackend.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 确保导入 @Param
import java.util.List;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 【已修改】根据传入的所有标签名称，查询同时拥有这些标签的用户
     * @param tagNameList 标签名称列表
     * @param listSize 标签列表的大小 (手动传入以避免 Mybatis bug)
     * @return 用户列表
     */
    List<User> findUsersByAllTags(@Param("tagNameList") List<String> tagNameList,
                                  @Param("listSize") int listSize); // <-- 关键：添加了第二个参数
}