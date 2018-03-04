package com.test;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.util.excel.Eoip;

//import com.inspect.util.excel.*;
public class ExcelTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
 
		ExcelTest e  = new ExcelTest();
		//D:\xj\formstest
		String filepath ="D:\\xj\\formstest\\unicom.xlsx";
         //String filepath ="D:\\xj\\tieta.xls";
		 File f  =new File(filepath);
		 List<String[]> list = e.readexcle(f);
		System.out.println(list.size());
		
//		for(int i=0;i<list.size();i++){
//			for(int j=0;j<list.get(i).length;j++){
//				System.out.print(list.get(i)[j] + " ");
//			}
//				System.out.println();
//		}
		
		e.saveList(list);
	 
	}
public String saveList(List<String[]> db){
		
		long s=System.currentTimeMillis();
		
 	        String connectStr = "jdbc:mysql://localhost:3306/inspect";
	        String insert_sql = "insert ignore into unicominfo(operator ,license ,datasheet ,baseid ,basename ,administrativedivision ,address ,radius ,longitute ,latitude ,cityx ,cityy ,high ,startdate ,recognitionbookid ,network1 ,network1sectorid ,network1sectornum ,network1angle , network1identificationcode ,network1receiveangle ,network1sendangle ,network1loss ,network1sendfreq ,network1receivefreq ,network1equipmentmodel ,network1cmiitid ,network1equipmentfactory ,network1equipmentnum ,network1transmitpower ,network1powerunit ,network1antennatype ,network1antennamodel ,network1polarization ,network13db ,network1dbi ,network1dbiunit ,network1antennafactory ,network1antennahigh ,network1startdate ,network2 ,network2sectorid ,network2sectornum ,network2angle , network2identificationcode ,network2receiveangle ,network2sendangle ,network2loss ,network2sendfreq ,network2receivefreq ,network2equipmentmodel ,network2cmiitid ,network2equipmentfactory ,network2equipmentnum ,network2transmitpower ,network2powerunit ,network2antennatype ,network2antennamodel ,network2polarization ,network23db ,network2dbi ,network2dbiunit ,network2antennafactory ,network2antennahigh ,network2startdate ,network3 ,network3sectorid ,network3angle ,network3identificationcode ,network3loss ,network3sendfreq ,network3receivefreq ,network3equipmentmodel ,network3cmiitid ,network3equipmentfactory ,network3transmitpower ,network3powerunit ,network3antennatype ,network3antennamodel ,network3polarization ,network3dbi ,network3dbiunit ,network3antennafactory ,network3antennahigh ,network3startdate ,network4 ,network4sectorid ,network4sectornum ,network4angle ,network4identificationcode ,network4receiveangle ,network4sendangle ,network4loss ,network4sendfreq ,network4receivefreq ,network4equipmentmodel ,network4cmiitid ,network4equipmentfactory ,network4equipmentnum ,network4transmitpower ,network4powerunit ,network4antennatype ,network4antennamodel ,network4polarization ,network43db ,network4dbi ,network4dbiunit ,network4antennafactory ,network4antennahigh ,network4startdate ,printdate ,validity ,remark) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	        String charset = "utf-8";
	        Boolean debug = true;
	        String username = "root";
	        String password = "123";
	        Connection conn=null;
	        PreparedStatement prest=null;
		  try {  
		      Class.forName("com.mysql.jdbc.Driver");  
		       conn = DriverManager.getConnection(connectStr, username,password);
		       conn.setAutoCommit(false);  
		       prest = conn.prepareStatement(insert_sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);  
		      for(int x = 0; x < db.size(); x++){ 
		    	  
		    	  String[] info = new String[113];
					
		    	  for(int j=0;j<db.get(x).length;j++){
		    		     info[j] = db.get(x)[j];
						//System.out.print(list.get(i)[j] + " ");
					}
		    	  
 
		    	  prest.setString(1,info[0]);
		    	  prest.setString(2,info[1]);
		    	  prest.setString(3,info[2]);
		    	  prest.setString(4,info[3]);
		    	  prest.setString(5,info[4]);
		    	  prest.setString(6,info[5]);
		    	  prest.setString(7,info[6]);
		    	  prest.setString(8,info[7]);
		    	  prest.setString(9,info[8]);
		    	  prest.setString(10,info[9]);
		    	  prest.setString(11,info[10]);
		    	  prest.setString(12,info[11]);
		    	  prest.setString(13,info[12]);
		    	  prest.setString(14,info[13]);
		    	  prest.setString(15,info[14]);
		    	  prest.setString(16,info[15]);
		    	  prest.setString(17,info[16]);
		    	  prest.setString(18,info[17]);
		    	  prest.setString(19,info[18]);
		    	  prest.setString(20,info[19]);
		    	  prest.setString(21,info[20]);
		    	  prest.setString(22,info[21]);
		    	  prest.setString(23,info[22]);
		    	  prest.setString(24,info[23]);
		    	  prest.setString(25,info[24]);
		    	  prest.setString(26,info[25]);
		    	  prest.setString(27,info[26]);
		    	  prest.setString(28,info[27]);
		    	  prest.setString(29,info[28]);
		    	  prest.setString(30,info[29]);
		    	  prest.setString(31,info[30]);
		    	  prest.setString(32,info[31]);
		    	  prest.setString(33,info[32]);
		    	  prest.setString(34,info[33]);
		    	  prest.setString(35,info[34]);
		    	  prest.setString(36,info[35]);
		    	  prest.setString(37,info[36]);
		    	  prest.setString(38,info[37]);
		    	  prest.setString(39,info[38]);
		    	  prest.setString(40,info[39]);
		    	  prest.setString(41,info[40]);
		    	  prest.setString(42,info[41]);
		    	  prest.setString(43,info[42]);
		    	  prest.setString(44,info[43]);
		    	  prest.setString(45,info[44]);
		    	  prest.setString(46,info[45]);
		    	  prest.setString(47,info[46]);
		    	  prest.setString(48,info[47]);
		    	  prest.setString(49,info[48]);
		    	  prest.setString(50,info[49]);
		    	  prest.setString(51,info[50]);
		    	  prest.setString(52,info[51]);
		    	  prest.setString(53,info[52]);
		    	  prest.setString(54,info[53]);
		    	  prest.setString(55,info[54]);
		    	  prest.setString(56,info[55]);
		    	  prest.setString(57,info[56]);
		    	  prest.setString(58,info[57]);
		    	  prest.setString(59,info[58]);
		    	  prest.setString(60,info[59]);
		    	  prest.setString(61,info[60]);
		    	  prest.setString(62,info[61]);
		    	  prest.setString(63,info[62]);
		    	  prest.setString(64,info[63]);
		    	  prest.setString(65,info[64]);
		    	  prest.setString(66,info[65]);
		    	  prest.setString(67,info[66]);
		    	  prest.setString(68,info[67]);
		    	  prest.setString(69,info[68]);
		    	  prest.setString(70,info[69]);
		    	  prest.setString(71,info[70]);
		    	  prest.setString(72,info[71]);
		    	  prest.setString(73,info[72]);
		    	  prest.setString(74,info[73]);
		    	  prest.setString(75,info[74]);
		    	  prest.setString(76,info[75]);
		    	  prest.setString(77,info[76]);
		    	  prest.setString(78,info[77]);
		    	  prest.setString(79,info[78]);
		    	  prest.setString(80,info[79]);
		    	  prest.setString(81,info[80]);
		    	  prest.setString(82,info[81]);
		    	  prest.setString(83,info[82]);
		    	  prest.setString(84,info[83]);
		    	  prest.setString(85,info[84]);
		    	  prest.setString(86,info[85]);
		    	  prest.setString(87,info[86]);
		    	  prest.setString(88,info[87]);
		    	  prest.setString(89,info[88]);
		    	  prest.setString(90,info[89]);
		    	  prest.setString(91,info[90]);
		    	  prest.setString(92,info[91]);
		    	  prest.setString(93,info[92]);
		    	  prest.setString(94,info[93]);
		    	  prest.setString(95,info[94]);
		    	  prest.setString(96,info[95]);
		    	  prest.setString(97,info[96]);
		    	  prest.setString(98,info[97]);
		    	  prest.setString(99,info[98]);
		    	  prest.setString(100,info[99]);
		    	  prest.setString(101,info[100]);
		    	  prest.setString(102,info[101]);
		    	  prest.setString(103,info[102]);
		    	  prest.setString(104,info[103]);
		    	  prest.setString(105,info[104]);
		    	  prest.setString(106,info[105]);
		    	  prest.setString(107,info[106]);
		    	  prest.setString(108,info[107]);
		    	  prest.setString(109,info[108]);
		    	  prest.setString(110,info[109]);
		    	  prest.setString(111,info[110]);
		    	  prest.setString(112,info[111]);
		    	  prest.setString(113,info[112]);

				  
				  
		          prest.addBatch();  
		      }  
		      prest.executeBatch();  
		     // conn.commit(); 
		      //conn.close();  
		      
		} catch (SQLException ex) {  
			ex.printStackTrace();
		   //Logger.getLogger(MyLogger.class.getName()).log(Level.SEVERE, null, ex);  
		} catch (ClassNotFoundException ex) {  
		     //Logger.getLogger(MyLogger.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}finally{
			if(conn!=null){
		        try{    
		        	conn.commit(); 
		        	conn.close() ;    
		        }catch(SQLException e){    
		            e.printStackTrace() ;    
		        } 
			}
			if(prest!=null){
			  try{    
				  prest.close() ;    
		        }catch(SQLException e){    
		            e.printStackTrace() ;    
		        } 
			}
			
		}
		
		long e=System.currentTimeMillis();

    	System.out.println("timespan: " + (e-s) +" 毫秒");
		return "" + db.size();
	}
	
	public List<String[]> readexcle(File file) throws Exception {
		int maxcolnum = 0;

		Workbook wbs = null;

		try {
			wbs = WorkbookFactory.create(new FileInputStream(file));// .create(inp);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Sheet st = wbs.getSheetAt(0);  //读取第几页
		int rnum = 2;     //从第几行开始读

		List<String[]> list = new ArrayList<String[]>();

		int totalExcelColumns = st.getLastRowNum();

		for (int j = rnum; j <= st.getLastRowNum(); j++) {
			Row row = st.getRow(j);
			if (row == null)
				continue;
			int iCellNum = row.getLastCellNum();

			// 如果erule没有设置最大列值，则在此得到该表格的最大列值
			//if (erule.getMaxcolnum() <= 0 && iCellNum > maxcolnum)
				maxcolnum = iCellNum;

			String[] srow = new String[iCellNum];
			for (int i = 0; i < iCellNum; i++) {
				Cell cell = row.getCell(i);
				srow[i] = getcellvalue(cell);
			}
			list.add(srow);
		}

//		if (erule.getMaxcolnum() <= 0)
//			erule.setMaxcolnum(maxcolnum);

		int totalExcelDataLines = list.size();

		return list;

	}
	
	// 将excel里面的格式转换为java里面的格式
	public String getcellvalue(Cell cell) {
		String value = null;
		// 简单的查检列类型
		if (cell == null)
			return value;

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:// 字符串
			value = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:// 数字
			// 判断是否是日期型数据
			if (DateUtil.isCellDateFormatted(cell)) {
				Date dTmp = cell.getDateCellValue();
				// 将成日期类型转化字符串
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				value = new String(df.format(dTmp));

			} else {

				CellStyle style = cell.getCellStyle();
				int idd = style.getDataFormat();

				// 2007年07月14日===189;2007年7月14日====185;2007-06-02====188
				if (idd == 185 || idd == 189 || idd == 188) {
					Date dTmp = cell.getDateCellValue();
					// 将成日期类型转化字符串
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					value = new String(df.format(dTmp));

				} else {
					Double d = new Double(cell.getNumericCellValue());
					value = new String(NumberFormat.getInstance().format(d).replace(",", ""));
				}
			}
			break;
		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;
		case Cell.CELL_TYPE_FORMULA:
			value = String.valueOf(cell.getCellFormula());
			break;
		case Cell.CELL_TYPE_BOOLEAN:// boolean型值
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			value = String.valueOf(cell.getErrorCellValue());
			break;
		default:
			break;
		}
		return value;

	}


}
