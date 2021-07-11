package com.onemsg.onepaste.model;

import java.util.Objects;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public final class Paster {

    @BsonId
    private ObjectId id;
    private String poster;
    private String syntax;
    private long expiration;
    private String content;
    private long postdate;

    public Paster() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPostdate() {
        return postdate;
    }

    public void setPostdate(long postdate) {
        this.postdate = postdate;
    }

    @Override
    public String toString() {
        return String.format("Paster [id=%s, poster=%s, syntax=%s, expiration=%s, postdate=%s, content=%s]", 
            id, poster, syntax, expiration, postdate, content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, expiration, id, postdate, poster, syntax);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Paster other = (Paster) obj;
        return Objects.equals(content, other.content) && expiration == other.expiration && Objects.equals(id, other.id)
                && postdate == other.postdate && Objects.equals(poster, other.poster)
                && Objects.equals(syntax, other.syntax);
    }
    
}
