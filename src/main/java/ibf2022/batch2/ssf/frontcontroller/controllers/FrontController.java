package ibf2022.batch2.ssf.frontcontroller.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2022.batch2.ssf.frontcontroller.models.Login;
import ibf2022.batch2.ssf.frontcontroller.services.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping
@Controller
public class FrontController {

	@Autowired
	private AuthenticationService authSvc;

	// TODO: Task 2, Task 3, Task 4, Task 6
	@GetMapping(path={"/", "/index.html"})
	public String showLogin(Model m, HttpSession ss) {
		Login login = (Login) ss.getAttribute("login");

		if(login == null) {
			login = new Login();
		}
		
		if(login.isCaptcha()) {
			login.generateCaptcha();
		}
		
		ss.setAttribute("login", login);
		m.addAttribute("login", login);
		return "view0";
	}

	@PostMapping(path="/login")	
	public String postLogin(Model m, HttpSession ss, @Valid Login login, 
			BindingResult bindings) throws Exception {
		
		if(bindings.hasErrors()) {
			m.addAttribute("login", login);
			return "view0";
		}

		if(authSvc.isLocked(login.getUsername())){
			return "view2";
		}

		if(login.isCaptcha()) {
			String userInput = login.getUseranswer();
			if(Integer.parseInt(userInput) != login.getAnswer()) {
				FieldError error = new FieldError("login", "useranswer", "Captcha incorrect");
				bindings.addError(error);
				login.setFailed(login.getFailed()+1);
				if(login.getFailed() >= 3){
					authSvc.disableUser(login.getUsername());
				}
				login.generateCaptcha();
				ss.setAttribute("login", login);
				m.addAttribute("login", login);
				return "view0";
			}
		}

		String respMessage = authSvc.authenticate(login.getUsername(), login.getPassword());
		
		if(!respMessage.contains("Authenticated")) {
			FieldError error = null;
			if(respMessage.contains("Invalid")) {
				error = new FieldError("login", "password", respMessage);
			} 
			if (respMessage.contains("Incorrect")) {
				error = new FieldError("login", "password", respMessage);
			}
			bindings.addError(error);
			login.setFailed(login.getFailed()+1);
			if(login.getFailed() >= 3){
				authSvc.disableUser(login.getUsername());
			}
			login.generateCaptcha();
			ss.setAttribute("login", login);
			m.addAttribute("login", login);
			return "view0";
			//loginFailed(m, ss, login, bindings, error);
		}

		return "view1";	
	}

	@GetMapping(path="/logout")
	public String logout (Model m, HttpSession ss){
		ss.invalidate();
		return "redirect:/";
	}

	public String loginFailed(Model m, HttpSession ss, Login login
			, BindingResult bindings, FieldError error){
		login.setFailed(login.getFailed()+1);
		if(login.getFailed() >= 3){
			authSvc.disableUser(login.getUsername());
		}
		login.generateCaptcha();
		ss.setAttribute("login", login);
		m.addAttribute("login", login);
		bindings.addError(error);
		return "view0";	
	}
}
