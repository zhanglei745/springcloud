package com.leyou.search.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service",contextId = "CategoryApi")
public interface CategoryClient extends CategoryApi {
}
