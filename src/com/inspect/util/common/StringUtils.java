package com.inspect.util.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




/**
 * 字符串处理函数工具包 StringUtils.java
 */
public class StringUtils {

	private StringUtils() {
	}

	private static int PRECISION = 2;

	private static String[] chrChineseUnit = { "分", "角", "元", "拾", "佰", "仟",
			"万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟" };

	private static String[] chrChineseNumber = { "零", "壹", "贰", "叁", "肆", "伍",
			"陆", "柒", "捌", "玖" };

	private static String chrChineseFull = "整";

	private static String chrChineseNegative = "负";

	public static final String EMPTY = "";

	public static final int NUMERIC_SHORT = 0;

	public static final int NUMERIC_INT = 1;

	public static final int NUMERIC_LONG = 2;

	public static final int NUMERIC_FLOAT = 3;

	public static final int NUMERIC_DOUBLE = 4;

	public static String parseValue(Object obj) {
		String value = null;
		if (isPrimaryType(obj))
			value = "" + obj;
		else if (obj instanceof Date)
			value = DateUtils.dateToString((Date) obj);
		else if (obj instanceof Calendar)
			value = DateUtils.toDateStr((Calendar) obj);
		else if (obj instanceof Timestamp) {
			value = DateUtils.toDateStr(DateUtils
					.convSqlTimestampToUtilCalendar((Timestamp) obj));
		} else
			value = null;
		return value;
	}

	@SuppressWarnings("unchecked")
	public static String List2Value(Object obj) {
		StringBuffer sb = new StringBuffer("");
		List data = (List) obj;
		for (int i = 0; i < data.size(); i++) {
			Object o = data.get(i);
			if (null != o) {
				if (BizVO.class.isAssignableFrom(o.getClass())) {
					sb.append(((BizVO) o).toCacheKey());
				} else if (obj.getClass().isEnum()) {
					sb.append(enum2Value(o));
				} else {
					sb.append(o);
				}
			}
		}
		return sb.toString();
	}

    /**
     * 将Null转为""
     * @param temp
     * @return
     */
	public static String toNull(Object temp){
        try {
        	
            if (temp == null || temp.equals("")) {
                return "" ;
            } else {
            	return String.valueOf(temp).trim() ;
            }
        } catch (Exception e) {
            return "" ;
        }
	}
	@SuppressWarnings("unchecked")
	public static String Map2Value(Object obj) {
		StringBuffer sb = new StringBuffer("");
		Map data = (Map) obj;
		Iterator set = data.keySet().iterator();
		while (set.hasNext()) {
			String key = (String) set.next();
			Object o = data.get(key);
			if (null != o) {
				if (BizVO.class.isAssignableFrom(o.getClass())) {
					sb.append(((BizVO) o).toCacheKey());
				} else if (obj.getClass().isEnum()) {
					sb.append(enum2Value(o));
				} else {
					sb.append(o);
				}
			}

		}
		return sb.toString();
	}

	public static String array2Value(Object obj) {
		StringBuffer sb = new StringBuffer("");
		int len = Array.getLength(obj);
		for (int i = 0; i < len; i++) {
			if (null != obj) {
				Object o = Array.get(obj, i);
				if (null != o) {
					if (BizVO.class.isAssignableFrom(o.getClass())) {
						sb.append(((BizVO) o).toCacheKey());
					} else if (obj.getClass().isEnum()) {
						sb.append(enum2Value(o));
					} else {
						sb.append(o);
					}
				}
			}
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static String enum2Value(Object obj) {
		Object[] object = null;
		Method[] methods = obj.getClass().getMethods();
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
					value = methods[i].invoke(obj, object);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				if (null != value) {
					ret.append("." + name).append(".").append(value);
				}
			}
		}
		return ret.toString();
	}

	public static boolean isPrimaryType(Object obj) {
		return (obj instanceof String) || (obj instanceof Integer)
				|| (obj instanceof Long) || (obj instanceof Double)
				|| (obj instanceof Float) || (obj instanceof Byte);
	}

	/**
	 * Description 将数字金额转换为中文金额
	 * 
	 * @param
	 * <p>
	 * BigDecimal bigdMoneyNumber 转换前的数字金额
	 * </P>
	 * @return String 调用：myToChineseCurrency("101.89")="壹佰零壹圆捌角玖分"
	 *         myToChineseCurrency("100.89")="壹佰零捌角玖分"
	 *         myToChineseCurrency("100")="壹佰圆整"
	 */
	public static String DoNumberCurrencyToChineseCurrency(
			BigDecimal bigdMoneyNumber) {
		// 中文金额缓存
		StringBuffer sb = new StringBuffer();
		// 获取符号
		int signum = bigdMoneyNumber.signum();
		// System.out.println("signum=" + signum);
		if (signum == 0) {
			return "零元整";
		}
		// System.out.println(bigdMoneyNumber.scale());

		// 转换金额为long,精确到分
		long number = bigdMoneyNumber.movePointRight(PRECISION).setScale(0,
				BigDecimal.ROUND_HALF_UP).abs().longValue();

		long scale = number % 100;

		int numUnit = 0;
		int numIndex = 0;
		// 遇零标志
		boolean getZero = false;
		// while((scale = scale % 10) == 0){
		// numIndex ++;
		// number = number / 10;
		// getZero = true;
		// }
		if (scale == 0) {
			numIndex = 2;
			number = number / 100;
			getZero = true;
		}
		if (scale != 0 && scale % 10 == 0) {
			numIndex = 1;
			number = number / 10;
			getZero = true;
		}
		int zeroSize = 0;
		while (number > 0) {
			numUnit = (int) (number % 10);
			if (numUnit > 0) {
				// 非零处理
				if (numIndex == 9 && zeroSize >= 3) {
					sb.insert(0, chrChineseUnit[6]);
				}
				if (numIndex == 13 && zeroSize >= 3) {
					sb.insert(0, chrChineseUnit[10]);
				}
				sb.insert(0, chrChineseUnit[numIndex]);
				sb.insert(0, chrChineseNumber[numUnit]);
				getZero = false;
				zeroSize = 0;
			} else {
				// 为零处理
				zeroSize++;
				if (!getZero)
					sb.insert(0, chrChineseNumber[numUnit]);

				if (numIndex == 2) {
					if (number > 0) {
						sb.insert(0, chrChineseUnit[numIndex]);
					}
				} else if ((numIndex - 2) % 4 == 0) {
					if (number % 1000 > 0) {
						sb.insert(0, chrChineseUnit[numIndex]);
					}
				}
				getZero = true;
			}

			// 自除10
			number = number / 10;
			numIndex++;

		}

		// 负数追加首字 负

		if (signum == -1) {
			sb.insert(0, chrChineseNegative);
		}
		// 整数追加尾字 整

		if (scale == 0) {
			sb.append(chrChineseFull);
		}

		return sb.toString();
	}

