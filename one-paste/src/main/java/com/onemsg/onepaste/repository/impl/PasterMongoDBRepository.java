package com.onemsg.onepaste.repository.impl;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lte;

import com.mongodb.client.MongoCollection;
import com.onemsg.onepaste.model.Paster;
import com.onemsg.onepaste.repository.PasterRepository;

import org.bson.types.ObjectId;

public class PasterMongoDBRepository implements PasterRepository {

    private MongoCollection<Paster> collection;

    public PasterMongoDBRepository(MongoCollection<Paster> collection){
        this.collection = collection;
    }

    @Override
    public Paster findOne(String id) {
        ObjectId _id = new ObjectId(id);
        return collection.find(eq("_id", _id)).first();
    }

    @Override
    public void delete(String id) {
        ObjectId _id = new ObjectId(id);
        collection.deleteOne(eq("_id", _id));
    }

    @Override
    public boolean exists(String id) {
        ObjectId _id = new ObjectId(id);
        return collection.countDocuments(eq("_id", _id)) != 0L;
    }

    @Override
    public String insert(Paster paster) {
        return collection.insertOne(paster).getInsertedId().asObjectId().getValue().toHexString();
    }

    @Override
    public long deleteExpired() {
        long currentTimestamp = System.currentTimeMillis();
        return collection.deleteMany(lte("expiration", currentTimestamp)).getDeletedCount();
    }
    
}
