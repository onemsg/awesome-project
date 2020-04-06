package thevideo.web;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import thevideo.common.UserRecommender;

/**
 * DataService
 * 提供用户和推荐的商品信息（json格式）
 */
public class DataService {

    private RedisRepository repository; //访问redis数据库
    private ObjectMapper mapper;        //对json字符串和java对象间的支持
    private GenericUserBasedRecommender ubr;    //基于用户推荐系统的模型
    private GenericItemBasedRecommender ibr;    //基于商品推荐系统的模型

    public DataService() throws Exception {
        init();
    }

    // 初始化对象变量，包括推荐系统模型，json支持类和redis访问类
    private void init() throws Exception {
        ubr = UserRecommender.getUserBasedRecommende();
        ibr = UserRecommender.getItemBasedRecommender();
        mapper = new ObjectMapper();
        repository = new RedisRepository();
    }

    //获得用户看多的电影信息
    public String getItemsOfUser(String userID) {
        ArrayNode items = mapper.createArrayNode();
        try {
            JsonNode itemsIDArray = mapper.readTree(repository.getItemsOfUser(userID));
            for (JsonNode itemID : itemsIDArray) {
                String value = repository.getItemsInfo(itemID.asText());
                items.add(mapper.readTree(value));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return items.toPrettyString();
    }

    /**
     * 基于用户的推荐系统，返回推荐给用户的商品信息
     * @param userID 用户ID
     * @param howMany 多少条商品要返回
     * @return  json格式的商品信息
     */
    public String getItemsRecommenderBasedUser(int userID, int howMany) {
        List<RecommendedItem> list;
        try {
            list = ubr.recommend(userID, howMany);
        } catch (TasteException e) {
            return null;
        }
        return toJson(list);
    }

    /**
     * 基于商品的推荐系统，返回推荐给用户的商品信息
     * @param userID 用户ID
     * @param howMany 多少条商品要返回
     * @return  json格式的商品信息
     */
    public String getItemsRecommenderBasedItem(int userID, int howMany) {
        List<RecommendedItem> list;
        try {
            list = ibr.recommend(userID, howMany);
        } catch (TasteException e) {
            return null;
        }
        return toJson(list);
    }

    /**
     * 基于商品的推荐系统，返回给基于用户userID看过的商品itemID推荐的商品信息
     * @param userID 用户ID
     * @param itemID 用户看多的商品ID
     * @param howMany 多少条商品要返回
     * @return  json格式的商品信息
     */
    public String getItemsRecommenderBasedUserAndItem(int userID, int itemID, int howMany) {
        List<RecommendedItem> list;
        try {
            list = ibr.recommendedBecause(userID, itemID, howMany);
        } catch (TasteException e) {
            return null;
        }
        return toJson(list);
    }

    //内部工具方法，用来把List<RecommendedItem>转化为合适的Json字符串
    private String toJson(List<RecommendedItem> list) {
        ArrayNode root = mapper.createArrayNode();
        for (RecommendedItem item : list) {
            long itemID = item.getItemID();
            float rating = item.getValue();
            JsonNode itemNode = null;
            try {
                itemNode = mapper.readTree(repository.getItemsInfo(itemID));
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            root.add(mapper.createObjectNode().put("itemID", itemID).put("rating", rating).set("info",itemNode));
        }
        return root.toPrettyString();
    }
}