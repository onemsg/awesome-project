package thevideo.common;

// import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
// import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DataInit
 * 数据初始化类，运行DataToRedis类中的（数据导入redis的）静态方法
 */
public class DataInit {

    private static final Logger logger = LoggerFactory.getLogger(DataInit.class);

    public static void start() throws Exception {
        
        logger.info("REDIS 数据初始化开始 ---");    

        DataToRedis.itemsOfUserToRedis(UserRecommender.getDataModel());
        DataToRedis.itemsInfoToReadi();
        // GenericUserBasedRecommender ubr = UserRecommender.getUserBasedRecommende(dataFile);
        // DataToRedis.itemsRecommenderByUserToRedis(ubr, 10);

        // GenericItemBasedRecommender ibr = UserRecommender.getItemBasedRecommender(dataFile);
        // DataToRedis.itemsRecommenderByItemToRedis(ibr, 10);

        logger.info("REDIS 数据初始化完成 ---");
    }
}