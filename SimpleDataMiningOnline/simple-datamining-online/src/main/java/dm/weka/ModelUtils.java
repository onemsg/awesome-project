package dm.weka;

import dm.model.DataLocation;
import dm.model.ModelInfo;

/**
 * ModelUtils
 */
public class ModelUtils {

    private ModelUtils(){

    }

    public static ModelInfo toEvaluate(ModelInfo modelInfo, DataLocation dataLocation) throws Exception {

        modelInfo.setDataName(dataLocation.getName());
        //得到模型
        ModelSelect modelSelect = ModelSelect.start(modelInfo.getModelName(), modelInfo.getOptions());
        //得到模型验证
        ModelEvaluate modelEvaluate = ModelEvaluate.start(DataUtils.getInstances(dataLocation.getPath()),
                modelSelect.getModel());
        //选择模型验证方法
        String evalMothod = modelInfo.getEvalMethod();  
        if( evalMothod.equals("use-training-test") ){
            modelInfo.setSummary(modelEvaluate.evalutionModelBySelf());
        } else if( evalMothod.equals("cross-validation") ){
            modelInfo.setSummary(modelEvaluate.crossValidateModel(modelInfo.getkFlods(), 2019));
        } else if( evalMothod.equals("split-validation") ){
            modelInfo.setSummary(modelEvaluate.splitEvalution(modelInfo.getSplitRadio(), 2019));
        }
        return modelInfo;
    }
}