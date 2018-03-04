package com.inspect.util.common;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;


public class ConvertObjectUtils {

	static Map<String, Object> map = new HashMap();

	  public static Map<String, Object> getMap()
	  {
	    return map;
	  }

	  public static boolean isEmpty(Object object)
	  {
	    if (object == null)
	      return true;
	    if (object.equals(""))
	      return true;
	    return (object.equals("null"));
	  }

	  public static String decode(String strIn, String sourceCode, String targetCode)
	  {
	    String temp = code2code(strIn, sourceCode, targetCode);
	    return temp;
	  }

	  public static String StrToUTF(String strIn, String sourceCode, String targetCode)
	  {
	    strIn = "";
	    try
	    {
	      strIn = new String(strIn.getBytes("ISO-8859-1"), "GBK");
	    }
	    catch (UnsupportedEncodingException e)
	    {
	      e.printStackTrace();
	    }
	    return strIn;
	  }

	  private static String code2code(String strIn, String sourceCode, String targetCode)
	  {
	    String strOut = null;
	    if ((strIn == null) || (strIn.trim().equals("")))
	      return strIn;
	    try
	    {
	      byte[] b = strIn.getBytes(sourceCode);
	      for (int i = 0; i < b.length; ++i)
	    //    System.out.print(b[i] + "  ");
	      strOut = new String(b, targetCode);
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      return null;
	    }
	    return strOut;
	  }

	  public static int getInt(String s, int defval)
	  {
	    if ((s == null) || (s == ""))
	      return defval;
	    try
	    {
	      return Integer.parseInt(s);
	    }
	    catch (NumberFormatException e)
	    {
	      return defval;
	    }
	  }

	  public static int getInt(String s)
	  {
	    if ((s == null) || (s == ""))
	      return 0;
	    try
	    {
	      return Integer.parseInt(s);
	    }
	    catch (NumberFormatException e)
	    {
	      return 0;
	    }
	  }

	  public static int getInt(String s, Integer df)
	  {
	    if ((s == null) || (s == ""))
	      return df.intValue();
	    try
	    {
	      return Integer.parseInt(s);
	    }
	    catch (NumberFormatException e)
	    {
	      return 0;
	    }
	  }

	  public static Integer[] getInts(String[] s)
	  {
	    Integer[] integer = new Integer[s.length];
	    if (s == null)
	      return null;
	    for (int i = 0; i < s.length; ++i)
	      integer[i] = Integer.valueOf(Integer.parseInt(s[i]));
	    return integer;
	  }

	  public static double getDouble(String s, double defval)
	  {
	    if ((s == null) || (s == ""))
	      return defval;
	    try
	    {
	      return Double.parseDouble(s);
	    }
	    catch (NumberFormatException e)
	    {
	      return defval;
	    }
	  }

	  public static double getDou(Double s, double defval)
	  {
	    if (s == null)
	      return defval;
	    return s.doubleValue();
	  }

	  public static Short getShort(String s)
	  {
	    if (StringUtils.isNotEmpty(s))
	      return Short.valueOf(Short.parseShort(s));
	    return null;
	  }

	  public static int getInt(Object object, int defval)
	  {
	    if (isEmpty(object))
	      return defval;
	    try
	    {
	      return Integer.parseInt(object.toString());
	    }
	    catch (NumberFormatException e)
	    {
	      return defval;
	    }
	  }

	  public static int getInt(BigDecimal s, int defval)
	  {
	    if (s == null)
	      return defval;
	    return s.intValue();
	  }

	  public static Integer[] getIntegerArry(String[] object)
	  {
	    int len = object.length;
	    Integer[] result = new Integer[len];
	    try
	    {
	      for (int i = 0; i < len; ++i)
	        result[i] = new Integer(object[i].trim());
	      return result;
	    }
	    catch (NumberFormatException e)
	    {
	      return null;
	    }
	  }

	  public static String getString(String s)
	  {
	    return getString(s, "");
	  }

	  public static String getString(Object object)
	  {
	    if (isEmpty(object))
	      return "";
	    return object.toString().trim();
	  }

	  public static String getString(int i)
	  {
	    return String.valueOf(i);
	  }

	  public static String getString(float i)
	  {
	    return String.valueOf(i);
	  }

	  public static String getString(String s, String defval)
	  {
	    if (isEmpty(s))
	      return defval;
	    return s.trim();
	  }

	  public static String getString(Object s, String defval)
	  {
	    if (isEmpty(s))
	      return defval;
	    return s.toString().trim();
	  }

	  public static long stringToLong(String str)
	  {
	    Long test = new Long(0L);
	    try
	    {
	      test = Long.valueOf(str);
	    }
	    catch (Exception localException)
	    {
	    }
	    return test.longValue();
	  }

