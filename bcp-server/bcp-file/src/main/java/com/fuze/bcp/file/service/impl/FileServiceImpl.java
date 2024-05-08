package com.fuze.bcp.file.service.impl;

import com.fuze.bcp.file.domain.BcpFile;
import com.fuze.bcp.file.repository.BcpFileRepository;
import com.fuze.bcp.file.service.IFileService;
import com.fuze.bcp.service.impl.BaseDataServiceImpl;
import com.fuze.bcp.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by CJ on 2017/6/20.
 */
@Service
public class FileServiceImpl extends BaseServiceImpl<BcpFile, BcpFileRepository> implements IFileService {

    @Autowired
    BcpFileRepository bcpFileRepository;

    @Override
    public void deleteFile(String id) {
        bcpFileRepository.delete(id);
    }
}
