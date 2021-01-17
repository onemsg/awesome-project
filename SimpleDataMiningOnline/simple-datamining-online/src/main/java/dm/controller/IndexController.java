package dm.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import dm.model.ModelInfo;

/**
 * IndexController
 */
@Controller
public class IndexController {

    @RequestMapping(path = { "/", "data-selection" })
    public String DataSelection(){
        return "data-selection";
    }

    @RequestMapping(path = "/data-exploration")
    public String DataExploration(@RequestParam(value = "name", required = false) String name, HttpServletResponse response) {
        if (name != null) {
            Cookie cookie = new Cookie("dataName", name);
            response.addCookie(cookie);
        }
        return "data-exploration";
    }

    @GetMapping(value = "/model-evaluation-selection")
    public String ModelEvaluationSelection(){
        return "model-evaluation-selection";
    }

    @RequestMapping(value = "/model-evaluation", method = {RequestMethod.GET,RequestMethod.POST})
    public String ModelEvaluation( ModelInfo modelInfo, HttpSession session){
        if( !modelInfo.isEmpty()) session.setAttribute("modelInfo", modelInfo);
        return "model-evaluation";  
    }
}