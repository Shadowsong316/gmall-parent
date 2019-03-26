package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.entity.HomeBrand;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 首页推荐品牌表 服务类
 * </p>
 *
 * @author Shadowsong
 * @since 2019-03-24
 */
public interface HomeBrandService extends IService<HomeBrand> {

    Map<String,Object> pageBrand(String brandName, Integer recommendStatus, Integer pageNum, Integer pageSize);

    void updateSortById(Long id, Integer sort);

}
