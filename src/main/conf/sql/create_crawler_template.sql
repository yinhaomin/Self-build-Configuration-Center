CREATE TABLE `crawler_template` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'PK',
	`bean_name` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '抓取的tempalte的bean的名称',
	`template_id` INT(11) NOT NULL DEFAULT '0' COMMENT '抓取的url页面的template id',
	`uc_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'uc Id(未使用，reserved)',
	`domain` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '抓取的url所属的域名',
	`merchant` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '抓取的url所属的商城',
	`secend_level_domain` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '抓取的url所属的二级域名',
	`crawl_type` TINYINT(4) NOT NULL DEFAULT '1' COMMENT 'url所属的类型，1-正常，4-default',
	`out_id_patten` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '获取outId的正则表达式',
	`patten_index` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '获取outId的正则表达式所要获取的文职',
	`html_charset` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '抓取到的html的编码',
	`title` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '获取title的正则表达式list',
	`price` VARCHAR(500) NOT NULL DEFAULT '' COMMENT '获取price的正则表达式list',
	`images` VARCHAR(500) NOT NULL DEFAULT '' COMMENT '获取images的正则表达式list',
	`value` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '获取value的正则表达式list',
	`brand` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '获取brand的正则表达式list',
	`stock` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '获取stock的正则表达式list',
	`category` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '获取category的正则表达式list',
	`sub_category` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '获取sub_category的正则表达式list',
	`third_category` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '获取third_category的正则表达式list',
	`forth_category` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '获取forth_category的正则表达式list',
	`description` VARCHAR(150) NOT NULL DEFAULT '' COMMENT '获取description的正则表达式list',
	`addtime` DATETIME NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT '添加时间',
	`updatetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更改时间',
	PRIMARY KEY (`id`)
)
COMMENT='所使用的爬虫的template'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
CHARSET=utf8
AUTO_INCREMENT=100000
;

