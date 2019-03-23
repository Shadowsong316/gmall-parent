package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品信息 Mapper 接口
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductMapper extends BaseMapper<Product> {

    void updateNewStatus(@Param("ids") List<Long> ids,@Param("newStatus") Integer newStatus);

    void updatePublishStatus(@Param("ids")List<Long> ids,@Param("publishStatus") Integer publishStatus);

    void updateRecommendStatus(@Param("ids")List<Long> ids,@Param("recommendStatus") Integer recommendStatus);

    void updateDeleteStatus(@Param("ids")List<Long> ids,@Param("deleteStatus") Integer deleteStatus);
}
