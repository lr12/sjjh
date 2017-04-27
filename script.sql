CREATE DATABASE  IF NOT EXISTS `SJJHPT`;
USE `SJJHPT`;

-- 银行-请求队列
DROP TABLE IF EXISTS `QUEUE_BANK`;
CREATE TABLE `QUEUE_BANK` (
  `UUID` varchar(255) NOT NULL,
  `REQUEST_ID` varchar(255) DEFAULT NULL,
  `RESPONSE_ID` varchar(255) DEFAULT NULL,
  `REQUEST_INTERFACE_ID` varchar(255) DEFAULT NULL,
  `RESPONSE_INTERFACE_ID` varchar(255) DEFAULT NULL,
  `QUERY_ID` varchar(255) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `REQUESTER` varchar(255) DEFAULT NULL,
  `REPLIER` varchar(255) DEFAULT NULL,
  `RECEIVE_REQUEST_TIME` datetime DEFAULT NULL,
  `SEND_REQUEST_TIME` datetime DEFAULT NULL,
  `RECEIVE_RESPONSE_TIME` datetime DEFAULT NULL,
  `SEND_RESPONSE_TIME` datetime DEFAULT NULL,
  `DECODED_PARAM` text DEFAULT NULL,
  `DECODED_RESULT` text DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  PRIMARY KEY (`UUID`),
  UNIQUE KEY `UUID` (`UUID`),
  UNIQUE KEY `QUERY_ID` (`QUERY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 银行-错误请求队列
DROP TABLE IF EXISTS `ERROR_QUEUE_BANK`;
CREATE TABLE `ERROR_QUEUE_BANK` (
  `UUID` varchar(255) NOT NULL,
  `REQUEST_ID` varchar(255) DEFAULT NULL,
  `RESPONSE_ID` varchar(255) DEFAULT NULL,
  `REQUEST_INTERFACE_ID` varchar(255) DEFAULT NULL,
  `RESPONSE_INTERFACE_ID` varchar(255) DEFAULT NULL,
  `QUERY_ID` varchar(255) DEFAULT NULL,
  `STATUS` varchar(255) DEFAULT NULL,
  `REQUESTER` varchar(255) DEFAULT NULL,
  `REPLIER` varchar(255) DEFAULT NULL,
  `RECEIVE_REQUEST_TIME` datetime DEFAULT NULL,
  `SEND_REQUEST_TIME` datetime DEFAULT NULL,
  `RECEIVE_RESPONSE_TIME` datetime DEFAULT NULL,
  `SEND_RESPONSE_TIME` datetime DEFAULT NULL,
  `DECODED_PARAM` text DEFAULT NULL,
  `DECODED_RESULT` text DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `ERROR_COUNT` int(11) DEFAULT 0,
  `ERROR_MESSAGE` VARCHAR(255) DEFAULT NULL,
  `DISMISS` VARCHAR(2) DEFAULT NULL,
  PRIMARY KEY (`UUID`),
  UNIQUE KEY `UUID` (`UUID`),
  UNIQUE KEY `QUERY_ID` (`QUERY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 通用 代码配置表
DROP TABLE IF EXISTS `CODE_CONFIG`;
CREATE TABLE `CODE_CONFIG` (
  `ID` int(11) NOT NULL,
  `CODE_TYPE` varchar(255) DEFAULT NULL,
  `CODE_KEY` varchar(255) DEFAULT NULL,
  `CODE_VALUE` varchar(255) DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `REMARK2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
