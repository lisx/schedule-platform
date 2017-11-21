package com.ducetech.framework.data;

import com.ducetech.framework.support.service.DynamicConfigService;
import com.ducetech.framework.util.DateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 数据库数据初始化处理器基类
 */
public abstract class DatabaseDataInitializeProcessor {

    private final static Logger logger = LoggerFactory.getLogger(DatabaseDataInitializeProcessor.class);

    private SqlSessionFactory sqlSessionFactory;

    public void initialize(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        logger.debug("Invoking data process for {}", this);
        initializeInternal();
        //确保最后提交事务
        commitAndResumeTransaction();
        if (DynamicConfigService.isDevMode()) {
            //重置恢复模拟数据设置的临时时间
            DateUtil.setCurrentDate(null);
        }
    }

    /**
     * 帮助类方法，从当前类的classpath路径下面读取文本内容为String字符串
     * @param fileName 文件名称
     * @return
     */
    protected String getStringFromTextFile(String fileName) {
        InputStream is = this.getClass().getResourceAsStream(fileName);
        try {
            String text = IOUtils.toString(is, "UTF-8");
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    protected int executeNativeSQL(String sql) {
        //TODO
        return 0;
    }

    /**
     * 查询整个数据对象表
     */
    @SuppressWarnings("unchecked")
    protected <X> List<X> findAll(Class<X> entity) {
        //TODO
        return null;
    }

    /**
     * 获取表数据总记录数
     */
    protected int countTable(Class<?> entity) {
        //TODO
        return 0;
    }

    /**
     * 判定实体对象对应表是否为空
     */
    protected boolean isEmptyTable(Class<?> entity) {
        //TODO
        return true;
    }

    /**
     * 提交当前事务并新起一个事务
     */
    protected void commitAndResumeTransaction() {
        //TODO
    }

    public abstract void initializeInternal();
}