	/**
	 * 字符串的字符集类型转换
	 * 
	 * @param src
	 *            需要转换的字符串
	 * @param fromCharSet
	 *            字符串当前的字符集类型，如"iso-8859-1","GBK"等
	 * @param toCharSet
	 *            目标字符集类型，如"iso-8859-1","GBK"等
	 * @return 转换后的字符串,失败返回null
	 */
	public static String charSetConvert(String src, String fromCharSet,
			String toCharSet) {
		if (src == null) {
			return src;
		}
		try {
			return new String(src.getBytes(fromCharSet), toCharSet);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将iso8859的字符集转换成UTF-8字符集
	 * 
	 * @param src
	 *            iso8859字符串
	 * @return 转化后的字符串,失败返回null
	 */
	public static String isoToUTF8(String src) {
		String arch = System.getProperty("os.arch");
		if ("x86".equalsIgnoreCase(arch)) {
			return src;
		}
		return charSetConvert(src, "iso-8859-1", "UTF-8");
	}

	/**
	 * 将UTF-8的字符集转换成iso8859字符集
	 * 
	 * @param src
	 *            UTF-8字符串
	 * @return 转化后的字符串,失败返回null
	 */
	public static String utf8ToISO(String src) {
		String arch = System.getProperty("os.arch");
		if ("x86".equalsIgnoreCase(arch)) {
			return src;
		}
		return charSetConvert(src, "UTF-8", "iso-8859-1");
	}

	/**
	 * 
	 * @将iso8859的字符集转换成UTF-8字符集
	 * @param src
	 *            iso8859字符串,force 强制转换标志
	 * @return 转化后的字符串,失败返回null
	 */
	public static String isoToUTF8(String src, boolean force) {
		if (force) {
			return charSetConvert(src, "iso-8859-1", "UTF-8");
		} else {
			return isoToUTF8(src);
		}
	}

	/**
	 * 将iso8859的字符集转换成GBK字符集
	 * 
	 * @param src
	 *            iso8859字符串
	 * @return 转化后的字符串,失败返回null
	 */
	public static String isoToGBK(String src) {
		return charSetConvert(src, "iso-8859-1", "GBK");
	}

	/**
	 * 将GBK的字符集转换成iso8859字符集
	 * 
	 * @param src
	 *            GBK字符串
	 * @return 转化后的字符串,失败返回null
	 */
	public static String gbkToISO(String src) {
		return charSetConvert(src, "GBK", "iso-8859-1");
	}

	/**
	 * 重复一个字符串 n 次，比如 abcabcabc
	 * 
	 * @param str
	 *            需要重复的字符串
	 * @param repeat
	 *            重复的次数
	 * @return 重复后生成的字符串
	 */
	public static String repeat(String str, int repeat) {
		StringBuffer buffer = new StringBuffer(repeat * str.length());
		for (int i = 0; i < repeat; i++) {
			buffer.append(str);
		}
		return buffer.toString();
	}

	/**
	 * 获得一个字符串的最左边的n个字符，如果长度len的长度大于字符串的总长度，返回字符串本身
	 * 
	 * @param str
	 *            原始的字符串
	 * @param len
	 *            左边的长度
	 * @return 最左边的字符
	 */
	public static String left(String str, int len) {
		if (len < 0) {
			throw new IllegalArgumentException("Requested String length " + len
					+ " is less than zero");
		}
		if ((str == null) || (str.length() <= len)) {
			return str;
		} else {
			return str.substring(0, len);
		}
	}

	/**
	 * 获得一个字符串的最右边的n个字符，如果长度len的长度大于字符串的总长度，返回字符串本身
	 * 
	 * @param str
	 *            原始的字符串
	 * @param len
	 *            右边的长度
	 * @return 最右边的字符
	 */
	public static String right(String str, int len) {
		if (len < 0) {
			throw new IllegalArgumentException("Requested String length " + len
					+ " is less than zero");
		}
		if ((str == null) || (str.length() <= len)) {
			return str;
		} else {
			return str.substring(str.length() - len);
		}
	}

	/**
	 * 将给定的字符串格式化为定长字符串, 原始字符串长度超过给定长度的,按照给定长度从左到右截取 如果原始字符串小于给定长度,
	 * 则按照给定字符在左端补足空位
	 * 
	 * @param src
	 *            原始字符串
	 * @param s2
	 *            补充用字符,
	 * @param length
	 *            格式化后长度
	 * @return 格式化后字符串
	 */
	public static String formatString(String src, char s2, int length) {
		String retValue = src;
		if (src == null || length <= 0) {
			return null;
		}

		if (src.length() > length) {
			retValue = src.substring(0, length);
		}

		for (int i = 0; i < length - src.length(); i++) {
			retValue = s2 + retValue;
		}

		return retValue;
	}

	/**
	 * 将一个浮点数转换为人民币的显示格式：￥##,###.##
	 * 
	 * @param value
	 *            浮点数
	 * @return 人民币格式显示的数字
	 */
	public static String toRMB(double value) {
		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.CHINA);
		return nf.format(value);
	}

	/**
	 * 默认保留小数点后两位，将一个浮点数转换为定长小数位的小数 ######.##
	 * 
	 * @param value
	 *            浮点数
	 * @return 定长小数位的小数
	 */
	public static String toCurrencyWithoutComma(double value) {
		String retValue = toCurrency(value);
		retValue = retValue.replaceAll(",", "");
		return retValue;
	}

	/**
	 * 默认保留小数点后两位，将一个浮点数转换为货币的显示格式：##,###.##
	 * 
	 * @param value
	 *            浮点数
	 * @return 货币格式显示的数字
	 */
	public static String toCurrency(double value) {
		return toCurrency(value, 2);
	}

	/**
	 * 根据指定的小数位数，将一个浮点数转换为货币的显示格式
	 * 
	 * @param value
	 *            浮点数
	 * @param decimalDigits
	 *            小数点后保留小数位数
	 * @return 货币格式显示的数字 <br>
	 *         <br>
	 *         例： toCurrency(123456.789,5) 将返回 "123,456.78900"
	 */
	public static String toCurrency(double value, int decimalDigits) {
		String format = "#,##0." + repeat("0", decimalDigits);
		NumberFormat nf = new DecimalFormat(format);
		return nf.format(value);

	}

	/**
	 * 将一个字符串格式化为给定的长度，过长的话按照给定的长度从字符串左边截取，反之以给定的 字符在字符串左边补足空余位 <br>
	 * 比如： <br>
	 * prefixStr("abc",'0',5) 将返回 00aaa <br>
	 * prefixStr("abc",'0',2) 将返回 ab
	 * 
	 * @param source
	 *            原始字符串
	 * @param profix
	 *            补足空余位时使用的字符串
	 * @param length
	 *            格式化后字符串长度
	 * @return 返回格式化后的字符串,异常返回null
	 */
	public static String prefixStr(String source, char profix, int length) {
		String strRet = source;
		if (source == null) {
			return strRet;
		}
		if (source.length() >= length) {
			strRet = source.substring(0, length);
		}

		if (source.length() < length) {
			for (int i = 0; i < length - source.length(); i++) {
				strRet = "" + profix + strRet;
			}
		}

		return strRet;
	}

	/**
	 * 格式化字符串,将字符串trim()后返回. 如果字符串为null,则返回长度为零的字符串("")
	 * 
	 * @param value
	 *            被格式化字符串
	 * @return 格式化后的字符串
	 */
	public static String stringTrim(String value) {
		if (value == null) {
			return "";
		}
		return value.trim();
	}

	/**
	 * 将"null"和null转换为""
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String trimNull(String str) {
		String result = "";
		if (!"null".equals(str)) {
			result = stringTrim(str);
		}
		return result;
	}

	/**
	 * 将一个字符串格式化为给定的长度，过长的话按照给定的长度从字符串左边截取，反之以给定的 字符在字符串右边补足空余位 <br>
	 * 比如： <br>
	 * suffixStr("abc",'0',5) 将返回 aaa00 <br>
	 * suffixStr("abc",'0',2) 将返回 ab
	 * 
	 * @param source
	 *            原始字符串
	 * @param profix
	 *            补足空余位时使用的字符串
	 * @param length
	 *            格式化后字符串长度
	 * @return 返回格式化后的字符串,异常返回null
	 */
	public static String suffixStr(String source, char suffix, int length) {
		String strRet = source;
		if (source == null) {
			return strRet;
		}
		if (source.length() >= length) {
			strRet = source.substring(0, length);
		}

		if (source.length() < length) {
			for (int i = 0; i < length - source.length(); i++) {
				strRet += suffix;
			}
		}
		return strRet;
	}

	/**
	 * 根据分割符sp，将str分割成多个字符串，并将它们放入一个ArrayList并返回，其规则是最后的 字符串最后add到ArrayList中
	 * 
	 * @param str
	 *            被分割的字符串
	 * @param sp
	 *            分割符字符串
	 * @return 封装好的ArrayList
	 */
	public static List<String> convertStrToArrayList(String str, String sp) {
		List<String> al = new ArrayList<String>();
		if (str == null) {
			return al;
		}
		String strArr[] = str.split(sp);
		for (int i = 0; i < strArr.length; i++) {
			al.add(strArr[i]);
		}

		return al;
	}

	// public static void main(String[] args) {
	// double dd = 50000000.1*.002;
	// String s2 =dd+"";
	// System.out.println( changeToBig(dd+" "));
	// System.out.println( changeToBig("0500000.0150"));
	//    	
	// System.out.println( changeToBig("1500"));
	// System.out.println( changeToBig("5000.000000001"));
	// Double je = new Double(50000.00000340);
	// System.out.println(changeToBig(je.toString()));
	// String wq = "0005000.00";
	// System.out.println(changeToBig(isoToUTF8(wq) ));
	// double d = 30000000d;
	// System.out.println(changeToBig(String.valueOf(d)));
	// d = 0.0000000d;
	// System.out.println(changeToBig(String.valueOf(d)));
	// d=2441.545;
	// System.out.println(changeToBig(String.valueOf(d)));
	// System.out.println(changeToBig(String.valueOf(54543.35)));
	// System.out.println(changeToBig(String.valueOf(5.233)));
	// System.out.println(changeToBig(String.valueOf(79789340000.00d)));
	// System.out.println(changeToBig(String.valueOf(43434)));
	// // for (int i = 0; i < 10000000; i++) {
	// // String s = changeToBig(String.valueOf(d));
	// // d+=0.001d;
	// // if (isZero(s)) {
	// // System.out.println(" d = "+d+ " "+s);
	// // }\
	// //
	// // }
	// // String sd = changeToBig("0500000.00");
	// // System.out.println(sd);
	// // System.out.println(isZero(sd));
	//    	
	// }
	// public static boolean isZero(String s ) {
	// int k=0;
	// for (int i = 0; i < s.length()-1; i++) {
	// if (s.charAt(i)=='零'&&s.charAt(i+1)=='零') {
	// k++;
	// }
	// if (k>1) {
	// return true;
	// }
	// }
	// return false;
	// }

	public static String changeToBig(String value) {
		return isoToUTF8(stringToBig(value));
	}

	private static String stringToBig(String value) {
		String result = "零元整";
		if (null == value || "".equals(value.trim())) {
			return result;
		}

		try {
			BigDecimal b = new BigDecimal(value);
			result = DoNumberCurrencyToChineseCurrency(b);

		} catch (Exception e) {
			//
			return "零元整";
		}
		return result;

	}

	/**
	 * 将数字字符串转换为人民币大写
	 * 
	 * @param value
	 *            金额人民币数字字符串
	 * @return 转换后的人民币大写字符串
	 * @author 王德春 : 友情提供
	 */
	public static String changeToBigOld(String value) {

		if (null == value || "".equals(value.trim())) {
			return "零";
		}
		// 正、负数判断标记:如果为负，则把负号去掉进行处理。 Add by Lichuanxin
		// ****start*******============================================================================================
		String flag = value;
		if ((flag.substring(0, 1).equals("-")))
			value = value.substring(1);
		// ****end*******================================================================================================

		// add by Johnson Zhang
		// int len = value.length();

		String strCheck, strFen, strDW, strNum, strBig, strNow;
		double d = 0;
		try {
			d = Double.parseDouble(value);
		} catch (Exception e) {
			return "数据" + value + "非法！";
		}

		strCheck = value + ".";
		int dot = strCheck.indexOf(".");
		if (dot > 12) {
			return "数据" + value + "过大，无法处理！";
		}

		try {
			int i = 0;
			strBig = "";
			strDW = "";
			strNum = "";

			// long intFen = (long)(d*100); //原来的处理方法

			/**
			 * 增加了对double转换为long时的精度，
			 * 
			 * 例如:(long)(208123.42 * 100) = 20812341 解决了该问题 add 庞学亮
			 * 
			 */
			BigDecimal big = new BigDecimal(d);
			big = big.multiply(new BigDecimal(100)).setScale(2, 4);
			long intFen = big.longValue();

			strFen = String.valueOf(intFen);
			int lenIntFen = strFen.length();
			while (lenIntFen != 0) {
				i++;
				switch (i) {
				case 1:
					strDW = "分";
					break;
				case 2:
					strDW = "角";
					break;
				case 3:
					strDW = "元";
					break;
				case 4:
					strDW = "拾";
					break;
				case 5:
					strDW = "佰";
					break;
				case 6:
					strDW = "仟";
					break;
				case 7:
					strDW = "万";
					break;
				case 8:
					strDW = "拾";
					break;
				case 9:
					strDW = "佰";
					break;
				case 10:
					strDW = "仟";
					break;
				case 11:
					strDW = "亿";
					break;
				case 12:
					strDW = "拾";
					break;
				case 13:
					strDW = "佰";
					break;
				case 14:
					strDW = "仟";
					break;
				}
				switch (strFen.charAt(lenIntFen - 1)) { // 选择数字

				case '1':
					strNum = "壹";
					break;
				case '2':
					strNum = "贰";
					break;
				case '3':
					strNum = "叁";
					break;
				case '4':
					strNum = "肆";
					break;
				case '5':
					strNum = "伍";
					break;
				case '6':
					strNum = "陆";
					break;
				case '7':
					strNum = "柒";
					break;
				case '8':
					strNum = "捌";
					break;
				case '9':
					strNum = "玖";
					break;
				case '0':
					strNum = "零";
					break;
				}
				// 处理特殊情况
				strNow = strBig;
				// 分为零时的情况

				if ((i == 1) && (strFen.charAt(lenIntFen - 1) == '0')) {
					strBig = "整";
					// 角为零时的情况

				} else if ((i == 2) && (strFen.charAt(lenIntFen - 1) == '0')) { // 角分同时为零时的情况
					if (!strBig.equals("整")) {
						strBig = "零" + strBig;
					}
				}
				// 元为零的情况
				else if ((i == 3) && (strFen.charAt(lenIntFen - 1) == '0')) {
					strBig = "元" + strBig;
					// 拾－仟中一位为零且其前一位（元以上）不为零的情况时补零

				} else if ((i < 7) && (i > 3)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (!("" + strNow.charAt(0)).equals("零"))
						&& (!("" + strNow.charAt(0)).equals("元"))) {
					strBig = "零" + strBig;
					// 拾－仟中一位为零且其前一位（元以上）也为零的情况时跨过

				} else if ((i < 7) && (i > 3)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (("" + strNow.charAt(0)).equals("零"))) {
				}
				// 拾－仟中一位为零且其前一位是元且为零的情况时跨过
				else if ((i < 7) && (i > 3)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (("" + strNow.charAt(0)).equals("元"))) {
				}
				// 当万为零时必须补上万字

				// modified by Johnson Zhang
				else if ((i == 7) && (strFen.charAt(lenIntFen - 1) == '0')) {
					strBig = "万" + strBig;
					// 拾万－仟万中一位为零且其前一位（万以上）不为零的情况时补零

				} else if ((i < 11) && (i > 7)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (!("" + strNow.charAt(0)).equals("零"))
						&& (!("" + strNow.charAt(0)).equals("万"))) {
					strBig = "零" + strBig;
					// 拾万－仟万中一位为零且其前一位（万以上）也为零的情况时跨过

				} else if ((i < 11) && (i > 7)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (("" + strNow.charAt(0)).equals("万"))) {
				}
				// 拾万－仟万中一位为零且其前一位为万位且为零的情况时跨过

				else if ((i < 11) && (i > 7)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (("" + strNow.charAt(0)).equals("零"))) {
				}
				// 万位为零且存在仟位和十万以上时，在万仟间补零
				else if ((i < 11) && (i > 8)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (("" + strNow.charAt(0)).equals("万"))
						&& (("" + strNow.charAt(2)).equals("仟"))) {
					strBig = strNum + strDW + "万零"
							+ strBig.substring(1, strBig.length());
					// 单独处理亿位
				} else if (i == 11) {
					// 亿位为零且万全为零存在仟位时，去掉万补为零

					if ((strFen.charAt(lenIntFen - 1) == '0')
							&& (("" + strNow.charAt(0)).equals("万"))
							&& (("" + strNow.charAt(2)).equals("仟"))) {
						strBig = "亿" + "零"
								+ strBig.substring(1, strBig.length());
						// 亿位为零且万全为零不存在仟位时，去掉万

					} else if ((strFen.charAt(lenIntFen - 1) == '0')
							&& (("" + strNow.charAt(0)).equals("万"))
							&& (("" + strNow.charAt(2)).equals("仟"))) {
						strBig = "亿" + strBig.substring(1, strBig.length());
						// 亿位不为零且万全为零存在仟位时，去掉万补为零
					} else if ((("" + strNow.charAt(0)).equals("万"))
							&& (("" + strNow.charAt(2)).equals("仟"))) {
						strBig = strNum + strDW + "零"
								+ strBig.substring(1, strBig.length());
						// 亿位不为零且万全为零不存在仟位时，去掉万
					} else if ((("" + strNow.charAt(0)).equals("万"))
							&& (("" + strNow.charAt(2)).equals("仟"))) {
						strBig = strNum + strDW
								+ strBig.substring(1, strBig.length());
						// 其他正常情况
					} else {
						if (("" + strBig.charAt(0)).equals("万")) {
							strBig = strNum + strDW
									+ strBig.substring(1, strBig.length())
									+ " ";
						} else {
							strBig = strNum + strDW + strBig;
						}
					}

				} // 拾亿－仟亿中一位为零且其前一位（亿以上）不为零的情况时补零

				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (("" + strNow.charAt(0)).equals("零"))
						&& (!("" + strNow.charAt(0)).equals("亿"))) {
					strBig = "零" + strBig;
					// 拾亿－仟亿中一位为零且其前一位（亿以上）也为零的情况时跨过

				} else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (("" + strNow.charAt(0)).equals("亿"))) {
				}
				// 拾亿－仟亿中一位为零且其前一位为亿位且为零的情况时跨过

				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) == '0')
						&& (("" + strNow.charAt(0)).equals("零"))) {
				}
				// 亿位为零且不存在仟万位和十亿以上时去掉上次写入的零

				else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) != '0')
						&& (("" + strNow.charAt(0)).equals("零"))
						&& (("" + strNow.charAt(1)).equals("亿"))
						&& (!("" + strNow.charAt(3)).equals("仟"))) {
					strBig = strNum + strDW
							+ strBig.substring(1, strBig.length());
					// 亿位为零且存在仟万位和十亿以上时，在亿仟万间补零
				} else if ((i < 15) && (i > 11)
						&& (strFen.charAt(lenIntFen - 1) != '0')
						&& (("" + strNow.charAt(0)).equals("零"))
						&& (("" + strNow.charAt(1)).equals("亿"))
						&& (("" + strNow.charAt(3)).equals("仟"))) {
					strBig = strNum + strDW + "亿零"
							+ strBig.substring(2, strBig.length());
				} else {
					strBig = strNum + strDW + strBig;
				}
				strFen = strFen.substring(0, lenIntFen - 1);
				lenIntFen--;
			}
			// return strBig;
			// 正、负数判断标记:如果为负，返回的时候加上“负”字符。 by Lichuanxin
			// start
			// ==========================================================================
			if ((flag.substring(0, 1).equals("-")))
				return isoToUTF8("负" + strBig);
			else
				return isoToUTF8(strBig);
			// end===============================================================================

		} catch (Exception e) {
			return "";
		}
	}

	// add by kfr for date
	public static String changeToBigdate(String value) {

		if (null == value || "".equals(value.trim())) {
			return " ";
		}

		String strDW, strNum, strNum1;

		strDW = "";
		strNum = "";
		strNum1 = "";

		for (int i = 0; i < value.length(); i++) {
			if (value.length() == 2) {
				if (i == 0) {
					switch (value.substring(0, 1).charAt(i)) {
					case '1':
						strNum = "十";
						break;
					case '2':
						strNum = "二十";
						break;
					case '3':
						strNum = "三十";
						break;
					case '0':
						strNum = " ";
						break;

					}
					switch (value.charAt(1)) { // 选择数字
					case '1':
						strNum1 = "一";
						break;
					case '2':
						strNum1 = "二";
						break;
					case '3':
						strNum1 = "三";
						break;
					case '4':
						strNum1 = "四";
						break;
					case '5':
						strNum1 = "五";
						break;
					case '6':
						strNum1 = "六";
						break;
					case '7':
						strNum1 = "七";
						break;
					case '8':
						strNum1 = "八";
						break;
					case '9':
						strNum1 = "九";
						break;
					case '0':
						strNum1 = " ";
						break;
					}

					strDW = strNum + strNum1;
				}
			}

			else {
				if (i == 0 && value.startsWith("0")) {
					strNum = "";
				} else {
					switch (value.charAt(i)) { // 选择数字
					case '1':
						strNum = "一";
						break;
					case '2':
						strNum = "二";
						break;
					case '3':
						strNum = "三";
						break;
					case '4':
						strNum = "四";
						break;
					case '5':
						strNum = "五";
						break;
					case '6':
						strNum = "六";
						break;
					case '7':
						strNum = "七";
						break;
					case '8':
						strNum = "八";
						break;
					case '9':
						strNum = "九";
						break;
					case '0':
						strNum = "零";
						break;
					}
				}
				strDW = strDW + strNum;
			}
		}

		return isoToUTF8(strDW);
	}

	/**
	 * 
	 * convertDbColToMapKey <br>
	 * 根据数据库的列字段名将其按BO定义格式转换为BO中属性定义
	 * 
	 * @param dbCol
	 *            数据库的字段
	 * @return String BO定义格式属性
	 */
	public static String convertDbColToMapKey(String dbCol) {
		String mapKey = "";
		String str = dbCol.toLowerCase();
		String[] ss = str.split("_");
		StringBuffer key = new StringBuffer();
		if (ss.length > 1) {
			key.append(ss[0]);
			for (int j = 1; j < ss.length; j++) {
				key.append(ss[j].substring(0, 1).toUpperCase()).append(
						ss[j].substring(1));
			}
		} else {
			key.append(str);
		}
		mapKey = key.toString();
		return mapKey;
	}

	/**
	 * 转换字符串 为字符串转换为Integer、Double、Long等对象去空格，如果为null或者“”转换为“0”返回 trim4Number
	 * <br>
	 * 
	 * @param str
	 * @return String
	 */
	public static String trim4Number(String str) {
		str = stringTrim(str);
		if ("".equals(str)) {
			return "0";
		}
		return str;
	}

	public static String trimMid(String str) {
		StringBuffer sb = new StringBuffer("");
		if (null == str) {
			return sb.toString();
		}
		str = str.trim();
		String[] strArray = str.split("|");
		if (null != strArray && strArray.length > 0) {
			for (int i = 0; i < strArray.length; i++) {
				String temp = strArray[i];
				if (null != temp && !"".equals(temp.trim())) {
					sb.append(temp.trim());
				}
			}
		}

		return sb.toString();
	}

	public static Log getLog(Object object) {
		return LogFactory.getLog(object.getClass().getName());
	}

	
	public static String notNull(String strTemp) {
		if (strTemp == null || strTemp.equals("null")) {
			return "";
		} else {
			return strTemp.trim();
		}
	}

	public static String notNullWithSpace(String strTemp) {
		if (strTemp == null || strTemp.equals("null")) {
			return " ";
		} else {
			return strTemp.trim();
		}
	}

	public static String[] StringToArray(String args) {
		String str = args;
		StringTokenizer stn = new StringTokenizer(str, "|");
		String[] temp = new String[stn.countTokens()];
		int i = 0;
		while (stn.hasMoreTokens()) {
			temp[i] = stn.nextToken();
			i++;
		}
		return temp;
	}

	// 将字符串转换成int类型
	public static int getIntByStr(String str) {
		str = notNull(str);
		if (str.equals("")) {
			return 0;
		} else {
			return Integer.parseInt(str);
		}
	}

	// 将字符串转换成long类型
	public static long getLongByStr(String str) {
		str = notNull(str);
		if (str.equals("")) {
			return 0;
		} else {
			return Long.parseLong(str);
		}
	}

	// 将字符串转换成double类型
	public static double getDoubleByStr(String str) {
		str = notNull(str);
		if (str.equals("")) {
			return 0;
		} else {
			return Double.parseDouble(str);
		}
	}

	/**
	 * <p>
	 * Replaces all occurances of a String within another String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> reference passed to this method is a no-op.
	 * </p>
	 * 
	 * <pre>
	 *   StringUtils.replace(null, *, *)        = null
	 *   StringUtils.replace(&quot;&quot;, *, *)          = &quot;&quot;
	 *   StringUtils.replace(&quot;aba&quot;, null, null) = &quot;aba&quot;
	 *   StringUtils.replace(&quot;aba&quot;, null, null) = &quot;aba&quot;
	 *   StringUtils.replace(&quot;aba&quot;, &quot;a&quot;, null)  = &quot;aba&quot;
	 *   StringUtils.replace(&quot;aba&quot;, &quot;a&quot;, &quot;&quot;)    = &quot;aba&quot;
	 *   StringUtils.replace(&quot;aba&quot;, &quot;a&quot;, &quot;z&quot;)   = &quot;zbz&quot;
	 * </pre>
	 * 
	 * @see #replace(String text, String repl, String with, int max)
	 * @param text
	 *            text to search and replace in, may be null
	 * @param repl
	 *            the String to search for, may be null
	 * @param with
	 *            the String to replace with, may be null
	 * @return the text with any replacements processed, <code>null</code> if
	 *         null String input
	 */
	public static String replace(String text, String repl, String with) {
		return replace(text, repl, with, -1);
	}

	/**
	 * <p>
	 * Replaces a String with another String inside a larger String, for the
	 * first <code>max</code> values of the search String.
	 * </p>
	 * 
	 * <p>
	 * A <code>null</code> reference passed to this method is a no-op.
	 * </p>
	 * 
	 * <pre>
	 *   StringUtils.replace(null, *, *, *)         = null
	 *   StringUtils.replace(&quot;&quot;, *, *, *)           = &quot;&quot;
	 *   StringUtils.replace(&quot;abaa&quot;, null, null, 1) = &quot;abaa&quot;
	 *   StringUtils.replace(&quot;abaa&quot;, null, null, 1) = &quot;abaa&quot;
	 *   StringUtils.replace(&quot;abaa&quot;, &quot;a&quot;, null, 1)  = &quot;abaa&quot;
	 *   StringUtils.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;&quot;, 1)    = &quot;abaa&quot;
	 *   StringUtils.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, 0)   = &quot;abaa&quot;
	 *   StringUtils.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, 1)   = &quot;zbaa&quot;
	 *   StringUtils.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, 2)   = &quot;zbza&quot;
	 *   StringUtils.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, -1)  = &quot;zbzz&quot;
	 * </pre>
	 * 
	 * @param text
	 *            text to search and replace in, may be null
	 * @param repl
	 *            the String to search for, may be null
	 * @param with
	 *            the String to replace with, may be null
	 * @param max
	 *            maximum number of values to replace, or <code>-1</code> if
	 *            no maximum
	 * @return the text with any replacements processed, <code>null</code> if
	 *         null String input
	 */
	public static String replace(String text, String repl, String with, int max) {
		if (text == null || repl == null || with == null || repl.length() == 0
				|| max == 0) {
			return text;
		}

		StringBuffer buf = new StringBuffer(text.length());
		int start = 0, end = 0;
		while ((end = text.indexOf(repl, start)) != -1) {
			buf.append(text.substring(start, end)).append(with);
			start = end + repl.length();

			if (--max == 0) {
				break;
			}
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	public static int getInt(String strTemp) {
		strTemp = notNull(strTemp);
		if (strTemp.equals(""))
			return 0;
		try {
			return (int) Math.floor(Double.parseDouble(strTemp));
		} catch (Exception e) {
			return 0;
		}
	}

	public static long getLong(String strTemp) {
		strTemp = notNull(strTemp);
		if (strTemp.equals(""))
			return 0L;
		try {
			return (long) Math.floor(Double.parseDouble(strTemp));
		} catch (Exception e) {
			return 0L;
		}
	}

	public static float getFloat(String strTemp) {
		strTemp = notNull(strTemp);
		if (strTemp.equals(""))
			return 0f;
		try {
			return Float.parseFloat(strTemp);
		} catch (Exception e) {
			return 0f;
		}
	}

	/**
	 * 返回字符串的长度，汉字按两个字符长度计算
	 * 
	 * @param s
	 *            需要计算的字符串
	 */
	public static int getLength(String s) {
		int len = 0;
		String strchar = "";
		for (int i = 0; i < s.length(); i++) {
			strchar = String.valueOf(s.charAt(i));
			if (strchar.length() == strchar.getBytes().length)
				len++;
			else
				len += 2;
		}
		return len;
	}

	// 是不是全英文
	public static boolean isEnglish(String str) {
		if (str == null) {
			return true;
		} else {
			if (str.length() == getLength(str)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 判断字符串是否是数字
	 */
	public static boolean isNumeric(String str, int intType) {
		try {
			switch (intType) {
			case NUMERIC_SHORT:
				Short.parseShort(str);
				break;
			case NUMERIC_INT:
				Integer.parseInt(str);
				break;
			case NUMERIC_LONG:
				Long.parseLong(str);
				break;
			case NUMERIC_FLOAT:
				Float.parseFloat(str);
				break;
			case NUMERIC_DOUBLE:
				Double.parseDouble(str);
				break;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否是手机
	 */
	public static boolean isMobile(String mobile) {
		if (notNull(mobile).equals("") || !isNumeric(mobile, NUMERIC_LONG))
			return false;
		if (mobile.length() != 11)
			return false;
		if (getInt(mobile) <= 0)
			return false;
		int intPre = getInt(mobile.substring(0, 3));
		if (intPre < 130 || intPre > 159)
			return false;
		return true;
	}

	/**
	 * 判断是否是email地址
	 */
	public static boolean isEmail(String email) {
		if (notNull(email).equals(""))
			return false;
		if (email.indexOf("@") <= 0)
			return false;
		int len = email.length();
		if (len <= 3)
			return false;
		String name = email.substring(0, email.indexOf("@"));
		if (name.length() <= 1)
			return false;
		String hostname = email.substring(email.indexOf("@") + 1);
		if (hostname.indexOf("@") >= 0)
			return false;
		if (hostname.indexOf(".") <= 0)
			return false;
		if (hostname.indexOf(".") > (hostname.length() - 3))
			return false;
		String domainname = hostname.substring(hostname.lastIndexOf(".") + 1);
		if (domainname.length() < 2)
			return false;
		if (isNumeric(domainname, NUMERIC_INT))
			return false;
		return true;
	}

	/**
	 * 处理 parseStringToArrayList 的方法，按分隔符号读出字符串的内容
	 * 
	 * @param strlist
	 *            含有分隔符号的字符串 ken 分隔符号
	 * @author mideowang
	 * @return 列表
	 * @exception BusinessException
	 * @version 2006-07-19
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList parseStringToArrayList(String strlist,
			String ken) {
		StringTokenizer st = new StringTokenizer(strlist, ken);

		if (strlist == null || strlist.equals("") || st.countTokens() <= 0) {
			return new ArrayList();
		}

		int size = st.countTokens();
		ArrayList strv = new ArrayList();

		for (int i = 0; i < size; i++) {
			String nextstr = st.nextToken();
			if (!nextstr.equals("")) {
				strv.add(nextstr);
			}
		}
		return strv;
	}

	/**
	 * 处理 removeZero 的方法，移除前置零
	 * 
	 * @param _cellValue
	 * @author mideowang
	 * @return String
	 * @exception BusinessException
	 * @version 2006-07-19
	 */

	@SuppressWarnings("unused")
	private static String removeZero(String _cellValue) {
		String cellValue = _cellValue;
		if (cellValue.startsWith("0")) {
			cellValue = cellValue.substring(1);
			return cellValue = removeZero(cellValue);
		} else
			return cellValue;
	}

	public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	public final static String DEFAULT_TIME_PATTERN = "hh:mm:ss";

	public final static String DEFAULT_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 将字符串转化为小写字串
	 * 
	 * @param formatstr
	 * @return
	 */
	public static String toFormat(String formatstr) {
		if (formatstr == null) {
			return "";
		}
		return formatstr.toLowerCase();
	}

	/**
	 * 将字串转化为标准的包名
	 * 
	 * @param formatstr
	 * @return
	 */
	public static String toFormatPackage(String formatstr) {
		if (formatstr == null) {
			return "";
		}
		return formatstr.toLowerCase();
	}

	/**
	 * 将字串的首写字母大写,出现"_"的,去掉并且将之后的一个字母大写.
	 * 
	 * @param formatstr
	 * @return
	 */
	public static String toFormatMethodName(String formatstr) {
		if (formatstr == null) {
			return "";
		}
		String _Str = formatstr.substring(0, 1).toUpperCase()
				+ formatstr.substring(1);
		int _Pos = 0;
		while ((_Pos = _Str.indexOf("_")) > 0) {
			if (_Str.length() > _Pos + 1)
				_Str = _Str.substring(0, _Pos)
						+ _Str.substring(_Pos + 1, _Pos + 2).toUpperCase()
						+ _Str.substring(_Pos + 2);
			else
				_Str = _Str.substring(0, _Pos);
		}
		return _Str;
	}

	/**
	 * 将字串的首写字母大写
	 * 
	 * @param formatstr
	 * @return
	 */
	public static String toFormatName(String formatstr) {
		if (formatstr == null) {
			return "";
		}
		return formatstr.substring(0, 1).toUpperCase() + formatstr.substring(1);
	}

	/**
	 * 将字串的首写字母小写
	 * 
	 * @param formatstr
	 * @return
	 */
	public static String toFormatVariable(String formatstr) {
		if (formatstr == null) {
			return "";
		}
		return formatstr.substring(0, 1).toLowerCase() + formatstr.substring(1);
	}

	/**
	 * 将字符串按照规则进行分段，如将"2001-10-12"按照"-"划分，则分为"2001"、"10"和"12"三段
	 * 
	 * @param szSource
	 *            进行分段的字符串
	 * @param token
	 *            分隔符号
	 * @return 分段后的字符串数组
	 */
	public static final String[] SplitString(String szSource, String token) {
		if (szSource == null || token == null)
			return null;
		StringTokenizer st1 = new StringTokenizer(szSource, token);
		String d1[] = new String[st1.countTokens()];
		for (int x = 0; x < d1.length; x++)
			if (st1.hasMoreTokens())
				d1[x] = st1.nextToken().trim();
		return d1;
	}

	/**
	 * 用于character codes的转换(ISO8859-1--->GB2312)
	 */
	public static final String toCNString(String str) {
		if (str == null)
			return null;
		try {
			byte[] bt = str.getBytes("ISO8859-1");
			str = new String(bt, "GB2312");
		} catch (Exception ex) {
			return null;
		}
		return str;
	}

	public static String writeObject(Serializable obj) throws Exception {
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(o);
		out.writeObject(obj);
		out.flush();
		out.close();
		o.close();
		byte[] data = o.toByteArray();

		String str = "";
		for (int i = 0; i < data.length; i++) {
			if (i == 0)
				str += data[i];
			else
				str += "." + data[i];
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	public static Object readObject(String str) throws Exception {
		StringTokenizer st = new StringTokenizer(str, ".");
		ArrayList al = new ArrayList();
		while (st.hasMoreTokens()) {
			al.add(st.nextToken());
		}

		byte[] bt = null;
		if (!al.isEmpty()) {
			String[] s_bt = new String[al.size()];
			al.toArray(s_bt);
			bt = new byte[al.size()];
			for (int i = 0; i < bt.length; i++) {
				bt[i] = Byte.parseByte(s_bt[i]);
			}
		}
		ByteArrayInputStream i = new ByteArrayInputStream(bt);
		ObjectInputStream in = new ObjectInputStream(i);
		Object obj = in.readObject();
		in.close();
		i.close();
		return obj;
	}

	/**
	 * 取源字符串中，从pos开始，出现在tag之间的子字符串
	 * 
	 * @param source源字符串
	 * @param tag标识
	 * @param pos开始位置
	 * @return 子字符串
	 */
	public static String subString(String source, String tag, int pos) {

		int start = source.indexOf("<" + tag + ">", pos) + tag.length() + 2;
		int end = source.indexOf("</" + tag + ">", pos + tag.length() + 3);
		if (start == tag.length() + 1)
			return "";
		return source.substring(start, end);
	}

	/**
	 * 按tag从原字符串中提取子字符串，并按tag后的名字存放到hashmap中
	 * 
	 * @param source源字符串
	 * @param tag标识
	 * @return hashMap
	 */
	@SuppressWarnings("unchecked")
	public static HashMap split(String source, String tag) {
		int pos = 0;
		String temp = "";
		HashMap hash = new HashMap();
		while (!(temp = subString(source, tag, pos)).equals("")) {
			pos = source.indexOf(temp) + temp.length();
			int _pos = temp.indexOf("$");
			hash.put(temp.substring(0, _pos), temp.substring(_pos + 1));
		}
		return hash;
	}

	/**
	 * 数组转String. 比如:<br>
	 * 
	 * <pre>
	 * String str = ArrayToString(new String[] { &quot;a&quot;, &quot;b&quot; }, &quot;_&quot;, &quot;,&quot;);
	 * System.out.println(str); // &quot;_a, _b&quot;
	 * </pre>
	 * 
	 * @param objs
	 *            数组
	 * @param prefix
	 *            转换结果中每一项的前缀
	 * @param delim
	 *            转换结果中各项的分隔符
	 * @return
	 */
	public static String ArrayToString(Object[] objs, String prefix,
			String delim) {
		StringBuffer sb = new StringBuffer();

		int len = objs == null ? 0 : objs.length;

		if (len > 0) {
			sb.append(prefix).append(objs[0]);

			for (int i = 1; i < len; i++) {
				sb.append(delim).append(prefix).append(objs[i]);
			}

		}

		return sb.toString();
	}

	public String firstCharToUpperCase(String name) {
		return new StringBuffer().append(Character.toUpperCase(name.charAt(0)))
				.append(name.substring(1)).toString();
	}

	public String firstCharToLowerCase(String name) {
		return new StringBuffer().append(Character.toLowerCase(name.charAt(0)))
				.append(name.substring(1)).toString();
	}

	/**
	 * 判断是否 不是空字符串或者null
	 */
	public static boolean notBlankOrNullString(String obj) {
		if (obj == null || obj.trim().equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * 将字符串按字母的进制进位(大写字母):比如<br>
	 * 
	 * <pre>
	 * String str = String.convertByLetter(&quot;ZZZ&quot;, 2, -1);
	 * System.out.println(str); // &quot;AAAB&quot;
	 * </pre>
	 * 
	 */
	public static String convertByLetter(String letters) {
		return convertByLetter(letters, 1, -1);
	}

	public static String convertByLetter(String letters, int increament) {
		return convertByLetter(letters, increament, -1);
	}

	public static String convertByLetter(String letters, int increament, int pos) {
		int len = letters.length();
		int value;
		char letter;
		String strPlace;

		if (pos == -1)
			pos = len;
		if (pos == 0) {
			letters = "A" + letters;
			return letters;
		}
		letter = letters.charAt(pos - 1);
		value = letter + increament;
		strPlace = letters.substring(pos - 1, pos);
		if (value - 90 > 0)
			strPlace = strPlace.replace(letter, (char) (64 + (value - 90)));
		else
			strPlace = strPlace.replace(letter, (char) value);
		letters = letters.substring(0, pos - 1) + strPlace
				+ letters.substring(pos);
		if (value - 90 > 0) {
			letters = convertByLetter(letters, 1, pos - 1);
		}

		return letters;

		// return "aa";
	}

	/**
	 * 拷贝指定个数字符为一个字符串。
	 * 
	 * @param c
	 *            字符
	 * @param count
	 *            拷贝个数。
	 * @return 新字符串。如果count<1的话，返回空串。
	 * @author Lishan
	 */
	public static String copyValue(char c, int count) {
		String string = new String("");
		if (count < 1) {
			return "";
		}
		for (int i = 0; i < count; i++) {
			string += c;
		}
		return string;
	}

	/**
	 * 在指定的位子替换字符。
	 * 
	 * @param source
	 *            待替换的字符串。
	 * @param c
	 *            要替换的字符。
	 * @param pos
	 *            位子。
	 * @return 新的被替换过的字符串。如果pos<0或pos>source.length就返回原字符串。
	 * @author Lishan
	 */
	public static String replace(String source, char c, int pos) {
		String string;
		char[] sa = source.toCharArray();
		for (int i = 0; i < sa.length; i++) {
			if (i == pos) {
				sa[i] = c;
			}
		}
		string = new String(sa);
		return string;
	}

	public static Timestamp getTimestamp(String timestamp, String pattern) {
		Timestamp t = null;

		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);

		try {
			t = new Timestamp(sdf.parse(timestamp).getTime());
		} catch (Exception e) {
			// e.printStackTrace();
		}

		return t;
	}

	public static String formatDisplay(String fortmatContent) {

		if (fortmatContent == null || fortmatContent.equals(""))
			return "";
		else {
			fortmatContent = replace(fortmatContent, "\r\n", "<br>");
			fortmatContent = replace(fortmatContent, "\r", "<br>");
			fortmatContent = replace(fortmatContent, " ", "&nbsp;&nbsp;");
			fortmatContent = replace(fortmatContent, "\t", " ");

		}
		return fortmatContent;
	}

	/**
	 * 判断是否是int型
	 * 
	 * @param input
	 *            String
	 * @return boolean
	 */
	public static boolean isInt(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * 转换编码 ISO-8859-1到GB2312
	 * 
	 * @param text
	 * @return
	 */
	public static String ISO2GB(String text) {
		String result = "";
		try {
			result = new String(text.getBytes("ISO-8859-1"), "GB2312");
		} catch (UnsupportedEncodingException ex) {
			result = ex.toString();
		}
		return result;
	}

	/**
	 * 转换编码 utf-8到GB2312
	 * 
	 * @param text
	 * @return
	 */
	public static String utf82GB2312(String text) {
		String result = "";
		try {
			result = new String(text.getBytes("utf-8"), "GB2312");
		} catch (UnsupportedEncodingException ex) {
			result = ex.toString();
		}
		return result;
	}

	/**
	 * 转换编码 GB2312到ISO-8859-1
	 * 
	 * @param text
	 * @return
	 */
	public static String GB2ISO(String text) {
		String result = "";
		try {
			result = new String(text.getBytes("GB2312"), "ISO-8859-1");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * Utf8URL编码
	 * 
	 * @param s
	 * @return
	 */
	public static String Utf8URLencode(String text) {
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < text.length(); i++) {

			char c = text.charAt(i);
			if (c >= 0 && c <= 255) {
				result.append(c);
			} else {

				byte[] b = new byte[0];
				try {
					b = Character.toString(c).getBytes("UTF-8");
				} catch (Exception ex) {
				}

				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					result.append("%" + Integer.toHexString(k).toUpperCase());
				}

			}
		}

		return result.toString();
	}

	/**
	 * Utf8URL解码
	 * 
	 * @param text
	 * @return
	 */
	public static String Utf8URLdecode(String text) {
		String result = "";
		int p = 0;

		if (text != null && text.length() > 0) {
			text = text.toLowerCase();
			p = text.indexOf("%e");
			if (p == -1)
				return text;

			while (p != -1) {
				result += text.substring(0, p);
				text = text.substring(p, text.length());
				if (text == "" || text.length() < 9)
					return result;

				result += CodeToWord(text.substring(0, 9));
				text = text.substring(9, text.length());
				p = text.indexOf("%e");
			}

		}

		return result + text;
	}

	/**
	 * utf8URL编码转字符
	 * 
	 * @param text
	 * @return
	 */
	private static String CodeToWord(String text) {
		String result;

		if (Utf8codeCheck(text)) {
			byte[] code = new byte[3];
			code[0] = (byte) (Integer.parseInt(text.substring(1, 3), 16) - 256);
			code[1] = (byte) (Integer.parseInt(text.substring(4, 6), 16) - 256);
			code[2] = (byte) (Integer.parseInt(text.substring(7, 9), 16) - 256);
			try {
				result = new String(code, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				result = null;
			}
		} else {
			result = text;
		}

		return result;
	}

	/**
	 * 编码是否有效
	 * 
	 * @param text
	 * @return
	 */
	private static boolean Utf8codeCheck(String text) {
		String sign = "";
		if (text.startsWith("%e"))
			for (int i = 0, p = 0; p != -1; i++) {
				p = text.indexOf("%", p);
				if (p != -1)
					p++;
				sign += p;
			}
		return sign.equals("147-1");
	}

	/**
	 * 是否Utf8Url编码
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isUtf8Url(String text) {
		text = text.toLowerCase();
		int p = text.indexOf("%");
		if (p != -1 && text.length() - p > 9) {
			text = text.substring(p, p + 9);
		}
		return Utf8codeCheck(text);
	}

	public static boolean isEmailByRegex(String email) {
		String check = "^([a-z0-9A-Z]+[-|\\._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p = Pattern.compile(check);
		Matcher matcher = p.matcher(email);
		return matcher.matches();
	}

	public static boolean isPasswordByRegex(String password) {
		String check = "^[0-9]{6,20}$";
		Pattern p = Pattern.compile(check);
		Matcher matcher = p.matcher(password);
		return matcher.matches();
	}

	public static boolean isMobilebyRegex(String mobile) {
		String check = "^0?((13)|(15)|(18))\\d{9}$";
		Pattern p = Pattern.compile(check);
		Matcher matcher = p.matcher(mobile);
		return matcher.matches();
	}

	public static boolean isTelephoneByRegex(String telepone) {
		String check = "^(0\\d{2,4}\\-)?\\d{5,8}(\\-\\d{1,5})?$";
		Pattern p = Pattern.compile(check);
		Matcher matcher = p.matcher(telepone);
		return matcher.matches();
	}

	public static boolean isCertNoByRegex(String certno) {
		String check = "^[0-9]{15}([0-9]{2}[Xx0-9])?$";
		Pattern p = Pattern.compile(check);
		Matcher matcher = p.matcher(certno);
		return matcher.matches();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return str != null && str.trim().length() > 0;
	}

	/**
	 * 400 系统使用
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isUnsCardbyRegex(String input) {
		String check = "^\\d{19}$";
		Pattern p = Pattern.compile(check);
		Matcher matcher = p.matcher(input);
		return matcher.matches();
	}

	/**
	 * 汉字转换位汉语拼音，英文字符不变
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String chinese2PinYin(String chinese) {
		if (org.apache.commons.lang.StringUtils.isBlank(chinese)) {
			return "";
		}
		char[] nameChar = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		StringBuffer pinyinName = new StringBuffer();
		for (int i = 0; i < nameChar.length; i++) {
			// System.out.println(nameChar[i]+"nameChar[i]=="+(int)nameChar[i]+"=aa");
			if (nameChar[i] > 128 && nameChar[i] != 65288
					&& nameChar[i] != 65289 && nameChar[i] != 65292) {
				try {
					String name = PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat)[0];
					name = name.substring(0, 1).toUpperCase()
							+ name.substring(1);
					pinyinName.append(name);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName.append(nameChar[i]);
			}
		}
		return pinyinName.toString();
	}

	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToFirstSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat)[0].charAt(0);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}

	public static <T> Boolean isBlank(T[] types) {
		if (null != types && types.length > 0) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public static <T> Boolean isNotBlank(T[] types) {
		return !isBlank(types);
	}

	public static <T> Boolean isBlank(List<T> types) {
		if (null != types && types.size() > 0) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public static <T> Boolean isNotBlank(List<T> types) {
		return !isBlank(types);
	}

	public static int getRandom(int size) {
		Random rand = new Random();
		// System.out.println(rand.nextInt(size));
		return rand.nextInt(size);
	}
	
	/**
	 * 从src中去除以suffix为结尾的字条串
	 * @param src
	 * @param target
	 * @return
	 */
	public static String removeLast(String src,String remove){
		return org.apache.commons.lang.StringUtils.removeEnd(src, remove);
	 
	}
	/**
	 * Parse the given <code>localeString</code> into a {@link Locale}.
	 * <p>This is the inverse operation of {@link Locale#toString Locale's toString}.
	 * @param localeString the locale string, following <code>Locale's</code>
	 * <code>toString()</code> format ("en", "en_UK", etc);
	 * also accepts spaces as separators, as an alternative to underscores
	 * @return a corresponding <code>Locale</code> instance
	 */
	public static Locale parseLocaleString(String localeString) {
		String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
		String language = (parts.length > 0 ? parts[0] : "");
		String country = (parts.length > 1 ? parts[1] : "");
		String variant = "";
		if (parts.length >= 2) {
			// There is definitely a variant, and it is everything after the country
			// code sans the separator between the country code and the variant.
			int endIndexOfCountryCode = localeString.indexOf(country) + country.length();
			// Strip off any leading '_' and whitespace, what's left is the variant.
			variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
			if (variant.startsWith("_")) {
				variant = trimLeadingCharacter(variant, '_');
			}
		}
		return (language.length() > 0 ? new Locale(language, country, variant) : null);
	}
	
	/**
	 * Trim all occurences of the supplied leading character from the given String.
	 * @param str the String to check
	 * @param leadingCharacter the leading character to be trimmed
	 * @return the trimmed String
	 */
	public static String trimLeadingCharacter(String str, char leadingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && buf.charAt(0) == leadingCharacter) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}
	
	/**
	 * Check that the given String is neither <code>null</code> nor of length 0.
	 * Note: Will return <code>true</code> for a String that purely consists of whitespace.
	 * @param str the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not null and has length
	 * @see #hasLength(CharSequence)
	 */
	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}
	
	/**
	 * Check that the given CharSequence is neither <code>null</code> nor of length 0.
	 * Note: Will return <code>true</code> for a CharSequence that purely consists of whitespace.
	 * <p><pre>
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 * @param str the CharSequence to check (may be <code>null</code>)
	 * @return <code>true</code> if the CharSequence is not null and has length
	 * @see #hasText(String)
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}
	
	/**
	 * Trim leading whitespace from the given String.
	 * @param str the String to check
	 * @return the trimmed String
	 * @see java.lang.Character#isWhitespace
	 */
	public static String trimLeadingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}
	
	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * Trims tokens and omits empty tokens.
	 * <p>The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * @param str the String to tokenize
	 * @param delimiters the delimiter characters, assembled as String
	 * (each of those characters is individually considered as delimiter).
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see java.lang.String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}
	
	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * <p>The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 * @param str the String to tokenize
	 * @param delimiters the delimiter characters, assembled as String
	 * (each of those characters is individually considered as delimiter)
	 * @param trimTokens trim the tokens via String's <code>trim</code>
	 * @param ignoreEmptyTokens omit empty tokens from the result array
	 * (only applies to tokens that are empty after trimming; StringTokenizer
	 * will not consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens (<code>null</code> if the input String
	 * was <code>null</code>)
	 * @see java.util.StringTokenizer
	 * @see java.lang.String#trim()
	 * @see #delimitedListToStringArray
	 */
	@SuppressWarnings("unchecked")
	public static String[] tokenizeToStringArray(
			String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List tokens = new ArrayList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}
	
	/**
	 * Copy the given Collection into a String array.
	 * The Collection must contain String elements only.
	 * @param collection the Collection to copy
	 * @return the String array (<code>null</code> if the passed-in
	 * Collection was <code>null</code>)
	 */
	@SuppressWarnings({ "unchecked" })
	public static String[] toStringArray(Collection collection) {
		if (collection == null) {
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}

	public static void main(String[] args) {
		System.out.println(removeLast("余平春;;",";;"));
	}

}
