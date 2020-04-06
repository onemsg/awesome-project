package thevideo.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

/**
 * DataToRedis
 * 把数据持久化到redis数据库的工具类，redis中的value都为json字符串格式
 */
public class DataToRedis {

    private static Jedis jedis = null;  //保存redis实例
    private static final Logger logger = LoggerFactory.getLogger(DataToRedis.class);

    private DataToRedis() {

    }

    public static void itemsInfoToReadi() throws IOException {
        
        init();
        File file = new File("D:\\home\\ml-20m\\movies.csv");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        ObjectMapper mapper = new ObjectMapper();

        reader.lines().skip(1).map(s -> s.split(",")).forEach(s -> {
            // s[0], s[1], s[2]: movieID, title, genres
            ObjectNode itemInfo = mapper.createObjectNode().put("itemID", s[0])
                                    .put("title", s[1]).put("genres", s[2]);
            jedis.set("II:" + s[0], itemInfo.toPrettyString());
            }
        );
        reader.close();
        close();
    }

    //用户和商品信息序列化到redis中
    //用户id为key,看过的items id集合为value（字符串格式）
    public static void itemsOfUserToRedis(DataModel dataModel) throws Exception {
        init();
        LongPrimitiveIterator userIDs = dataModel.getUserIDs();
        ObjectMapper mapper = new ObjectMapper();
        while (userIDs.hasNext()) {
            long userID = userIDs.nextLong();
            long[] itemsId = dataModel.getItemIDsFromUser(userID).toArray();
            jedis.set( "UI:" + userID, mapper.writeValueAsString(itemsId) );
        }
        close();
    }

    //基于用户的商品推荐模型序列化道reids中
    //key=用户id, value=数个商品id和评分
    public static void itemsRecommenderByUserToRedis(GenericUserBasedRecommender recommender, int howMany) throws Exception {
        init();
        LongPrimitiveIterator userIDs = recommender.getDataModel().getUserIDs();
        ObjectMapper mapper = new ObjectMapper();
        while(userIDs.hasNext()){
            long userID = userIDs.nextLong();
            List<RecommendedItem> list = recommender.recommend(userID, howMany);
            ArrayNode root = mapper.createArrayNode();
            for (RecommendedItem item : list) {
                root.add(mapper.createObjectNode().put("itemID", item.getItemID()).put("rating", item.getValue()));
            }
            // key = "IRBU:" + userID 
            jedis.set("IRBU:" + userID, root.toPrettyString());
        }
        close();
    }

    public static void itemsRecommenderByItemToRedis(GenericItemBasedRecommender recommend, int howMany) throws Exception {
        init();
        LongPrimitiveIterator userIDs = recommend.getDataModel().getUserIDs();
        ObjectMapper mapper = new ObjectMapper();
        while(userIDs.hasNext()){
            long userID = userIDs.nextLong();
            List<RecommendedItem> list = recommend.recommend(userID, howMany);
            ArrayNode root = mapper.createArrayNode();
            for (RecommendedItem item : list) {
                root.add(mapper.createObjectNode().put("itemID", item.getItemID()).put("rating", item.getValue()));
            }
            // key = "IRBU:" + userID 
            jedis.set("IRBI:" + userID, root.toPrettyString());
        }
        close();
    }

    private static void init(){
        if(jedis == null){
            jedis = new Jedis("localhost");
        }
        logger.info("数据开始导入Redis ---");
    }

    private static void close(){
        if(jedis != null){
            jedis.close();
        }
        logger.info("数据导入道Redis完成 !");
    }
}