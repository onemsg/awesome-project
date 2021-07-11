package com.onemsg.onepaste.config;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.onemsg.onepaste.model.Paster;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoDBConfig {
    
    public static final String CONNECTION_STRING = "mongodb://localhost:27017";
    public static final String DB_NAME = "onepaste";
    public static final String COLLECTION_NAME = "paster";

    public static MongoCollection<Paster> getPasterCollection(){

        MongoClient client =  MongoClients.create(CONNECTION_STRING);
        MongoDatabase database = client.getDatabase(DB_NAME);

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().register(Paster.class).build()));
        
        return database.withCodecRegistry(pojoCodecRegistry)
            .getCollection(COLLECTION_NAME, Paster.class);
    }

}
