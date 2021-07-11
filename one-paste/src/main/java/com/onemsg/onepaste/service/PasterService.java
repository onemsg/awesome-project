package com.onemsg.onepaste.service;

import com.onemsg.onepaste.model.Paster;

public interface PasterService {
    

    Paster find(String pasterId);

    String insert(Paster paster);

    /**
     * 删除过期内容
     * @return
     */
    long deleteExpired();
}
