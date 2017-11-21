package com.ducetech.app.support.data;

import com.ducetech.app.service.UserService;
import com.ducetech.framework.data.DatabaseDataInitializeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 业务数据初始化处理器
 */
@Component
public class BizDatabaseDataInitializeProcessor extends DatabaseDataInitializeProcessor {

    private final static Logger logger = LoggerFactory.getLogger(BizDatabaseDataInitializeProcessor.class);

    @Autowired
    private UserService userService;

    @Override
    public void initializeInternal() {
        //TODO
    }
}
