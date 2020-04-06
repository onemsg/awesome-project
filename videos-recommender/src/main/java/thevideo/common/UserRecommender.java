package thevideo.common;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * UserRecommender
 * 包含提供数据模型和推荐系统模型的静态方法
 */
public class UserRecommender {

    public static final String DATA_PATH = "D:\\home\\ml-20m\\ratings.csv";
    private static DataModel dataModel;     //保存到类中，减少每次生成推荐模型的花费

    private UserRecommender() {
    }

    //初始化，返回数据模型
    private static void init() throws IOException {
        if(dataModel == null ){
            dataModel = new FileDataModel(new File(DATA_PATH)); //高计算量
        }
    }

    public static DataModel getDataModel() throws IOException {
        init();
        return dataModel;
    }

    //基于用户的推荐，返回推荐模型
    public static GenericUserBasedRecommender getUserBasedRecommende() throws Exception{
        init();
        UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);
        UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(50, userSimilarity, dataModel);
        GenericUserBasedRecommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);
        return recommender;
    }

    //基于物品的推荐，返回推荐模型
    public static GenericItemBasedRecommender getItemBasedRecommender() throws Exception{
        init();
        //计算相似度，使用皮尔逊算法
        ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
        //使用基于物品的协同过滤器
        GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
        return recommender;
    }
}