package com.ducetech.framework.data;

import com.ducetech.app.service.UserService;
import com.ducetech.framework.annotation.MenuData;
import com.ducetech.framework.context.ExtPropertyPlaceholderConfigurer;
import com.ducetech.framework.util.DateUtil;
import com.ducetech.framework.util.Exceptions;
import com.google.common.collect.Sets;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Set;

/**
 * 数据库基础数据初始化处理器
 */
@Component
public class BasicDatabaseDataInitializeProcessor extends DatabaseDataInitializeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(BasicDatabaseDataInitializeProcessor.class);

    @Autowired
    private UserService userService;


    @Autowired
    private ExtPropertyPlaceholderConfigurer extPropertyPlaceholderConfigurer;

    @Override
    public void initializeInternal() {
        logger.info("Running " + this.getClass().getName());
        Date now = DateUtil.currentDate();

        //TODO 用户、角色的初始化

        //权限数据初始化
        rebuildPrivilageDataFromControllerAnnotation();

        //菜单数据初始化
        rebuildMenuDataFromControllerAnnotation();
    }

    /**
     * 基于Controller的@MenuData注解重建菜单基础数据
     */
    private void rebuildMenuDataFromControllerAnnotation() {
        try {
            logger.debug("Start to rebuildMenuDataFromControllerAnnotation...");
            //Date now = DateUtil.currentDate();
            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
            String[] packages = StringUtils.split(ExtPropertyPlaceholderConfigurer.getBasePackages(),
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String pkg : packages) {
                beanDefinitions.addAll(scan.findCandidateComponents(pkg));
            }
            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(this.getClass()));

            for (BeanDefinition beanDefinition : beanDefinitions) {
                String className = beanDefinition.getBeanClassName();
                CtClass cc = pool.get(className);
                CtMethod[] methods = cc.getMethods();
                for (CtMethod method : methods) {
                    MenuData menuData = (MenuData) method.getAnnotation(MenuData.class);
                    if (menuData != null) {
                        String[] paths = menuData.value();
                        Assert.isTrue(paths.length == 1, "Unimplments for multi menu path");
                        String fullpath = paths[0];
                        String[] names = fullpath.split(":");
                        for (int i = 0; i < names.length; i++) {
                            String path = StringUtils.join(names, ":", 0, i + 1);
                            //TODO 保存Menu菜单
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 扫码Spring MVC Controller的所有方法的@RequiresPermissions注解，重建权限基础数据
     */
    private void rebuildPrivilageDataFromControllerAnnotation() {
        try {
            logger.debug("Start to rebuildPrivilageDataFromControllerAnnotation...");
            Date now = DateUtil.currentDate();
            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
            String[] packages = StringUtils.split(ExtPropertyPlaceholderConfigurer.getBasePackages(),
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String pkg : packages) {
                beanDefinitions.addAll(scan.findCandidateComponents(pkg));
            }

            ClassPool pool = ClassPool.getDefault();
            pool.insertClassPath(new ClassClassPath(this.getClass()));

            //合并所有类中所有RequiresPermissions定义信息
            Set<String> mergedPermissions = Sets.newHashSet();
            for (BeanDefinition beanDefinition : beanDefinitions) {
                String className = beanDefinition.getBeanClassName();
                CtClass cc = pool.get(className);
                CtMethod[] methods = cc.getMethods();
                for (CtMethod method : methods) {
                    RequiresPermissions rp = (RequiresPermissions) method.getAnnotation(RequiresPermissions.class);
                    if (rp != null) {
                        String[] perms = rp.value();
                        for (String perm : perms) {
                            mergedPermissions.add(perm);
                        }
                    }
                }
            }
            //TODO
        } catch (Exception e) {
            Exceptions.unchecked(e);
        }
    }
}
