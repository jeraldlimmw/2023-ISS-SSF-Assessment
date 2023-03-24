package ibf2022.batch2.ssf.frontcontroller.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2022.batch2.ssf.frontcontroller.models.Login;
import jakarta.servlet.http.HttpSession;

@RequestMapping(path="/protected")
@Controller
public class ProtectedController {

	// TODO Task 5
	// Write a controller to protect resources rooted under /protected
	@GetMapping(path="{view}")
	public String getProtectedPage(Model m, HttpSession ss, @PathVariable String view){
		Login login = (Login) ss.getAttribute("login");
		if(!login.isAuthenticated()){
			return "view0";
		}
		return view;
	}

}
