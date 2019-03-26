package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.Brand;
import com.atguigu.gmall.pms.mapper.BrandMapper;
import com.atguigu.gmall.pms.service.BrandService;
import com.atguigu.gmall.util.SelectPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Override
    public Map<String, Object> pageBrand(String keyword, Integer pageNum, Integer pageSize) {
        QueryWrapper<Brand> queryWrapper=null;
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper = new QueryWrapper<Brand>().like("name", keyword)
                    .or().eq("first_letter", keyword);
        }
        IPage<Brand> selectPage = baseMapper.selectPage(new Page<Brand>(pageNum, pageSize), queryWrapper);
        Map<String, Object> map = SelectPageUtil.getStringObjectMap(pageSize, selectPage);
        return map;
    }

    @Override
    public List<Brand> listAll() {
        return baseMapper.selectList(null);
    }

    @Override
    public void updateShowStatus(List<Long> ids, Integer showStatus) {
            baseMapper.updateShowStatus(ids, showStatus);

    }

    @Override
    public void updatefactoryStatus(List<Long> ids, Integer factoryStatus) {
        baseMapper.updatefactoryStatus(ids, factoryStatus);
    }

}
