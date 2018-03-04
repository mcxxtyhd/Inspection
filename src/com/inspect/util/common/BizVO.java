package com.inspect.util.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * 
 * BaseVO.java
 * 
 */
public class BizVO implements IValueObject {

	protected BizVO() {

	}
	private static final long serialVersionUID = 1L;

	private Log logger = LogFactory.getLog(BizVO.class);

	@SuppressWarnings("unchecked")
	public String toCacheKey() {
		Object[] object = null;
		Method[] methods = this.getClass().getMethods();
		StringBuffer ret = new StringBuffer("");
		for (int i = 0; i < methods.length; i++) {
			String methodName = methods[i].getName();
			if (methodName.startsWith("get")) { // 只处理get方法
				// 过滤掉getClass和有参数的方法
				Class declarClass = methods[i].getDeclaringClass();
				Constructor[] constructor = declarClass.getConstructors();
				if (constructor.length == 0 || declarClass.isInterface()) {
					continue; // 如果是接口或抽象类的话则自动跳过
				}
				// 过滤掉getClass和有参数的方法
				if (methodName.equals("getClass")
						|| methods[i].getParameterTypes().length > 0) {
					continue;
				}
				String name = methodName.trim().substring(3);
				name = name.substring(0, 1).toLowerCase() + name.substring(1);
				Object value = null;
				try {
					value = methods[i].invoke(this, object);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				if (null != value) {
					if (BizVO.class.isAssignableFrom(value.getClass())) {
						ret.append("." + getDataType(value));
						ret.append(((BizVO) value).toCacheKey());
					} else if (value.getClass().isEnum()) {
						ret.append("." + name).append(".").append(value);
					} else if (Map.class.isAssignableFrom(value.getClass())) {
						ret.append(StringUtils.Map2Value(value));
					} else if (List.class.isAssignableFrom(value.getClass())) {
						ret.append(StringUtils.List2Value(value));
					} else if (value.getClass().isArray()) {
						ret.append(StringUtils.array2Value(value));
					} else {
						parse(ret, name, value);
						// ret.append("." + name).append(".").append(value);
					}

				}
			}
		}
		return ret.toString();
	}

	public  void parse(StringBuffer sb, String name, Object value) {
		if (Date.class.isAssignableFrom(value.getClass())) {
			sb.append("." + name).append(".").append(
					DateUtils
							.dateToString((Date) (value)));
		} else if (String.class.isAssignableFrom(value.getClass())) {
			String val = (String) value;
			if (org.apache.commons.lang.StringUtils.isNotBlank(val)) {
				sb.append("." + name).append(".").append(val);
			}
		} else if (Double.class.isAssignableFrom(value.getClass())) {
			Double dbl = (Double) value;
			if (null != dbl) {
				sb.append("." + name).append(".").append(dbl);
			}
		} else if (BigDecimal.class.isAssignableFrom(value.getClass())){
			BigDecimal big = (BigDecimal) value;
			if (null != big) {
				sb.append("." + name).append(".").append(big);
			}
		}else if (Long.class.isAssignableFrom(value.getClass())){
			Long big = (Long) value;
			if (null != big) {
				sb.append("." + name).append(".").append(big);
			}
		} else if (Calendar.class.isAssignableFrom(value.getClass())) {
			sb.append("." + name).append(".").append(
					DateUtils
							.toDateStr((Calendar) (value)));
		}  else {
			sb.append("." + name).append(".").append(value);
		}
	}

	/**
	 * 得到传入参数的具体类型；
	 * 
	 * @param orb：Object
	 * @return 参数的具体类型
	 * 
	 */
	public  String getDataType(Object obj) {
		if (obj == null) {
			return null;
		}

		String type = obj.getClass().getName();

		int pos = type.lastIndexOf(".");
		if (pos >= 0) {
			type = type.substring(pos + 1);
		}

		return type;
	}

	/**
	 * 返回类的基本信息, 包括类的名称和类的基本属性值信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String toString() {
		Object[] object = null;
		Method[] methods = this.getClass().getMethods();
		StringBuffer ret = new StringBuffer(super.toString());
		ret.append(" = {\r\n");
		for (int i = 0; i < methods.length; i++) {
			try {
				String methodName = methods[i].getName();
				if (methodName.startsWith("get")) { // 只处理get方法

					// 过滤掉getClass和有参数的方法
					Class declarClass = methods[i].getDeclaringClass();
					Constructor[] constructor = declarClass.getConstructors();
					if (constructor.length == 0 || declarClass.isInterface()) {
						continue; // 如果是接口或抽象类的话则自动跳过
					}

					// 过滤掉getClass和有参数的方法
					if (methodName.equals("getClass")
							|| methods[i].getParameterTypes().length > 0) {
						continue;
					}

					String tmp = methodName.trim().substring(3);
					tmp = tmp.substring(0, 1).toLowerCase() + tmp.substring(1);
					ret.append("\t").append(tmp).append(" = ").append(
							methods[i].invoke(this, object)).append("\r\n");
				}
			} catch (Exception ex) {
				logger.error("toString:" + ex);
				continue;
			}
		}
		ret.append("}\r\n");
		return ret.toString();
	}

	/**
	 * 重载Object的equals函数
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!obj.getClass().equals(getClass())) {
			return false;
		}
		Object[] object = null;
		Method[] methods = this.getClass().getMethods();
		Field[] fields = this.getClass().getFields();
		boolean flag = true;

		for (int i = 0; flag && i < methods.length; i++) {
			try {
				String methodName = methods[i].getName();
				if (methodName.startsWith("get")) { // 只处理get方法
					// 过滤掉getClass和有参数的方法
					Class declarClass = methods[i].getDeclaringClass();
					Constructor[] constructor = declarClass.getConstructors();
					if (constructor.length == 0 || declarClass.isInterface()) {
						continue; // 如果是接口或抽象类的话则自动跳过
					}

					if (methodName.equals("getClass")
							|| methods[i].getParameterTypes().length > 0) {
						continue;
					}
					// String tmp = methodName.trim().substring(3);
					Object proObj1 = methods[i].invoke(this, object);
					Object proObj2 = methods[i].invoke(obj, object);
					if (proObj1 != null) {
						flag = (proObj1).equals(proObj2);
					} else if (proObj2 != null) {
						flag = (proObj2).equals(proObj1);
					}
				}
			} catch (Exception ex) {
				logger.error("equals:" + ex);
				continue;
			}
		}

		for (int i = 0; flag && i < fields.length; i++) {
			try {
				Object fields1 = fields[i].get(this);
				Object fields2 = fields[i].get(obj);
				if (fields1 != null) {
					flag = (fields1).equals(fields2);
				} else if (fields2 != null) {
					flag = (fields2).equals(fields1);
				}
			} catch (Exception ex) {
				logger.error("equals:" + ex);
				continue;
			}
		}

		return flag;
	}

	/**
	 * 把自己的对外属性封装成一个HashMap返回 键:小写的属性名称 值:该属性对应的值 这成为一种规范.
	 */
	@SuppressWarnings("unchecked")
	public Map<Object, Object> toHashMap() {
		Map<Object, Object> hashMap = new HashMap<Object, Object>();
		Method[] methods = this.getClass().getMethods();
		// Field[] fields = this.getClass().getFields();
		Object[] object = null;
		// boolean flag = true;
		for (int i = 0; i < methods.length; i++) {
			try {
				String methodName = methods[i].getName();
				if (methodName.startsWith("get")) { // 只处理get方法
					// 过滤掉getClass和有参数的方法
					Class declarClass = methods[i].getDeclaringClass();
					Constructor[] constructor = declarClass.getConstructors();
					if (constructor.length == 0 || declarClass.isInterface()) {
						continue; // 如果是接口或抽象类的话则自动跳过
					}

					if (methodName.equals("getClass")
							|| methods[i].getParameterTypes().length > 0) {
						continue;
					}
					String filedName = methodName.substring(3);
					String upper = filedName.substring(0, 1);
					filedName = filedName.replaceFirst(upper, upper
							.toLowerCase());
					// String tmp =
					// methodName.trim().substring(3).toLowerCase();
					Object obj = methods[i].invoke(this, object);
					hashMap.put(filedName, obj);
				}
			} catch (Exception ex) {
				logger.error("toHashmap:" + ex);
				continue;
			}
		}
		return hashMap;
	}

	/**
	 * 重写clone
	 * 
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings("unchecked")
	public <T> T clone(Class<T> type) {
		// String boClassName = this.getClass().getName();
		T resultObject = null;
		T[] object = null;

		try {
			resultObject = type.cast(super.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Method[] methods = this.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			String methodName = methods[i].getName();
			if (methodName.startsWith("get")) { // 只处理get方法
				Class declarClass = methods[i].getDeclaringClass();
				Constructor[] constructor = declarClass.getConstructors();

				if (constructor.length == 0 || declarClass.isInterface()) {
					continue; // 如果是接口或抽象类的话则自动跳过
				}
				// 采用此方式可以避免BO基类get方法调用问题，屏蔽掉抽象类和接口的方法
				// 也可用架构的包路径和开发路径不同判断，但更危险
				if (methods[i].getParameterTypes().length > 0
						|| methodName.equals("getClass")) {
					continue; // 如果是getClass或参数大于0个取消或为父类的get方法
				}
				// 寻找set == get
				for (int j = 0; j < methods.length; j++) {
					String methodNameNew = methods[j].getName();
					if (!methodNameNew.startsWith("set")) {
						continue;
					}
					StringBuffer sf = new StringBuffer();
					sf.append("g");
					sf.append(methodNameNew.substring(1));
					if (sf.toString().equals(methodName)) {
						Object result;
						try {
							result = methods[i].invoke(this, object);
							Object[] params = new Object[1];
							params[0] = result;
							methods[j].invoke(resultObject, params);
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}
		}

		return resultObject;
	}

	/**
	 * 重写clone
	 */
	@SuppressWarnings("unchecked")
	public Object clone() {
		// String boClassName = this.getClass().getName();
		Object resultObject = null;
		Object[] object = null;
		try {
			// Class c = Class.forName(boClassName);
			resultObject = (super.clone());
			Method[] methods = this.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				String methodName = methods[i].getName();
				if (methodName.startsWith("get")) { // 只处理get方法
					Class declarClass = methods[i].getDeclaringClass();
					Constructor[] constructor = declarClass.getConstructors();

					if (constructor.length == 0 || declarClass.isInterface()) {
						continue; // 如果是接口或抽象类的话则自动跳过
					}
					// 采用此方式可以避免BO基类get方法调用问题，屏蔽掉抽象类和接口的方法
					// 也可用架构的包路径和开发路径不同判断，但更危险
					if (methods[i].getParameterTypes().length > 0
							|| methodName.equals("getClass")) {
						continue; // 如果是getClass或参数大于0个取消或为父类的get方法
					}
					// 寻找set == get
					for (int j = 0; j < methods.length; j++) {
						String methodNameNew = methods[j].getName();
						if (!methodNameNew.startsWith("set")) {
							continue;
						}
						StringBuffer sf = new StringBuffer();
						sf.append("g");
						sf.append(methodNameNew.substring(1));
						if (sf.toString().equals(methodName)) {
							Object result = methods[i].invoke(this, object);
							Object[] params = new Object[1];
							params[0] = result;
							methods[j].invoke(resultObject, params);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("VO对象clone失败", ex);
			ex.printStackTrace();
		}
		return resultObject;
	}

}
