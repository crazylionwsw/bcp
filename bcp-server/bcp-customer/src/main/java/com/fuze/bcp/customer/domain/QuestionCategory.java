package com.fuze.bcp.customer.domain;

import com.fuze.bcp.domain.TreeDataEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *      调查问卷----问题分类
 * Created by zqw on 2017/8/26.
 */
@Document(collection = "bd_question_category")
@Data
public class QuestionCategory extends TreeDataEntity {

    /**
     *  分类编码
     */

    /**
     *  分类名称
     */

    /**
     * 显示顺序
     */
    private int displayOrder;
}
