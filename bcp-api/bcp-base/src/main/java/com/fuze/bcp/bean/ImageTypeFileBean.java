package com.fuze.bcp.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 集成档案类型和文件ID (PAD)
 */
@Data
public class ImageTypeFileBean extends APIBaseDataBean {

    /**
     * 样本的文件ID
     */
    private List<String> exampleFileIds = new ArrayList<String>();

    /**
     * 档案支持的文件后缀名
     */
    private List<String> suffixes = new ArrayList<String>();

    /**
     * 文件ID列表
     */
    private List<String> fileIds = new ArrayList<String>();

    //  可以删除的档案资料
    private List<String> canNotDeleteFileIds = new ArrayList<String>();

}
