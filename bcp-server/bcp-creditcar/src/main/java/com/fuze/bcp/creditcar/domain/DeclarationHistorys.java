package com.fuze.bcp.creditcar.domain;

import com.fuze.bcp.api.creditcar.bean.declaration.DeclarationRecord;
import com.fuze.bcp.domain.MongoBaseEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zqw on 2017/12/8.
 */
@Document(collection = "so_declaration_historys")
@Data
public class DeclarationHistorys extends MongoBaseEntity {

    private String customerId;

    private String customerTransactionId;

    // 报批反馈历史记录
    private List<DeclarationRecord> historyRecords = new ArrayList<DeclarationRecord>();
}
