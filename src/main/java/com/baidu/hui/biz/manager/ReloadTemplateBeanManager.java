package com.baidu.hui.biz.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.baidu.hui.biz.constance.CrawlTemplateConstant;
import com.baidu.hui.biz.constance.HtmlAttribute;
import com.baidu.hui.biz.database.properties.DatabaseProperties;
import com.baidu.hui.biz.vo.UrlTemplate;
import com.baidu.hui.dao.dao.CrawlerTemplateDAO;
import com.baidu.hui.redis.factory.RedisFactory;
import com.baidu.hui.redis.serializer.MessageSerializer;

@Log4j
@Component
public class ReloadTemplateBeanManager implements ApplicationContextAware {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DatabaseProperties databaseProperties;

    @Autowired
    DefaultListableBeanFactory beanFactory;

    @Autowired
    private RedisFactory redisFactory;

    @Autowired
    @Qualifier(value = "jsonMessageSerializer")
    private MessageSerializer messageSerializer;

    @Autowired
    private CrawlerTemplateDAO crawlerTemplateDAO;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 更新容器内的template bean的属性
     */
    public void refreshContext() {
        try {
            databaseProperties.afterPropertiesSet();
        } catch (Exception e) {
            log.error("Error in databaseProperties.afterPropertiesSet(): ", e);
        }

        Properties properties = databaseProperties.getProperties();

        for (String beanName : CrawlTemplateConstant.CRAWL_TEMPLATE_NAMES) {
            UrlTemplate urlTemplate = null;
            try {
                urlTemplate = (UrlTemplate) ((ConfigurableApplicationContext) applicationContext).getBean(beanName);
                String keyPrefix = CrawlTemplateConstant.DEFAULT_TEMPLATE_NAME;
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
                try {
                    urlTemplate.setId(Long.parseLong(properties.get(keyPrefix + CrawlTemplateConstant.TEMPLATE_ID)
                            .toString()));
                    urlTemplate.setDomain(properties.get(keyPrefix + CrawlTemplateConstant.DOMAIN).toString());
                    urlTemplate.setMerchant(properties.get(keyPrefix + CrawlTemplateConstant.MERCHANT).toString());
                    urlTemplate.setSecendLevelDomain(properties.get(
                            keyPrefix + CrawlTemplateConstant.SECOND_LEVEL_DOMAIN).toString());
                    Map<HtmlAttribute, String> xpaths = generateXpath(keyPrefix, properties);
                    urlTemplate.setXpaths(xpaths);
                    urlTemplate.setOutIdPatten(properties.get(keyPrefix + CrawlTemplateConstant.OUT_ID_PATTERN)
                            .toString());
                    urlTemplate.setPattenIndex(Integer.parseInt(properties.get(
                            keyPrefix + CrawlTemplateConstant.PATTERN_INDEX).toString()));
                    urlTemplate.setHtmlCharset(properties.get(keyPrefix + CrawlTemplateConstant.HTML_CHARSET)
                            .toString());
                    urlTemplate.setUpdateTime(System.currentTimeMillis());
                } catch (Exception e) {
                    log.error("Failed to set properties:", e);
                }
            } catch (Exception e) {
                log.error("Error in reolad:", e);
            }
        }
        // ((ConfigurableApplicationContext) applicationContext).refresh();
    }

    /**
     * 为template生成xpath
     * 
     * @param keyPrefix
     * @param properties
     * @return
     */
    private Map<HtmlAttribute, String> generateXpath(String keyPrefix, Properties properties) {
        Map<HtmlAttribute, String> map = new HashMap<HtmlAttribute, String>();
        try {
            map.put(HtmlAttribute.TITLE, properties.getProperty(keyPrefix + CrawlTemplateConstant.TITLE));
        } catch (Exception e) {
            log.error("Failed to get the property:", e);
        }
        try {
            map.put(HtmlAttribute.PRICE, properties.getProperty(keyPrefix + CrawlTemplateConstant.PRICE));
        } catch (Exception e) {
            log.error("Failed to get the property:", e);
        }
        try {
            map.put(HtmlAttribute.IMAGES, properties.getProperty(keyPrefix + CrawlTemplateConstant.IMAGES));
        } catch (Exception e) {
            log.error("Failed to get the property:", e);
        }
        try {
            map.put(HtmlAttribute.VALUE, properties.getProperty(keyPrefix + CrawlTemplateConstant.VALUE));
        } catch (Exception e) {
            log.error("Failed to get the property:", e);
        }
        try {
            map.put(HtmlAttribute.BRAND, properties.getProperty(keyPrefix + CrawlTemplateConstant.BRAND));
        } catch (Exception e) {
            log.error("Failed to get the property:", e);
        }
        try {
            map.put(HtmlAttribute.STOCK, properties.getProperty(keyPrefix + CrawlTemplateConstant.STOCK));
        } catch (Exception e) {
            log.error("Failed to get the property:", e);
        }
        try {
            map.put(HtmlAttribute.CATEGORY, properties.getProperty(keyPrefix + CrawlTemplateConstant.CATEGORY));
        } catch (Exception e) {
            log.error("Failed to get the property:", e);
        }
        try {
            map.put(HtmlAttribute.SUBCATEGORY, properties.getProperty(keyPrefix + CrawlTemplateConstant.SUB_CATEGORY));
        } catch (Exception e) {
            log.error("Failed to get the property:", e);
        }
        try {
            map.put(HtmlAttribute.THIRDCATEGORY, properties.getProperty(keyPrefix
                    + CrawlTemplateConstant.THIRD_CATEGORY));
        } catch (Exception e) {
            log.error("Failed to get the property:", e);
        }
        try {
            map.put(HtmlAttribute.FOURTHCATEGORY, properties.getProperty(keyPrefix
                    + CrawlTemplateConstant.FORTH_CATEGORY));
        } catch (Exception e) {
            log.error("Failed to get the property:", e);
        }
        try {
            map.put(HtmlAttribute.DESCRIPTION, properties.getProperty(keyPrefix + CrawlTemplateConstant.DESCRIPTION));
        } catch (Exception e) {
            log.error("Failed to get the property:", e);
        }

        return map;
    }
}
