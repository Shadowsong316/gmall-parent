package com.atguigu.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.pms.entity.ProductVertifyRecord;
import com.atguigu.gmall.pms.mapper.ProductVertifyRecordMapper;
import com.atguigu.gmall.pms.service.ProductVertifyRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 商品审核记录 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
@Service
@Component
public class ProductVertifyRecordServiceImpl extends ServiceImpl<ProductVertifyRecordMapper, ProductVertifyRecord> implements ProductVertifyRecordService {

    @Override
    public void updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        baseMapper.updateVerifyStatus(ids,verifyStatus,detail);
    }
}