	  public static String getIp()
	  {
	    String ip = null;
	    try
	    {
	      InetAddress address = InetAddress.getLocalHost();
	      ip = address.getHostAddress();
	    }
	    catch (UnknownHostException e)
	    {
	      e.printStackTrace();
	    }
	    return ip;
	  }

	  @SuppressWarnings("unused")
	private static boolean isBaseDataType(Class clazz)
	    throws Exception
	  {
	    return ((clazz.equals(String.class)) || (clazz.equals(Integer.class)) || (clazz.equals(Byte.class)) || (clazz.equals(Long.class)) || (clazz.equals(Double.class)) || (clazz.equals(Float.class)) || (clazz.equals(Character.class)) || (clazz.equals(Short.class)) || (clazz.equals(BigDecimal.class)) || (clazz.equals(BigInteger.class)) || (clazz.equals(Boolean.class)) || (clazz.equals(Date.class)) || (clazz.isPrimitive()));
	  }

	  public static String getIpAddrByRequest(HttpServletRequest request)
	  {
	    String ip = request.getHeader("x-forwarded-for");
	    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip)))
	      ip = request.getHeader("Proxy-Client-IP");
	    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip)))
	      ip = request.getHeader("WL-Proxy-Client-IP");
	    if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip)))
	      ip = request.getRemoteAddr();
	    return ip;
	  }

	  public static String getRealIp()
	    throws SocketException
	  {
	    String localip = null;
	    String netip = null;
	    Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
	    InetAddress ip = null;
	    boolean finded = false;
	    while ((netInterfaces.hasMoreElements()) && (!(finded)))
	    {
	      NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
	      Enumeration address = ni.getInetAddresses();
	      while (address.hasMoreElements())
	      {
	        ip = (InetAddress)address.nextElement();
	        if ((!(ip.isSiteLocalAddress())) && (!(ip.isLoopbackAddress())) && (ip.getHostAddress().indexOf(":") == -1))
	        {
	          netip = ip.getHostAddress();
	          finded = true;
	          break;
	        }
	        if ((ip.isSiteLocalAddress()) && (!(ip.isLoopbackAddress())) && (ip.getHostAddress().indexOf(":") == -1))
	          localip = ip.getHostAddress();
	      }
	    }
	    if ((netip != null) && (!("".equals(netip))))
	      return netip;
	    return localip;
	  }

	  public static String replaceBlank(String str)
	  {
	    String dest = "";
	    if (str != null)
	    {
	      Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	      Matcher m = p.matcher(str);
	      dest = m.replaceAll("");
	    }
	    return dest;
	  }

	  public static boolean isIn(String substring, String[] source)
	  {
	    if ((source == null) || (source.length == 0))
	      return false;
	    for (int i = 0; i < source.length; ++i)
	    {
	      String aSource = source[i];
	      if (aSource.equals(substring))
	        return true;
	    }
	    return false;
	  }

	  public static Map<Object, Object> getHashMap()
	  {
	    return new HashMap();
	  }

	  public static Map<Object, Object> SetToMap(Set<Object> setobj)
	  {
	    Map map = getHashMap();
	    Iterator iterator = setobj.iterator();
	    while (iterator.hasNext())
	    {
	      Map.Entry entry = (Map.Entry)(Map.Entry)iterator.next();
	      map.put(entry.getKey().toString(), entry.getValue().toString().trim());
	    }
	    return map;
	  }

	  public static boolean isInnerIP(String ipAddress)
	  {
	    boolean isInnerIp = false;
	    long ipNum = getIpNum(ipAddress);
	    long aBegin = getIpNum("10.0.0.0");
	    long aEnd = getIpNum("10.255.255.255");
	    long bBegin = getIpNum("172.16.0.0");
	    long bEnd = getIpNum("172.31.255.255");
	    long cBegin = getIpNum("192.168.0.0");
	    long cEnd = getIpNum("192.168.255.255");
	    isInnerIp = (isInner(ipNum, aBegin, aEnd)) || (isInner(ipNum, bBegin, bEnd)) || (isInner(ipNum, cBegin, cEnd)) || (ipAddress.equals("127.0.0.1"));
	    return isInnerIp;
	  }

	  private static long getIpNum(String ipAddress)
	  {
	    String[] ip = ipAddress.split("\\.");
	    long a = Integer.parseInt(ip[0]);
	    long b = Integer.parseInt(ip[1]);
	    long c = Integer.parseInt(ip[2]);
	    long d = Integer.parseInt(ip[3]);
	    long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
	    return ipNum;
	  }

	  private static boolean isInner(long userIp, long end, long arg4)
	  {
	    return ((!(userIp > end)) && (!(userIp > end)));
	  }
	}