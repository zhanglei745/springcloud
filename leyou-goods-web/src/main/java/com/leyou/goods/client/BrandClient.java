package com.leyou.goods.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service",contextId = "BrandApi")
public interface BrandClient extends BrandApi {
}
