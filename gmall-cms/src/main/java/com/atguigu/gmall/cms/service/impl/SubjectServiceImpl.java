package com.atguigu.gmall.cms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.cms.entity.Subject;
import com.atguigu.gmall.cms.mapper.SubjectMapper;
import com.atguigu.gmall.cms.service.SubjectService;
import com.atguigu.gmall.util.SelectPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专题表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Override
    public List<Subject> listAll() {
        return baseMapper.selectList(null);
    }

    @Override
    public Map<String, Object> getList(String keyword, Integer pageNum, Integer pageSize) {
        IPage<Subject> selectPage = baseMapper.selectPage(new Page<Subject>(pageNum, pageSize),
                new QueryWrapper<Subject>().like("title", keyword));
        return SelectPageUtil.getStringObjectMap(pageSize,selectPage);
    }
}
