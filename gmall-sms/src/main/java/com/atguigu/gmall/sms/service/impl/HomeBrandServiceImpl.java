package com.atguigu.gmall.sms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.sms.entity.HomeBrand;
import com.atguigu.gmall.sms.mapper.HomeBrandMapper;
import com.atguigu.gmall.sms.service.HomeBrandService;
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
 * 首页推荐品牌表 服务实现类
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-24
 */
@Component
@Service
public class HomeBrandServiceImpl extends ServiceImpl<HomeBrandMapper, HomeBrand> implements HomeBrandService {

    @Override
    public Map<String, Object> pageBrand(String brandName, Integer recommendStatus, Integer pageNum, Integer pageSize) {
        QueryWrapper<HomeBrand> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(brandName)) {
            queryWrapper.like("brand_name", brandName);
        }
        if (!StringUtils.isEmpty(recommendStatus)) {
            queryWrapper.eq("recommend_status", recommendStatus);
        }
        IPage<HomeBrand> selectPage = baseMapper.selectPage(new Page<HomeBrand>(pageNum, pageSize), queryWrapper);
        return SelectPageUtil.getStringObjectMap(pageSize, selectPage);
    }

    @Override
    public void updateSortById(Long id, Integer sort) {
        HomeBrand homeBrand = new HomeBrand();
        homeBrand.setId(id);
        homeBrand.setSort(sort);
        baseMapper.updateById(homeBrand);
    }


}
