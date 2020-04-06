package thevideo.web;

import redis.clients.jedis.Jedis;

/**
 * RedisRepository
 * 访问Redis数据库
 */
public class RedisRepository {

    private final Jedis jedis = new Jedis("localhost");
    public static final String USER_ID = "UI:";
    public static final String ITEM_ID =  "II:";
    // public static final String IRBU = "IRBU:";
    // public static final String IRBI = "IRBI:";

    public RedisRepository() { }

    public String getItemsOfUser(String userID) {
        // key = UI: + userID
        return jedis.get(USER_ID  + userID);
    }

    public String getItemsOfUser(long userID) {
        return getItemsOfUser(String.valueOf(userID));
    }

    public String getItemsInfo(String itemID) {
        return jedis.get(ITEM_ID + itemID);
    }

    public String getItemsInfo(long itemID) {
        return getItemsInfo(String.valueOf(itemID));
    }

}