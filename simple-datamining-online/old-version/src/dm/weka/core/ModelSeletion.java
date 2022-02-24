package dm.weka.core;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.*;
import weka.classifiers.functions.*;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.*;
import weka.core.Utils;

public class ModelSeletion {
	
	private AbstractClassifier model;	
	private String modelName;
	//共7大算法
//	private LinearRegression lr;
//	private Logistic lg;
//	private IBk knn;
//	private NaiveBayes nb;
//	private J48 tree;
//	private RandomForest rf;
//	private SMO svm;
	public static final String[] ModelNames = {"LinearRegression","Logistic","KNN","NaiveBayes","C45","RandomForest","SVM"};
	
	public ModelSeletion(String modelName) {
		selectionModel(modelName);
	}
	
	public void selectionModel(String modelName) {
		
		switch (modelName) {
		case "LinearRegression":
			this.modelName = modelName;
			this.model = new LinearRegression();
			break;
		case "Logistic":
			this.modelName = modelName;
			this.model = new Logistic();
			break;
		case "KNN":
			this.modelName = modelName;
			this.model = new IBk();
			break;
		case "C45":
			this.modelName = modelName;
			this.model = new J48();
			break;
		case "RandomForset":
			this.modelName = modelName;
			this.model = new RandomForest();
			break;
		case "SVM":
			this.modelName = modelName;
			this.model = new SMO();
			break;		
		case "NaiveBayes":
			this.modelName = modelName;
			this.model = new NaiveBayes();
			break;		
		default:
			System.out.println("模型名字错误，自动选择为KNN算法");
			this.modelName = "KNN";
			this.model = new IBk();
			break;
		}
	}
	
	public AbstractClassifier getModel() {
		return this.model;
	}
	
	public void setModel(AbstractClassifier model) {
		this.model = model;
	}
	
	public String getModelName() {
		return this.modelName;
	}
	
	public void setOptions(String options) throws Exception{
		String[] options_2 = Utils.splitOptions(options);
		setOptions(options_2);
	}
	
	public void setOptions(String[] options) throws Exception{
			this.model.setOptions(options);
	}
}
