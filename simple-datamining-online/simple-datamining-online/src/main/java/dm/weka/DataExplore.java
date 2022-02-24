package dm.weka;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

import weka.core.Attribute;
import weka.core.Instances;

import weka.experiment.Stats;

/**
 * 数据探索类 封装了weka
 */
public class DataExplore{
    
    //  weka的数据集类
    private Instances instances = null;
    // 统计信息关键字
    public static final String[] statisKeys = {"Name","Missing","Count","Type","Mean","Std","Mix","Min"};
    //  数据集名称、样本数量、特征数量
    public static final String[] dataInfo = {"dataSetName","numInstances","numAttributes"};
    
	private DataExplore(){

	}

	/**
	 * 创建一个实例对象
	*/
	public static DataExplore start(){
		return new DataExplore();
	}

	/**
	 * 创建一个实例对象
	*/
	public static DataExplore start(String datapath) throws Exception {
		DataExplore explore = new DataExplore();
		explore.instances = DataUtils.getInstances(datapath);
		return explore;
	}

	/**
	 * 创建一个实例对象
	*/
	public static DataExplore start(String datapath, int classIndex) throws Exception {
		DataExplore explore = new DataExplore();
		explore.instances = DataUtils.getInstances(datapath,classIndex);
		return explore;
	}

	
	public Instances getInstances() {
		return instances;
	}
    
    //设置索引
	public void setClassIndex(int classIndex) {
		this.instances.setClassIndex(classIndex);
	}
	
	//得到所有特征
	public String[] getAttributeNames() {
		int numAttributes = instances.numAttributes();		
		String[] attNames = new String[numAttributes];
		for(int i=0; i<numAttributes; i++){
			attNames[i] = instances.attribute(i).name();
		}
		return attNames;
	}
	
	//返回第index样本，样本数据用String[]保存
	private String[] getValue(int index) {	
		String[] instanceValue = null;
		instanceValue = instances.instance(index).toString().split(",");
		return instanceValue;
	}
	
	/**
	 * 返回数据集里的所有的样本
	 * @return List<String[]> values
	 */
	public List<String[]> getValues(){	
		List<String[]> instancesValue =  new ArrayList<String[]>();
		int length = instances.size();
		for(int i=0; i<length; i++) {	//遍历所有样本
			instancesValue.add(getValue(i));	//添加样本到链表
		}
		return instancesValue;
	}
	
	/**
	 * 返回数据大小，样本数和特征数
	 * @return
	 */
	public Integer[] getShape() {
		Integer[] shape = new Integer[2];
		shape[0] = Integer.valueOf(instances.numAttributes());
		shape[1]  = Integer.valueOf(instances.numInstances());
		return shape;
	}
	
	//获得第index个属性的统计信息
	private Map<String,String> getAttributeInfo(int index){	
		
		Attribute att = instances.attribute(index);	//获得第index各属性
		String type = Attribute.typeToString(att); //获得属性类型
		Map<String,String> attMap = new HashMap<String, String>();	//保存属性的统计信息
		attMap.put("Name",att.name());	//放进属性类型
		attMap.put("Type", type);		//属性类型
		attMap.put("Missing", String.valueOf(instances.attributeStats(index).missingCount));	//缺失值
		//判断属性类型来采用不同的数据统计方法
	    switch (type) {
	    	case "numeric":	//数值属性
	    		DecimalFormat df = new DecimalFormat("0.000"); //保留3位有效小数    		
	    		Stats stats = this.instances.attributeStats(index).numericStats;
	    		attMap.put("Count",String.valueOf(stats.count));
	    		attMap.put("Mean",df.format(stats.mean));	//均值
	    		attMap.put("Std", df.format(stats.stdDev));	//标准差
	    		attMap.put("Min",String.valueOf(stats.min));	//最小值
	    		attMap.put("Max",String.valueOf(stats.max));	//最大值
	    		break;
	    	case "nominal":	//标称属性
	    		Enumeration<Object> eunm = att.enumerateValues();
	    		//获得标称属性的取值类别
	    		int[] values = this.instances.attributeStats(index).nominalCounts;
	    		int i = 0;
	    		//遍历保存标称属性各类别的个数
	    		while(eunm.hasMoreElements()) {
	    			String label = (String)eunm.nextElement();
	    			attMap.put(label,String.valueOf(values[i++]));
	    		}
	    		break;
	    }	
	    return attMap;
	}
	
	public Map<String,Map<String,String>> getDescription(){
		//把所有属性的统计信息保存到一起
		int numAtt = instances.numAttributes();
		Map<String, Map<String,String>> desc = new HashMap<String, Map<String,String>>();
		for(int i=0; i<numAtt; i++) {
			String attName = instances.attribute(i).name();
			Map<String, String> attMap = getAttributeInfo(i);
			desc.put(attName, attMap);			 
		}
		return desc;
	}
	
	//刷新数据集顺序
	public void shuffling(long seed) {
		instances.randomize(new Random(seed));
	}
}
