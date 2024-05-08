package com.fuze.bcp.domain;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.mapping.Document;
import org.w3c.dom.events.DocumentEvent;

import java.io.Serializable;


/**
 * 统一定义id的entity基类.
 * <p>
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * 子类可重载getId()函数重定义id的列名映射和生成策略.
 *
 * @author Sean
 */
//JPA 基类的标识
public abstract class BaseEntity implements Serializable {

    /**
     * 分页单位
     */
    public static final int PAGESIZE = 10;


    /**
     * 表示创建时间
     */
    private String ts;

    /**
     * 表示最后更新时间
     */
    private Long ms;

    /**
     * 记录距离现在的时间
     */

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public BaseEntity() {
        super();
        setMs(0L);
    }

    public Long getMs() {
        return ms;
    }

    public void setMs(Long ms) {
        this.ms = System.currentTimeMillis();
    }

    /**
     * 降序排列
     *
     * @return
     */
    public static Sort getTsSort() {
        return new Sort(Direction.DESC, "ts");
    }

    /**
     * 升序排列
     *
     * @return
     */
    public static Sort getTsSortASC() {
        return new Sort(Direction.ASC, "ts");
    }

    public static Sort getSortASC(String propName) {
        return new Sort(Direction.ASC, propName);
    }

    public static Sort getSortDESC(String propName) {
        return new Sort(Direction.DESC, propName);
    }

    public  static <T extends BaseEntity> String getMongoCollection(T t) throws Exception {
        Document document =  t.getClass().getAnnotation(Document.class);
        if(document != null){
            return document.collection();
        }else {
            throw new Exception("no collectionName defined");
        }
    }
}
