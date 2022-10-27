package jp.co.internous.duo.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import jp.co.internous.duo.model.domain.MstUser;

@Mapper
public interface MstUserMapper {
	@Select("SELECT * FROM mst_user WHERE user_name = #{userName} AND password = #{password}")
	List<MstUser> findByUserNameAndPassword(@Param("userName") String userName,@Param("password") String password);
	
	@Select("SELECT user_name FROM mst_user WHERE user_name = #{userName}")
	List<MstUser> findDuplicatedUserName(String userName);
	
	@Insert("INSERT INTO mst_user"
			+ "(user_name,password,family_name,first_name,family_name_kana,first_name_kana,gender,created_at,updated_at)"
			+ "VALUES"
			+ "(#{userName},#{password},#{familyName},#{firstName},#{familyNameKana},#{firstNameKana},#{gender},now(),now())")
	@Options(useGeneratedKeys = true, keyProperty = "id")
	void insert(MstUser user);
	
	/**
	 * ユーザー名を条件にパスワードを更新する
	 * @param userName ユーザー名
	 * @param password パスワード
	 * @return 更新件数
	 */
	@Update("UPDATE mst_user SET password = #{password}, updated_at = now() WHERE user_name = #{userName}")
	int updatePassword(
			@Param("userName") String userName,
			@Param("password") String password);
}
