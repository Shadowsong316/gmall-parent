package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.ProductVertifyRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品审核记录 Mapper 接口
 * </p>
 *
 * @author Lfy
 * @since 2019-03-19
 */
public interface ProductVertifyRecordMapper extends BaseMapper<ProductVertifyRecord> {

    void updateVerifyStatus(@Param("ids") List<Long> ids,@Param("verifyStatus") Integer verifyStatus,@Param("detail") String detail);
}
