package dm.weka.core;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.experiment.Stats;

public class DataExploration {
	
	private Instances instances = null;
	public static final String[] statisKeys = {"name","Missing","Count","Type","Mean","Std","Mix","Min"};
	public static final String[] dataInfo = {"dataSetName","numInstances","numAttributes"};
	public DataExploration(){
		
	}
	
	public DataExploration(String datapath) throws Exception {
		setInstances(datapath);
	}
	
	public void setInstances(String datapath) throws Exception{
		DataSource source = new DataSource(datapath);
		this.instances = source.getDataSet(); //加载数据
		if(this.instances.classIndex() == -1) {			//设置标签
			this.instances.setClassIndex(this.instances.numAttributes() -1);
		}
	}
	
	public void setInstances(Instances instances) {
		this.instances = instances;
	}
	
	public Instances getInstances() {
		return this.instances;
	}
	
	public void setClassIndex(int classIndex) {
		this.instances.setClassIndex(classIndex);
	}
	
	public String[] getAttributeNames() {
		
		int numAttributes = this.instances.numAttributes();		
		String[] attNames = new String[numAttributes];
		for(int i=0;i<numAttributes;i++){
			attNames[i] = this.instances.attribute(i).name();
		}
		return  attNames;
	}
	
	public String[] getValue(int index) {	//返回第index样本，样本数据用String[]保存
		String[] instanceValue = null;
		instanceValue = this.instances.instance(index).toString().split(",");
		return instanceValue;
	}
	
	public List<String[]> getValue(){	//用链表返回数据集里的所有的样本
		List<String[]> instancesValue =  new ArrayList<String[]>();
		int length =  this.instances.size();
		for(int i=0; i<length; i++) {	//遍历所有样本
			String[] value = getValue(i);
			instancesValue.add(value);	//添加样本到链表
		}
		return instancesValue;
	}
	
	public Map<String,String> getDataInfo() {
		Map<String,String> info = new HashMap<String,String>();
		info.put("dataSetName",this.instances.relationName());
		info.put("numAttributes", String.valueOf(this.instances.numAttributes()));
		info.put("numInstances",String.valueOf(this.instances.numInstances()));
		return info;		
	}
	
	//获得第index个属性的统计信息
	public Map<String,String> getAttributeInfo(int index){	
		
		Attribute att = this.instances.attribute(index);	//获得第index各属性
		String type = Attribute.typeToString(att); //获得属性类型
		Map<String,String> attMap = new HashMap<String, String>();	//保存属性的统计信息
		attMap.put("name",att.name());	//放进属性类型
		//判断属性类型来采用不同的数据统计方法
	    switch (type) {
	    	case "numeric":	//数值属性
	    		DecimalFormat df = new DecimalFormat("0.000"); //保留3位有效小数
	    		attMap.put("Type", type);	//放进类型信息
	    		//放进缺失值信息
	    		attMap.put("Missing", String.valueOf(this.instances.attributeStats(index).missingCount));	    		
	    		Stats stats = this.instances.attributeStats(index).numericStats;
	    		attMap.put("Count",String.valueOf(stats.count));
	    		attMap.put("Mean",df.format(stats.mean));	//均值
	    		attMap.put("Std", df.format(stats.stdDev));	//标准差
	    		attMap.put("Min",String.valueOf(stats.min));	//最小值
	    		attMap.put("Max",String.valueOf(stats.max));	//最大值
	    		break;
	    	case "nominal":	//标称属性
	    		attMap.put("Type", type);
	    		attMap.put("Missing", String.valueOf(this.instances.attributeStats(index).missingCount));
	    		Enumeration<Object> eunm = att.enumerateValues();
	    		//获得标称属性的取值类别
	    		int[] values = this.instances.attributeStats(index).nominalCounts;
	    		int i = 0;
	    		//遍历保存标称属性各类别的个数
	    		while(eunm.hasMoreElements()) {
	    			String label = (String)eunm.nextElement();
	    			attMap.put(label,String.valueOf(values[i]));
	    			i++;
	    		}
	    		break;
	    }	
	    return attMap;
	}
	
	public Map<String,Map<String,String>> getDataDescribe(){
		//把所有属性的统计信息保存到一起
		int numAtt = this.instances.numAttributes();
		Map<String, Map<String,String>> desc = new HashMap<String, Map<String,String>>();
		for(int i=0; i<numAtt; i++) {
			String attName = this.instances.attribute(i).name();
			Map<String, String> attMap = getAttributeInfo(i);
			desc.put(attName, attMap);			 
		}
		return desc;
	}
	
	//刷新数据集顺序
	public void shuffling(long seed) {
		this.instances.randomize(new Random(seed));
		
	}
}
