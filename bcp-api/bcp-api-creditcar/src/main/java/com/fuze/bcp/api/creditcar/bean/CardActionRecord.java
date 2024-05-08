package com.fuze.bcp.api.creditcar.bean;
import com.fuze.bcp.api.bd.bean.HistoryRecord;
import lombok.Data;

/**
 * 操作的历史记录
 * Created by Lily on 2017/8/22.
 */
@Data
public class CardActionRecord extends HistoryRecord {
    /**
     * 操作的动作
     */
    private String action = null;
    /**
     * 完成动作的操作人
     */
    private String actionName = null;

    @Override
    public String toString() {
        return "{" +
                "action='" + action + '\'' +
                ", actionName='" + actionName + '\'' +
                ", ts='" + super.getTs() + '\'' +
                '}';
    }
}
