package jp.co.internous.duo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.internous.duo.model.domain.MstUser;
import jp.co.internous.duo.model.form.UserForm;
import jp.co.internous.duo.model.mapper.MstUserMapper;
import jp.co.internous.duo.model.session.LoginSession;

@Controller
@RequestMapping("/duo/user")
public class UserController {

	@Autowired
	MstUserMapper mstUserMapper;
	
	@Autowired
	LoginSession loginSession;
	
	@RequestMapping("/")
	public String index(Model m) {
		m.addAttribute("loginSession",loginSession);
		
		return "register_user";
	}
	
	@ResponseBody
	@RequestMapping(value="/duplicatedUserName",method = RequestMethod.POST)
	public String checkDuplicatedUserName(@RequestBody UserForm f) {
		List<MstUser> result = mstUserMapper.findDuplicatedUserName(f.getUserName());
		
		if(result != null && result.size() > 0) {
			return "1";
		}
		
		return "0";
	}
	
	@ResponseBody
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public String register(@RequestBody UserForm f) {
		MstUser user = new MstUser(f);
		
		mstUserMapper.insert(user);
		
		return "0";
	}
}
