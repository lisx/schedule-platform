package com.ducetech.framework.data;

import com.ducetech.framework.annotation.MetaData;
import com.ducetech.framework.context.ExtPropertyPlaceholderConfigurer;
import com.ducetech.framework.util.DateUtil;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shiro.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 数据库数据初始化处理器执行器
 */
@Component
@Transactional
public class DatabaseDataInitializeExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseDataInitializeExecutor.class);

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private List<DatabaseDataInitializeProcessor> initializeProcessors;

    @Transactional
    public void initialize() {
        CountThread countThread = new CountThread();
        countThread.start();
        {
            //搜索所有entity对象，并自动进行自增初始化值设置
            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(MetaData.class));
            String[] packages = StringUtils.split(ExtPropertyPlaceholderConfigurer.getBasePackages(),
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String pkg : packages) {
                beanDefinitions.addAll(scan.findCandidateComponents(pkg));
            }
            for (BeanDefinition beanDefinition : beanDefinitions) {
                Class<?> entityClass = ClassUtils.forName(beanDefinition.getBeanClassName());
                MetaData metaData = entityClass.getAnnotation(MetaData.class);
                if (metaData != null && metaData.autoIncrementInitValue() > 0) {
                    //TODO
                    autoIncrementInitValue(entityClass, sqlSessionFactory);
                }
            }
        }

        for (DatabaseDataInitializeProcessor initializeProcessor : initializeProcessors) {
            logger.debug("Invoking data initialize for {}", initializeProcessor);
            countThread.update(initializeProcessor.getClass());
            initializeProcessor.initialize(sqlSessionFactory);
        }
        //停止计数线程
        countThread.shutdown();
    }

    /**
     * 初始化自增对象起始值
     */
    public static void autoIncrementInitValue(final Class<?> entity, SqlSessionFactory factory) {
        SqlSession session = factory.openSession();
        Connection connection = session.getConnection();
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String name = databaseMetaData.getDatabaseProductName().toLowerCase();
            if (name.indexOf("mysql") > -1) {
                //TODO
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    private static class CountThread extends Thread {

        private Class<?> runnerClass;
        private boolean running = true;
        private Date startTime = new Date();

        @Override
        public void run() {
            while (running) {
                try {
                    if (runnerClass != null) {
                        Date now = new Date();
                        logger.info("Running at " + runnerClass.getName() + ". Total passed time " + DateUtil.getHumanDisplayForTimediff(now.getTime() - startTime.getTime()) + " at Thread: " + Thread.currentThread().getId());
                    }
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            super.run();
        }

        public void update(Class<?> runnerClass) {
            this.runnerClass = runnerClass;
        }

        public void shutdown() {
            logger.info("Shutdowning DatabaseDataInitializeTrigger CountThread: " + Thread.currentThread().getId());
            running = false;
        }
    }
}
