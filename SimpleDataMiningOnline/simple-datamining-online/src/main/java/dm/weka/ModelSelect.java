package dm.weka;

import java.util.HashMap;
import java.util.Map;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Utils;

/**
 * 生成模型对象
 * 包含了机器学习模型对象和名字
 */
public class ModelSelect {

	private Classifier model;
	private String modelName;
	
	public static final String[] modelNames = { "LinearRegression", "Logistic", "KNN", "NaiveBayes", "C45",
			"RandomForest", "SVM" };

	public static final Map<String,String> models;

	static {
		// 初始化模型和模型的全类名
		models = new HashMap<>();
		models.put("LinearRegression", "weka.classifiers.functions.LinearRegression");
		models.put("Logistic", "weka.classifiers.functions.Logistic");
		models.put("KNN", "weka.classifiers.lazy.IBk");
		models.put("NaiveBayes", "weka.classifiers.bayes.NaiveBayes");
		models.put("C45", "weka.classifiers.trees.J48");
		models.put("RandomForest", "weka.classifiers.trees.RandomForest");
		models.put("SVM", "weka.classifiers.functions.SMO");
	}
	
	private ModelSelect(){

	}

	/**
	 * 返回实例对象
	 * @param modelName	模型名称
	 * @param options	模型参数
	 * @return
	 * @throws Exception
	 */
	public static ModelSelect start(String modelName, String options) throws Exception {
		ModelSelect select = new ModelSelect();
		//利用反射创建类
		try{
			select.model = AbstractClassifier.forName(models.get(modelName), Utils.splitOptions(options));
		}catch(Exception e){
			System.out.println("模型参数错误:  " + e.getMessage());
			select.model = AbstractClassifier.forName(models.get(modelName), null);
		}
		select.modelName = modelName;
		return select;
	}

	/**
	 * 给定分类器返回模型
	 * @param classifier
	 * @return
	 */
	public static ModelSelect start(Classifier classifier){
		ModelSelect select = new ModelSelect();
		select.modelName = classifier.getClass().getSimpleName();
		select.model = classifier;
		return select;		
	}

	public Classifier getModel() {
		return model;
	}

	public String getModelName() {
		return modelName;
	}

}
