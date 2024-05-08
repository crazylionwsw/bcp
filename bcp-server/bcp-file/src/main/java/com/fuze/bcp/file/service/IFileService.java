package com.fuze.bcp.file.service;

import com.fuze.bcp.file.domain.BcpFile;
import com.fuze.bcp.service.IBaseService;

/**
 * Created by CJ on 2017/6/20.
 */
public interface IFileService extends IBaseService<BcpFile> {

    void deleteFile(String id);
}
