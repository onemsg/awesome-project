package com.onemsg.onepaste.service.impl;

import java.util.Objects;

import com.onemsg.onepaste.model.Paster;
import com.onemsg.onepaste.repository.PasterRepository;
import com.onemsg.onepaste.service.PasterService;

public class PasterServiceImpl implements PasterService {

    private PasterRepository repository;

    public PasterServiceImpl(PasterRepository repository) {
        this.repository = repository;
    }

    @Override
    public Paster find(String pasterId) {
        Objects.requireNonNull(pasterId);
        return repository.findOne(pasterId);
    }

    @Override
    public String insert(Paster paster) {
        Objects.requireNonNull(paster);
        return repository.insert(paster);
    }

    @Override
    public long deleteExpired() {
        return repository.deleteExpired();
    }

    

}
