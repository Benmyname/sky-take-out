package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("adminShopController")
@Api(tags = "店铺相关接口")
@RequestMapping("/admin/shop")
public class ShopController {
    public  static final String KEY = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;
        /**
        * 设置店铺状态
        * @param status 店铺状态
        * @return 结果
        */
    @ApiOperation("设置店铺状态")
    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺状态为: {}", status==1?"营业":"关闭");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

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
