package dm.servlet;

import dm.dao.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

@WebServlet("/DataSelectServlet")
public class DataSelectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DataSelectServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int dataID = Integer.valueOf(request.getParameter("id"));
		DataSet data = null;
		try {
			data = Factory.getDataSetDao().getDataSet(dataID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("DATA", data);				
		response.setCharacterEncoding("UTF-8");
		response.sendRedirect("data-exploration.jsp");
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String dir = "E:\\elipse-workspace\\DataMiningOnline\\dataset\\temp";
		System.out.println("Save to path: " + dir);
		MultipartRequest mr = new MultipartRequest(request, dir);		
		String formName = "upload-data";		
		String fileName = mr.getFilesystemName(formName).split("[.]")[0]; //获得 iris.arff 点前面的名字
		File f = mr.getFile(formName);
		if(f == null) {
			throw new ServletException("file is not exist");
		}
		String filePath = f.getPath();
		DataSet data = new DataSet(-1, fileName, filePath);
		HttpSession session = request.getSession();
		session.setAttribute("DATA", data);			
		response.setCharacterEncoding("UTF-8");
		response.sendRedirect("data-exploration.jsp");
	}
}
