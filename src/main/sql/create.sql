CREATE TABLE IF NOT EXISTS QUOTES (
  ID int(11) NOT NULL AUTO_INCREMENT UNIQUE,
  TIME TIMESTAMP NOT NULL  DEFAULT CURRENT_TIMESTAMP,
  BID decimal(18,6) NOT NULL,
  ASK decimal(18,6) NOT NULL,
  EXCHANGE varchar(20) NOT NULL,
  NAME varchar(20) NOT NULL,
  PRIMARY KEY (ID),
  KEY QUOTES_TIME_IDX (TIME) USING BTREE,
  KEY QUOTES_SYNTHETIC_IDX (NAME) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8