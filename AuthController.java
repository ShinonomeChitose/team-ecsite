package jp.co.internous.duo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import jp.co.internous.duo.model.domain.MstUser;
import jp.co.internous.duo.model.form.UserForm;
import jp.co.internous.duo.model.mapper.MstUserMapper;
import jp.co.internous.duo.model.mapper.TblCartMapper;
import jp.co.internous.duo.model.session.LoginSession;

@RestController
@RequestMapping("/duo/auth")
public class AuthController {

	@Autowired
	private MstUserMapper mstUserMapper;
	
	@Autowired
	private TblCartMapper tblCartMapper;
	
	@Autowired
	private LoginSession loginSession;
	
	private Gson gson = new Gson();
	
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public String login(@RequestBody UserForm f) {
		List<MstUser> users = mstUserMapper.findByUserNameAndPassword(f.getUserName(),f.getPassword());
		MstUser user = null;
		
		if(users != null && users.size() > 0) {
			user = users.get(0);
			
			int result = tblCartMapper.findCountByUserId(loginSession.getTmpUserId());
			if(result > 0){
				result = tblCartMapper.updateUserId(user.getId(),loginSession.getTmpUserId());
			}
			
			loginSession.setUserId(user.getId());
			loginSession.setTmpUserId(0);
			loginSession.setUserName(user.getUserName());
			loginSession.setPassword(user.getPassword());
			loginSession.setLoginFlg(true);
		}
		return gson.toJson(user);
	}
	
	@RequestMapping("/logout")
	public String logout(Model m) {
		loginSession.setUserId(0);
		loginSession.setTmpUserId(0);
		loginSession.setUserName(null);
		loginSession.setPassword(null);
		loginSession.setLoginFlg(false);
		
		m.addAttribute("loginSession",loginSession);
		
		return "0";
	}
	
	@RequestMapping("/resetPassword")
	public String resetPassword(@RequestBody UserForm f) {
		String newPassword = f.getNewPassword();
		String newPasswordConfirm = f.getNewPasswordConfirm();
		
		List<MstUser> users = mstUserMapper.findByUserNameAndPassword(f.getUserName(), f.getPassword());

		if(users.size() == 0 || users == null){
			return "現在のパスワードが正しくありません。";
		}

		MstUser user = new MstUser();
		user = users.get(0);

		if(user.getPassword().equals(newPassword)){
			return "現在のパスワードと同一文字列が入力されました。";
		}

		if(!newPassword.equals(newPasswordConfirm)){
			return "新パスワードと確認用パスワードが一致しません。";
		}
		
		// mst_userとloginSessionのパスワードを更新する
		mstUserMapper.updatePassword(user.getUserName(), f.getNewPassword());
		loginSession.setPassword(f.getNewPassword());
		
		return "パスワードが再設定されました。";
	}
}
