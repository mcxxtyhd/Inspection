package com.test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.inspect.dao.BaseDaoI;
import com.inspect.model.system.Enterprise;
import com.inspect.model.system.Parameter;
import com.inspect.model.system.TSFunction;
import com.inspect.model.system.TSRole;
import com.inspect.model.system.TSRoleFunction;
import com.inspect.model.system.TSRoleUser;
import com.inspect.model.system.TSUser;
import com.inspect.service.SystemServiceI;
import com.inspect.util.system.CriteriaQuery;
import com.inspect.vo.basis.UnicomVo;
import com.inspect.vo.comon.ComboTreeModel;
import com.inspect.vo.system.EnterpriseVo;
import com.inspect.vo.system.PageRole;
import com.inspect.vo.system.PageUser;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class UnicomTest{
	

	public List<UnicomVo> comboboxUnicom1(int qsql) {
		System.out.println("466comboboxUnicom(int qsql): "+qsql);
		
		StringBuffer buf=new StringBuffer("select administrativedivision,basename from unicominfo where ");
		if(qsql==0){
			buf.append("1=1");
		}else{
			buf.append(" id=").append(qsql);
		}
		//而spring的jdbcTemplate.queryForList( sql );
		//先返回一个object，这个object里面是一个map，对应的key就是数据库里面的字段名，value就是我们要取的值了！
		String sql = "select * from unicominfo";
		//List<UnicomVo> l = baseDao.getJdbcTemplate().queryForList(sql);//(buf.toString());
		
		System.out.println("476systemserviceimpl list长度: "+l.size());
		return l;
	}
	
	public static SimpleJdbcTemplate config(String url) {
		
		MysqlDataSource mds = new MysqlDataSource();
		mds.setUrl(url);
		
		try {
			mds.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new SimpleJdbcTemplate(mds);
	}
	static String urlLocal = "jdbc:mysql://localhost:3306/inspect?user=root&password=123";
	
	
	public static void main(String args[]){
		SimpleJdbcTemplate jdbcLocal = config(urlLocal);
		test(jdbcLocal);
		//UnicomTest u = new UnicomTest();
		//u.comboboxUnicom1(0);
	}
	public static void test(SimpleJdbcTemplate jdbc) {
		String sql = "select administrativedivision,basename from unicominfo";
		List<Map<String, Object>> list = jdbc.queryForList(sql);
		list.get(0);
		System.out.println("ok: "+ list.get(0));
	}
}
