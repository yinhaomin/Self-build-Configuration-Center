package com.baidu.hui.biz.manager;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.baidu.hui.biz.constance.CrawlTemplateConstant;
import com.baidu.hui.biz.utils.SpringContextUtil;
import com.baidu.hui.dao.dao.CrawlerTemplateDAO;
import com.baidu.hui.redis.factory.RedisFactory;
import com.baidu.hui.redis.serializer.MessageSerializer;

@Log4j
@Component
public class RefreshTemplateTimeManager {

    @Autowired
    private RedisFactory redisFactory;

    @Autowired
    @Qualifier(value = "jsonMessageSerializer")
    private MessageSerializer messageSerializer;

    @Autowired
    private CrawlerTemplateDAO crawlerTemplateDAO;
    
    @Autowired
    private SpringContextUtil springContextUtil;

    public void setRefreshTimeStamp() {
        JdbcTemplate template = (JdbcTemplate) springContextUtil.getBean("icHuiJdbcTemplate");
        long keyTimeStamp = crawlerTemplateDAO.getLatestTimestamp(template);
        Jedis jedis = null;
        try {
            jedis = redisFactory.getClient();
            jedis.set(CrawlTemplateConstant.HUI_CRAWL_TEMPLATE_UPDATETIME, keyTimeStamp + "");
        } catch (Exception e) {
            log.error(e);
        }
        try {
            redisFactory.returnResource(jedis);
        } catch (Exception e) {
            log.error(e);
        }
    }

}
