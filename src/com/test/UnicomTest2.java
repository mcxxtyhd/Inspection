package com.test;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class UnicomTest2 {

	static String urlLocal = "jdbc:mysql://localhost:3306/inspect?user=root&password=123";
	
	public static void main(String[] args) {
		SimpleJdbcTemplate jdbcLocal = config(urlLocal);
		
		test(jdbcLocal);
	}
	
	public static void test(SimpleJdbcTemplate jdbc) {
		String sql = "select * from unicominfo";
		List<Map<String, Object>> list = jdbc.queryForList(sql);
		list.get(0);
		System.out.println("ok: "+ list.get(0));
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
	
}

