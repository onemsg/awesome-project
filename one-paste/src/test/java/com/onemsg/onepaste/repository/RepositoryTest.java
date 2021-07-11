package com.onemsg.onepaste.repository;

import com.onemsg.onepaste.config.MongoDBConfig;
import com.onemsg.onepaste.model.Paster;
import com.onemsg.onepaste.repository.impl.PasterMongoDBRepository;

public class RepositoryTest {
    
    public static void main(String[] args) {
        
        PasterRepository rep =  new PasterMongoDBRepository(MongoDBConfig.getPasterCollection());

        Paster paster = new Paster();
        paster.setPoster("onemsg");
        paster.setPostdate(System.currentTimeMillis());
        paster.setContent("hello world");
        paster.setExpiration(System.currentTimeMillis() + 3600000L);

        System.out.println(paster);

        String id = rep.insert(paster);
        System.out.println(id);

    }
}
