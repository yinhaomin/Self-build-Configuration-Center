package com.baidu.hui.biz.database.properties;

import java.sql.ResultSet;
import java.util.Date;
import java.util.Properties;

import javax.sql.DataSource;

import lombok.Data;
import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.baidu.hui.base.utils.DateUtils;
import com.baidu.hui.biz.constance.CrawlTemplateConstant;
import com.baidu.hui.biz.utils.SpringContextUtil;
import com.baidu.hui.dao.dao.CrawlerTemplateDAO;

/**
 * 从数据库里读取配置
 * 
 */
@SuppressWarnings("rawtypes")
@Log4j
@Data
public class DatabaseProperties implements InitializingBean, FactoryBean {

    private CrawlerTemplateDAO crawlerTemplateDAO;

    private SpringContextUtil springContextUtil;

    // 将数据库中的配置存放到placeholder 的property
    private Properties properties = new Properties();

    public DatabaseProperties(CrawlerTemplateDAO crawlerTemplateDAO, SpringContextUtil springContextUtil) {
        this.crawlerTemplateDAO = crawlerTemplateDAO;
        this.springContextUtil = springContextUtil;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initProperties();
    }

    @Override
    public Object getObject() throws Exception {
        return properties;
    }

    @Override
    public Class getObjectType() {
        return Properties.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private void initProperties() {
        DataSource dataSource = (DataSource) springContextUtil.getBean("icHuiDataSource1");
        try {
            ResultSet rs = crawlerTemplateDAO.getResultSet(dataSource.getConnection());
            while (rs.next()) {
                String[] propertyNames = new String[CrawlTemplateConstant.PROPERTIES_NAMES.length];
                String keyPrefix = CrawlTemplateConstant.DEFAULT_TEMPLATE_NAME;
                String beanName = rs.getString(2);
                if (beanName.equals(CrawlTemplateConstant.JD_PC_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.JD_PC_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.AMAZON_DP_PC_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.AMAZON_DP_PC_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.DD_PC_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.DD_PC_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.YHD_PC_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.YHD_PC_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.GM_PC_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.GM_PC_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.SN_PC_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.SN_PC_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.WPH_PC_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.WPH_PC_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.DEFAULT_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.DEFAULT_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.YHD_TUAN_PC_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.YHD_TUAN_PC_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.GM_TUAN_PC_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.GM_TUAN_PC_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.GM_Q_PC_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.GM_Q_PC_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.YX_PC_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.YX_PC_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.AMZON_GP_PC_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.AMZON_GP_PC_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.GM_ITEM_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.GM_ITEM_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.JD_MOBILE_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.JD_MOBILE_TEMPLATE_NAME;
                } else if (beanName.equals(CrawlTemplateConstant.YHD_MOBILE_TEMPLATE_NAME)) {
                    keyPrefix = CrawlTemplateConstant.YHD_MOBILE_TEMPLATE_NAME;
                }

                for (int i = 0; i < CrawlTemplateConstant.PROPERTIES_NAMES.length; i++) {
                    propertyNames[i] = keyPrefix + CrawlTemplateConstant.PROPERTIES_NAMES[i];
                }
                for (int i = 2; i < CrawlTemplateConstant.PROPERTIES_NAMES.length - 1; i++) {
                    String key = propertyNames[i];
                    String value = rs.getString(i);
                    properties.setProperty(key, value);
                }
                String key = propertyNames[CrawlTemplateConstant.PROPERTIES_NAMES.length - 1];
                String value = rs.getString(CrawlTemplateConstant.PROPERTIES_NAMES.length - 1);
                Date date = DateUtils.parse(value);
                properties.setProperty(key, date.getTime() + "");
            }
            rs.close();
        } catch (Exception e) {
            log.error(e);
        }
    }
}
