package dm.weka;

import weka.core.converters.ConverterUtils.DataSource;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

import org.springframework.util.ResourceUtils;

import dm.model.DataDescription;
import dm.model.DataLocation;
import weka.core.Instances;

/**
 * DataUtils
 */
public class DataUtils {

    private DataUtils(){

    }

    public static DataDescription toExplore(DataLocation dataLocation) throws Exception {
        DataExplore explore = DataExplore.start(dataLocation.getPath());
        DataDescription desc = new DataDescription();
        desc.setName(dataLocation.getName());
        desc.setShape(explore.getShape());
        desc.setFeatures(explore.getAttributeNames());
        desc.setValues(explore.getValues());
        desc.setDescription(explore.getDescription());
        return desc;
    }


    public static Instances getInstances(String datapath) throws Exception {
        File file = ResourceUtils.getFile("classpath:" + datapath);
		Instances instances = DataSource.read(new FileInputStream(file)); //加载数据
		if(instances.classIndex() == -1) {			//设置标签
			instances.setClassIndex(instances.numAttributes() -1);
        }
        return instances;
    }

    public static Instances getInstances(String datapath, int classIndex) throws Exception {
        File file = ResourceUtils.getFile("classpath:" + datapath);
		Instances instances = DataSource.read(new FileInputStream(file)); //加载数据
        if(instances.classIndex() == -1) {			//设置标签
			instances.setClassIndex(classIndex);
        }
        return instances;
    }

    /**
     * 分割数据集为训练集和测试集，默认比例为0.75，随机数种子2019
     * @param instances 数据集
     * @return 训练集和测试机（索引0和1）
     * @see #splitTrainTest(Instances, double, long)
     */
	public static Instances[] splitTrainTest(Instances instances) {
		return splitTrainTest(instances, 0.75,2019);
	}
    
    /**
     * 分割数据集为训练集和测试集，默认随机数种子2019
     * @param instances 数据集
     * @param ration 分割比例
     * @return 训练集和测试机（索引0和1）
     * @see #splitTrainTest(Instances, double, long)
     */
	public static Instances[] splitTrainTest(Instances instances,double ration) {
		return splitTrainTest(instances, ration,2019);
    }
    
	/**
     * 分割数据集为训练集和测试集
     * @param instances 数据集
     * @param ratio 分割比例
     * @param seed  随机数种子
     * @return 训练集和测试机（索引0和1）
     */
	public static Instances[] splitTrainTest(Instances instances,double ratio,long seed) {

		instances.randomize(new Random(seed));		//打乱顺序
		// 按 ratio 分割训练集和测试集
		int train_size = (int) Math.round(instances.numInstances() * 0.75);
		int test_size = instances.numInstances() - train_size;
		Instances train_data = new Instances(instances, 0, train_size);
		Instances test_data = new Instances(instances,train_size,test_size);
		return new Instances[] {train_data, test_data};
	}
}