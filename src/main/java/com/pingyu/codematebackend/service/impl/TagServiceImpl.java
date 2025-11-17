package com.pingyu.codematebackend.service.impl;

import com.pingyu.codematebackend.model.Tag;
import com.pingyu.codematebackend.service.TagService;
import com.pingyu.codematebackend.mapper.TagMapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
* @author 花萍雨
* @description 针对表【tag(标签表（中心化标签库）)】的数据库操作Service实现
* @createDate 2025-10-26 14:08:46
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




