package com.fuze.bcp.bd.domain;


import com.fuze.bcp.domain.TreeDataEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 地区信息
 * Created by sean on 16/10/10.
 */
@Document(collection="bd_province")
@Data
public class Province extends TreeDataEntity {
}
