package com.fly.basic.api;

import com.fly.basic.domain.FlyConfig;
import com.fly.basic.domain.PageData;

public interface IFlyconfig
{

    PageData<FlyConfig> getFlyConfigList(FlyConfig config, Integer pageNum, Integer pageSize);

    void updateConfig(FlyConfig config);

    void insertConfig(FlyConfig config);

    void delConfig(String ids);

}
