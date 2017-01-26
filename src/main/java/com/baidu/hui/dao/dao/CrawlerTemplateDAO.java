package com.baidu.hui.dao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.baidu.hui.dao.dao.impl.HuiSimpleDao;
import com.baidu.hui.dao.po.CrawlerTemplate;

@Component(value = "crawlerTemplateDAO")
public class CrawlerTemplateDAO extends HuiSimpleDao<CrawlerTemplate> {

    private static final String COLUMNS = "`id`,  `bean_name`,  `template_id`,  `uc_id`,  "
            + "`domain`,  `merchant`,`secend_level_domain`,  `crawl_type`,  `out_id_patten`,"
            + "  `patten_index`,  `html_charset`,  `title`,  `price`, `images`,  `value`,  `brand`, "
            + " `stock`,  `category`,  `sub_category`,  `third_category`,  `forth_category`,  "
            + "`description`,  `addtime`,  `updatetime`";

    public ResultSet getResultSet(Connection connection) {
        ResultSet resultSet = null;
        try {
            String query = String.format("SELECT %s FROM %s; ", COLUMNS, getTableName());
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (Exception e) {
            log.error("Error in get the crawler_template table values:", e);
        }
        return resultSet;
    }

    public long getLatestTimestamp(JdbcTemplate jdbcTemplate) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ").append(getTableName());
        final String sql = sb.toString();
        log.info("Query the CrawlerTemplate by: " + sql);
        List<CrawlerTemplate> ret = jdbcTemplate.query(sql, new BeanPropertyRowMapper<CrawlerTemplate>(
                CrawlerTemplate.class));
        long timeStamp = 0L;

        if (ret != null) {
            for (CrawlerTemplate template : ret) {
                if (timeStamp < template.getUpdatetime().getTime()) {
                    timeStamp = template.getUpdatetime().getTime();
                }
            }
        }

        return timeStamp;
    }
}
