package dm.weka.core;

import weka.core.Instances;

import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;;
public class ModelEvaluation {
	
	private Instances instances;
	private AbstractClassifier model;
	private Evaluation eval;
	
	public ModelEvaluation() {
		
	}
	
	public ModelEvaluation(DataExploration explor,ModelSeletion select) throws Exception {
		this(explor.getInstances(),select.getModel());
	}
	

	public ModelEvaluation(Instances instances,AbstractClassifier model) throws Exception {
		this.instances = instances;
		this.model = model;
		this.eval = new Evaluation(instances);
	}
	
	public void setInstances(Instances instances) throws Exception {
		this.instances = instances;
		this.eval = new Evaluation(instances);
	}
	
	public void setModel(AbstractClassifier model) {
		this.model = model;
	}
	
	public Evaluation createEvaluation() throws Exception {
		Evaluation eval = null;
		if(this.instances != null) {
			 eval = new Evaluation(this.instances);		
		}
		return eval;
	}
	
	public Evaluation createEvaluation(Instances instances) throws Exception {
		Evaluation eval = null;
		eval = new Evaluation(instances);
		return eval;
	}
	
	//交叉验证
	public String[] crossValidateModel(int Folds,long seed) throws Exception {
		Evaluation eval = createEvaluation();
		eval.crossValidateModel(this.model, this.instances,Folds , new Random(seed));	
		String[] summary = new String[3];
		summary[0] = eval.toSummaryString("性能统计数据:",false);//模型整体评估方法
		try {
			summary[1] = eval.toMatrixString("混淆矩阵:");	//混肴矩阵
			summary[2] = eval.toClassDetailsString("其他度量:");//TP/FP率、查全率等
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return summary;
		}
		return summary;
	}

	//用训练集验证
	public String[] evalutionModelBySelf() throws Exception {
		model.buildClassifier(instances);
		eval.evaluateModel(model, instances);
		String[] summary = new String[3];
		summary[0] = eval.toSummaryString("性能统计数据:",false);//模型整体评估方法
		try {
			summary[1] = eval.toMatrixString("混淆矩阵:");	//混肴矩阵
			summary[2] = eval.toClassDetailsString("其他度量:");//TP/FP率、查全率等
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return summary;
		}
		return summary;
	}
	
	//分割数据集验证
	public String[] splitEvalution(double ratio,long seed) throws Exception {		
		//分割数据集按照ratio比例分成训练集和测试集
		//dataset[0]训练集，dataset[1]测试集
		Instances[] dataset = splitTrainTest(this.instances,ratio,seed); 	
		if(dataset[0]==null || dataset[1]==null) {
			System.out.println("dataset 为 null");
		}
		try{		//模型拟合数据
			this.model.buildClassifier(dataset[0]);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		//创建验证类
		Evaluation eval = new Evaluation(dataset[0]);
		//验证模型
		eval.evaluateModel(this.model,dataset[1]);
		//summary用来存储验证结果
		String[] summary = new String[3];
		summary[0] = eval.toSummaryString("性能统计数据:",false);//模型整体评估方法
		try {
			summary[1] = eval.toMatrixString("混淆矩阵:");	//混肴矩阵
			summary[2] = eval.toClassDetailsString("其他度量:");//TP/FP率、查全率等
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return summary;
		}
		return summary;
	}
	
	//传入新数据集分割验证
	public String[] evalutionModel(Instances instances) throws Exception {
		Instances[] dataset = splitTrainTest(instances);
		Evaluation eval = new Evaluation(dataset[0]);
		this.model.buildClassifier(dataset[0]);
		eval.evaluateModel(this.model,dataset[1]);
		String[] summary = new String[3];
		summary[0] = eval.toSummaryString("性能统计数据:",false);//模型整体评估方法
		try {
			summary[1] = eval.toMatrixString("混淆矩阵:");	//混肴矩阵
			summary[2] = eval.toClassDetailsString("其他度量:");//TP/FP率、查全率等
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return summary;
		}
		return summary;
	}
	
	//分割数据集
	public static Instances[] splitTrainTest(Instances instances) {
		return splitTrainTest(instances, 0.75,2019);
	}
	
	public static Instances[] splitTrainTest(Instances instances,double ration) {
		return splitTrainTest(instances, ration,2019);
	}
	
	public static Instances[] splitTrainTest(Instances instances,double ratio,long seed) {
		// 0 为训练集 1为测试机
		Instances[] data = new Instances[2];
		instances.randomize(new Random(seed));		//打乱顺序
		// 按 ratio 分割训练集和测试集
		int train_size = (int) Math.round(instances.numInstances() * 0.75);
		int test_size = instances.numInstances() - train_size;
		Instances train_data = new Instances(instances, 0, train_size);
		Instances test_data = new Instances(instances,train_size,test_size);
		data[0] = train_data;
		data[1] = test_data;
		return data;
	}
	
}
