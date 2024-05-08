package com.fuze.bcp.api.file.service;

import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.ResultBean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 文件上传与下载服务
 */
public interface IFileBizService {


    /**
     * 单个文件保存
     * @param file
     * @return 文件ID
     */
    ResultBean<FileBean> actSaveFile(FileBean file);

    /**
     * 单个文件上传+保存
     */
    ResultBean<FileBean> actPackageSaveFile(FileBean fileBean, File file);

    /**
     * 多个文件保存
     * @param files
     * @return
     */
    ResultBean<List<FileBean>> actSaveFiles(List<FileBean> files);

    /**
     * 多个文件上传+保存
     * @param files
     * @return
     */
    ResultBean<List<FileBean>> actPackageSaveFiles(List<Map<String, Object>> files);


    /**
     * 获取文件bean
     * @param fileId
     * @return
     */
    ResultBean<FileBean> actGetFile(String fileId);

    /**
     * 删除文件bean     数据状态为作废的删除
     * @param id
     * @return
     */
    ResultBean<FileBean> actDeleteFileById(String id);

    /**
     * 删除文件bean     直接删除
     * @param id
     * @return
     */
    ResultBean<FileBean> actRealDeleteFileById(String id);

    /**
     * 批量删除文件bean
     * @param ids
     */
    void actDeleteFileByIds(List<String> ids);

    /**
     * 获取文件列表
     * @param currentPage
     * @return
     */
    ResultBean<DataPageBean<FileBean>> actGetFiles(@NotNull @Min(0) Integer currentPage);

    /**
     *删除数据库记录
     */
    ResultBean<FileBean> actDeleteFile(String id);


}
