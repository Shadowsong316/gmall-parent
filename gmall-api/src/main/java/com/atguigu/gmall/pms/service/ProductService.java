package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.entity.Product;
import com.atguigu.gmall.pms.vo.PmsProductQueryParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductService extends IService<Product> {

    Map<String,Object> pageProduct(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);

    List<Product> getList(String keyword);

    void updateNewStatus(List<Long> ids, Integer newStatus);

    void updatePublishStatus(List<Long> ids, Integer publishStatus);

    void updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    void updateDeleteStatus(List<Long> ids, Integer deleteStatus);
}
