package dm.weka;

import weka.core.Instances;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;;
public class ModelEvaluate {
	
	private Instances instances;
	private Classifier model;
	
	private ModelEvaluate() {
		
	}
	
	public static ModelEvaluate start(Instances instances, Classifier model) throws Exception {
		ModelEvaluate evaluate = new ModelEvaluate();
		evaluate.instances = instances;
		evaluate.model = model;
		return evaluate;
	}
	
	//得到验证结果
	private Map<String,String> getSummary(Evaluation eval){
		Map<String,String> summary = new HashMap<String,String>();
		summary.put("summary", eval.toSummaryString("性能统计数据:",false));//模型整体评估方法
		try {
			summary.put("matrix", eval.toMatrixString("混淆矩阵:")) ;	//混肴矩阵
			summary.put("classDetails", eval.toClassDetailsString("其他度量指标:"));//TP/FP率、查全率等
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return summary;	//尽量避免在 finally里写return
		}
		return summary;
	}

	/**
	 * 交叉验证
	 * @param Folds 几折
	 * @param seed	随机数种子
	 * @return 性能度量结果 {"summary": **, "matrix": **, "classDetails"：**}
	 * @throws Exception
	 */
	public Map<String,String> crossValidateModel(int Folds,long seed) throws Exception {
		Evaluation eval = new Evaluation(instances);
		eval.crossValidateModel(model, instances,Folds , new Random(seed));	
		return getSummary(eval);
	}

	/**
	 * 自身验证
	 * @return	性能度量结果 {"summary": **, "matrix": **, "classDetails"：**}
	 * @throws Exception
	 */
	public Map<String,String> evalutionModelBySelf() throws Exception {
		return evalutionModel(instances, instances);
	}
	
	/**
	 * 分割验证
	 * @param ratio 训练集和测试集比例
	 * @param seed	随机数种子
	 * @return 性能度量结果 {"summary": **, "matrix": **, "classDetails"：**}
	 * @throws Exception
	 */
	public Map<String,String> splitEvalution(double ratio,long seed) throws Exception {		
		//dataset[0]训练集，dataset[1]测试集
		Instances[] dataset = DataUtils.splitTrainTest(instances, ratio, seed);		
		return evalutionModel(dataset[0], dataset[0]);
	}
	
	/**
	 * 模型验证
	 * @param train 训练集
	 * @param test	测试机
	 * @return	性能度量结果 {"summary": **, "matrix": **, "classDetails"：**}
	 * @throws Exception
	 */
	public Map<String,String> evalutionModel(Instances train, Instances test) throws Exception {
		model.buildClassifier(train);
		Evaluation eval = new Evaluation(train);	
		eval.evaluateModel(model, test);
		return getSummary(eval);
	}

}
