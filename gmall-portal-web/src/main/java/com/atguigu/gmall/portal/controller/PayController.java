package com.atguigu.gmall.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.atguigu.gmall.oms.service.OrderAndPayService;
import com.atguigu.gmall.portal.config.AlipayConfig;
import com.atguigu.gmall.to.OrderStatusEnum;
import com.atguigu.gmall.vo.OrderResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {
    @Reference
    OrderAndPayService orderAndPayService;
    @ResponseBody
    @RequestMapping("/order")
    public String payOrder(OrderResponseVo orderResponseVo){

        String payHtml = orderAndPayService.payMyOrder(
                orderResponseVo.getOut_trade_no(),
                orderResponseVo.getTotal_amount(),
                orderResponseVo.getSubject(),
                orderResponseVo.getBody());

        return payHtml;
    }

    /**
     * 异步通知支付宝会调用很多次
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping("/async/success")
    public String paySuccess(HttpServletRequest request) throws UnsupportedEncodingException {
        log.debug("支付宝支付异步通知完成....");
        // 修改订单的状态
        // 支付宝收到了success说明处理完成，不会再通知

        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = true;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset,
                    AlipayConfig.sign_type);
            System.out.println("验签：" + signVerified);

        } catch (AlipayApiException e) {
            System.out.println("验签失败");
        }
        // 商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        // 支付宝流水号
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
        // 交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

        if (trade_status.equals("TRADE_FINISHED")) {
            //改订单状态
            orderAndPayService.updateOrderStatus(out_trade_no,OrderStatusEnum.FINISHED);
            //流水日志记录表

            log.debug("订单【{}】,已经完成...不能再退款。数据库都改了",out_trade_no);
        } else if (trade_status.equals("TRADE_SUCCESS")) {
            orderAndPayService.updateOrderStatus(out_trade_no,OrderStatusEnum.PAYED);

            log.debug("订单【{}】,已经支付成功...可以退款。数据库都改了",out_trade_no);
        }

        return "success";


    }


}
