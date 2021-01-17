package dm.servlet;

import java.io.IOException;
//import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dm.weka.core.*;
import dm.dao.DataSet;

@WebServlet("/ModelSelectEvaluateServlet")
public class ModelSelectEvaluateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ModelSelectEvaluateServlet() {
        super();    
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
        request.setCharacterEncoding("UTF-8");
//        Enumeration<String> names = request.getParameterNames();
//        while (names.hasMoreElements()){
//            String name = (String)names.nextElement(); // 得到名字
//            System.out.print(name + "\t");
//            String[] values = request.getParameterValues(name);
//            for (int i=0; values!=null&&i<values.length; i++){
//                System.out.println(values[i]);
//            }
//        }
				
		HttpSession session = request.getSession(false);
		DataSet dataSet = ((DataSet) session.getAttribute("DATA"));
		if(dataSet == null) {
			response.sendRedirect("data-selection.jsp");
			return;
		}
		
		String modelName = request.getParameter("modelName");		
//		System.out.println("模型名字是:  " + modelName);		
		String options = request.getParameter("options");		

		String dataName = dataSet.getDataName();
		String dataPath = dataSet.getDataPath();
		DataExploration explor = null;
		String error = "";
		
		try {
			explor = new DataExploration(dataPath);
		} catch (Exception e) {
			error += "数据读取出错;\n";
			e.printStackTrace();
		}
		
		ModelSeletion modelSeletion = new ModelSeletion(modelName);

		try {
			if(options != null) {
				modelSeletion.setOptions(options);
			}			
		} catch (Exception e) {
			error += "模型参数设置出错;\n";
			e.printStackTrace();
		}
		
		ModelEvaluation eval = null;
		try {
			eval = new ModelEvaluation(explor, modelSeletion);
		} catch (Exception e) {
			error += "模型验证配置出错;\n";
			e.printStackTrace();
		}
		
		String evaluate_method = request.getParameter("evaluate-mothod");
		String[] summary = null; //用来保存模型验证结果
		switch(evaluate_method) {
		case "use-training-test":
			try {
				summary = eval.evalutionModelBySelf();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				error += "用训练集验证出错;\n";
				e.printStackTrace();
			}
			break;
		case "cross-validation" :
			try {
				String kFlods = request.getParameter("k-folds");
				summary = eval.crossValidateModel(Integer.valueOf(kFlods), 2019);
				session.setAttribute("KFlods", kFlods);
			} catch (Exception e) {
				e.printStackTrace();
				error += "交叉验证出错;\n";
			}
			break;
		case "split-validation":
			try {
				Double ratio = Double.valueOf(request.getParameter("split-ratio"));
				ratio = ratio / 100;
				summary = eval.splitEvalution(ratio,2019);
				session.setAttribute("ratio",ratio);
			} catch (Exception e) {
				error += "流出法验证出错;\n";
				e.printStackTrace();
			}
			break;
		}
		
		session.setAttribute("modelName", modelName); //模型名字
		session.setAttribute("options", options);	//模型参数
		session.setAttribute("dataName",dataName);	//数据集名称
		session.setAttribute("summary", summary);	//结果
		session.setAttribute("evaluate_method", evaluate_method); //验证方法
		if( !error.equals("")) {
			session.setAttribute("error", error);
		}else {
			session.setAttribute("error", null);
		}
		response.sendRedirect("model-evaluation.jsp");
	}

}
