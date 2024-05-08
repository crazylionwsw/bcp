package com.fuze.bcp.creditcar.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**  客户资料交接
 * Created by ${Liu} on 2017/9/19.
 */
@Document(collection = "so_recept_file")
@Data
public class ReceptFile extends BaseBillEntity {

    /**
     * 交接资料内容
     */
    private List<String> fileNames;

    public String getBillTypeCode() {
        return "A030";
    }
}
