package com.fuze.bcp.file.business;

import com.fuze.bcp.api.file.bean.FileBean;
import com.fuze.bcp.api.file.service.IFileBizService;
import com.fuze.bcp.api.file.utils.FileBeanUtils;
import com.fuze.bcp.bean.DataPageBean;
import com.fuze.bcp.bean.DataStatus;
import com.fuze.bcp.bean.ResultBean;
import com.fuze.bcp.file.domain.BcpFile;
import com.fuze.bcp.file.service.IFileService;
import com.fuze.bcp.service.MappingService;
import com.fuze.bcp.utils.QingStorUtils;
import com.qingstor.sdk.exception.QSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CJ on 2017/6/15.
 */
@Service
public class BizFileService implements IFileBizService {

    Logger logger = LoggerFactory.getLogger(BizFileService.class);

    @Autowired
    IFileService iFileService;

    @Autowired
    MappingService mappingService;

    @Override
    public ResultBean<FileBean> actSaveFile(FileBean file) {
        BcpFile bcpFile = mappingService.map(file, BcpFile.class);
        bcpFile = iFileService.save(bcpFile);
        if (bcpFile != null) {
            return ResultBean.getSucceed().setD(mappingService.map(bcpFile, FileBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<FileBean> actPackageSaveFile(FileBean fileBean, File file) {
        try {
            FileBeanUtils.uploadFile(fileBean, new FileInputStream(file));
            return this.actSaveFile(fileBean);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultBean.getFailed();
        }
    }

    @Override
    public ResultBean<List<FileBean>> actSaveFiles(List<FileBean> files) {
        List<FileBean> results = new ArrayList<>();
        if (files.size() > 0) {
            for (FileBean fileBean : files) {
                BcpFile bcpFile = mappingService.map(fileBean, BcpFile.class);
                bcpFile = iFileService.save(bcpFile);
                if (bcpFile != null) {
                    results.add(mappingService.map(bcpFile, FileBean.class));
                }
            }
            return ResultBean.getSucceed().setD(results);
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<List<FileBean>> actPackageSaveFiles(List<Map<String, Object>> files) {
        List<FileBean> fileBeans = new ArrayList<>();
        for (Map<String, Object> map : files) {
            FileBean fileBean = (FileBean) map.get("fileBean");
            File file = (File) map.get("file");
            if (fileBean == null || file == null) {
                try {
                    throw new Exception("上传文件对象不存在");
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return ResultBean.getFailed();
                }
            }
            ResultBean result = actPackageSaveFile(fileBean, file);
            if (!result.failed()) {
                fileBeans.add((FileBean) result.getD());
            }
        }
        return ResultBean.getSucceed().setD(fileBeans);
    }

    @Override
    public ResultBean<FileBean> actGetFile(String fileId) {
        BcpFile bcpFile = iFileService.getOne(fileId);
        if (bcpFile != null) {
            return ResultBean.getSucceed().setD(mappingService.map(bcpFile, FileBean.class));
        }
        return ResultBean.getFailed();
    }

    @Override
    public ResultBean<FileBean> actDeleteFileById(String id) {
        BcpFile bcpFile = iFileService.getOne(id);
        if (bcpFile != null && bcpFile.getDataStatus() == DataStatus.DISCARD) {
            QingStorUtils qingStorUtils = QingStorUtils.getInstance();
            try {
                qingStorUtils.deleteObject(bcpFile.getBucketName(), bcpFile.getZoneName(), bcpFile.getObjectname());
            } catch (QSException e) {
                e.printStackTrace();
            }
        }
        bcpFile = iFileService.delete(id);
        if (bcpFile != null) {
            return ResultBean.getSucceed().setD(mappingService.map(bcpFile, FileBean.class));
        }

        return ResultBean.getSucceed();
    }

    @Override
    public ResultBean<FileBean> actRealDeleteFileById(String id) {
        BcpFile bcpFile = iFileService.getOne(id);
        if (bcpFile != null) {
            QingStorUtils qingStorUtils = QingStorUtils.getInstance();
            try {
                qingStorUtils.deleteObject(bcpFile.getBucketName(), bcpFile.getZoneName(), bcpFile.getObjectname());
            } catch (QSException e) {
                e.printStackTrace();
            }
        }
        bcpFile = iFileService.delete(id);
        if (bcpFile != null) {
            return ResultBean.getSucceed().setD(mappingService.map(bcpFile, FileBean.class));
        }
        return ResultBean.getSucceed();
    }

    @Override
    public void actDeleteFileByIds(List<String> ids) {
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.get(i);
            actDeleteFileById(id);
        }
    }

    @Override
    public ResultBean<DataPageBean<FileBean>> actGetFiles(@NotNull @Min(0L) Integer currentPage) {
        Page<BcpFile> bcpFiles = iFileService.getAll(currentPage);
        return ResultBean.getSucceed().setD(mappingService.map(bcpFiles, FileBean.class));
    }

    @Override
    public ResultBean<FileBean> actDeleteFile(String id) {
        BcpFile bcpFile = iFileService.deleteReal(id);
        if(bcpFile != null){
            return ResultBean.getSucceed().setD(mappingService.map(bcpFile,FileBean.class));
        }
        return ResultBean.getFailed();
    }
}
