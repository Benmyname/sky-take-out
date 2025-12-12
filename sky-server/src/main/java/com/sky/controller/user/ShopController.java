package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("userShopController")
@Api(tags = "店铺相关接口")
@RequestMapping("/user/shop")
public class ShopController {
    public  static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 获取店铺状态
     * @return 店铺状态
     */
    @ApiOperation("获取店铺状态")
    @GetMapping("/status")//无参数
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺状态为: {}", status==1?"营业":"关闭");
        return Result.success(status);
    }
}
