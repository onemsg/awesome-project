package com.onemsg.onepaste.repository;

import com.onemsg.onepaste.model.Paster;

/**
 * PasterRepository
 */
public interface PasterRepository {

    Paster findOne(String id);

    void delete(String id);

    boolean exists(String id);

    String insert(Paster paster);

    long deleteExpired();
}