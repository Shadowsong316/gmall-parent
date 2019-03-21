package com.atguigu.gmall.pms.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SelectPageUtil {
    public static Map<String, Object> getStringObjectMap(Integer pageSize, IPage<?> selectPage) {
        Map<String, Object> map=new HashMap<>();
        map.put("pageSize",pageSize);
        map.put("totalPage",selectPage.getPages());
        map.put("total",selectPage.getTotal());
        map.put("pageNum",selectPage.getCurrent());
        map.put("list",selectPage.getRecords());
        return map;
    }
}
