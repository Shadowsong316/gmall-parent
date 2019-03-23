package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.ProductVertifyRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品审核记录 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductVertifyRecordService extends IService<ProductVertifyRecord> {

    void updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail);
}
