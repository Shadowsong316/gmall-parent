package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.SkuStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * sku的库存 Mapper 接口
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface SkuStockMapper extends BaseMapper<SkuStock> {

    void updateSkuStock(@Param("pid") Long pid,@Param("skuStockList") List<SkuStock> skuStockList);


}
