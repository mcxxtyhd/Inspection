package com.test;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
public class JsonString {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 HashMap<String, Object> map = new HashMap<String, Object>();
		  map.put("username", "zhangsan");
		  map.put("age", 24);
		  map.put("sex", "男");
		  String jsonString = JSON.toJSONString(map);
		  //map={sex=男, username=zhangsan, age=24}
	      //JSON={"age":24,"sex":"男","username":"zhangsan"}
		  System.out.println("map=" + map);
		  System.out.println("JSON=" + jsonString);
		  
		  String json1 = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss");
		  System.out.println("JSON1=" + json1);
		  
		  long millis = 1324138987429L; 

		  Date date = new Date(millis);         

		  System.out.println(JSON.toJSONString(date));
		  
		  String json2  = JSON.toJSONString(date, SerializerFeature.WriteDateUseDateFormat);
		  
		  String json3  =  JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd HH:mm:ss.SSS");
		  
		  System.out.println("json2 : "+json2 );
		  System.out.println("json3 :  "+json3 );
	}

}
