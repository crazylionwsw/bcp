package com.fuze.bcp.mq.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用于记录日志
 * Created by CJ on 2017/7/19.
 */
@Document(collection = "mq_businesslog")
@Data
public class BusinessLog {
}
