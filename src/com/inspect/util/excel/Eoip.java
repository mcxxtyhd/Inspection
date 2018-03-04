package com.inspect.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Eoip {
	private Eoiprule erule;
	// for import from excel
	private Map<String, Integer> d2emap;
	private Map<Integer, String> validatemap;

	// for export to excel
	private Map<String, String> expd2emap = new HashMap<String, String>();

	private Integer totalExcelColumns = 0;// 总行数
	private Integer totalExcelDataLines = 0;// 不是空行的数目
	private Integer totalObjectCount = 0;// 有效数据行数（这个跟保存的条数不同，因为还有重复条数）

	public Integer getTotalExcelColumns() {
		return totalExcelColumns;
	}

	public void setTotalExcelColumns(Integer totalExcelColumns) {
		this.totalExcelColumns = totalExcelColumns;
	}

	public Integer getTotalExcelDataLines() {
		return totalExcelDataLines;
	}

	public void setTotalExcelDataLines(Integer totalExcelDataLines) {
		this.totalExcelDataLines = totalExcelDataLines;
	}

	public Integer getTotalObjectCount() {
		return totalObjectCount;
	}

	public void setTotalObjectCount(Integer totalObjectCount) {
		this.totalObjectCount = totalObjectCount;
	}

	public Eoiprule getErule() {
		return erule;
	}

	public void setErule(Eoiprule erule) {
		this.erule = erule;
	}

	/**
	 * 逐行读取excel文件，将内容放入list<String[]>中
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */

	public List<String[]> readexcle(File file) throws Exception {
		int maxcolnum = 0;

		Workbook wbs = null;

		try {
			wbs = WorkbookFactory.create(new FileInputStream(file));// .create(inp);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Sheet st = wbs.getSheetAt(erule.getSheetnumber());
		int rnum = erule.getRowcontentspos();

		List<String[]> list = new ArrayList<String[]>();

		totalExcelColumns = st.getLastRowNum();

		for (int j = rnum; j <= st.getLastRowNum(); j++) {
			Row row = st.getRow(j);
			if (row == null)
				continue;
			int iCellNum = row.getLastCellNum();

			// 如果erule没有设置最大列值，则在此得到该表格的最大列值
			if (erule.getMaxcolnum() <= 0 && iCellNum > maxcolnum)
				maxcolnum = iCellNum;

			String[] srow = new String[iCellNum];
			for (int i = 0; i < iCellNum; i++) {
				Cell cell = row.getCell(i);
				srow[i] = getcellvalue(cell);
			}
			list.add(srow);
		}

		if (erule.getMaxcolnum() <= 0)
			erule.setMaxcolnum(maxcolnum);

		totalExcelDataLines = list.size();

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

	/**
	 * 字段所在列的配置从字符串形式转换成map形式
	 * "bnumber,1;bcity,2;bregion,3;bname,4;baddress,5;btower,11" 正常的配置
	 * 
	 * @return
	 */
	public Map<String, Integer> getd2emap() {
		Map<String, Integer> d2emap = new HashMap<String, Integer>();
		String[] nc = erule.getNametocol().split(";");
		for (int i = 0; i < nc.length; i++) {
			String[] onenc = nc[i].split(",");
			d2emap.put(onenc[0].trim(), Integer.parseInt(onenc[1].trim()));
		}
		return d2emap;
	}

	/**
	 * 字段所在列的配置从字符串形式转换成map形式
	 * "1,NotNull;6,Exeption-Abandon;7,Exeption-Abandon;10,0" 异常的配置
	 * 
	 * @return
	 */
	public Map<Integer, String> getvalidatemap() {
		Map<Integer, String> d2emap = new HashMap<Integer, String>();
		if (erule.getValidaterule() == null)
			return null;
		String[] nc = erule.getValidaterule().split(";");
		for (int i = 0; i < nc.length; i++) {
			String[] onenc = nc[i].split(",");
			d2emap.put(Integer.parseInt(onenc[0].trim()), onenc[1].trim());
		}
		return d2emap;
	}

	/*
	 * 若包含所规定的的异常，则返回true
	 */
	public Boolean keyvalidate(String[] si, int column, String action) {

		if (validatemap == null) {
			return false;
		}

		for (int i : validatemap.keySet()) {
			String validatestr = validatemap.get(i);
			if (validatestr.equals("NotNull") && action.equals("NotNull")) {
				if (si[i] == null || si[i].isEmpty())
					return true;
			}
			if (validatestr.equals("Exeption") && action.equals("Exeption")) {

				return true;
			}

		}

		return false;
	}

	public List<Object> list2bean(Object o, List<String[]> source) throws Exception {

		List<Object> listbean = new ArrayList<Object>();
		// 通过配置文件获取需要读取的列，放入map中
		d2emap = getd2emap();
		// 通过配置文件获取需要异常处理的列，放入map中
		validatemap = getvalidatemap();

		Class<?> fromClass = o.getClass();
		// 逐行读取封装在 List<String[]> source 中excel表中的内容
		int index = 0;
		for (int i = 0; i < source.size(); i++) {
			String[] si = source.get(i);

			// 对excle的每行数据进行格式化，主要是对空字段进行new处理，未来还可能有其他的处理；
			// 事实证明这种做法不可取，模板可能会后超多的列，但实际用的其实只是其中的一小部分
			// 因此，这部分的空字段判断，放在了type类型判断里
			// formatline(si);

			if (keyvalidate(si, 0, "NotNull")) {
				continue;
			}

			Object dest = fromClass.newInstance();
			// 读取配置文件里面的内容 如bnumber,1;bcity,2;然后将对应的值赋给dest对象
			for (String sn : d2emap.keySet()) {
				// fromField.set(ints, fromField.get(from));
				int col = d2emap.get(sn);
				Field field = dest.getClass().getDeclaredField(sn);
				String type = field.getGenericType().toString();
				field.setAccessible(true);
				/*
				 * 根据不同的源数据类型判断 支持的类型可逐步完善 异常数据判断的逻辑有：--来自规则validaterule
				 * 如果没有配置“默认值”，则置为空值；对string:"";数值型：0；其他类型：待补充
				 * 该列如果配置了例外处理"Exeption"，直接抛出异常，退出 如果配置了"默认值"，则用规则中的值进行赋值替换；
				 */
				if (type.equals("class java.lang.String")) {
					try {
						field.set(dest, si[col]);// 将si[col]的值赋给dest对应字段中
					} catch (Exception e) {
						if (validatemap == null)

							field.set(dest, new String(""));
						else {

							String validatestr = validatemap.get(col);
							if (validatestr == null) {
								field.set(dest, new String(""));
								continue;
							}

							if (validatestr.equals("Exception-Exit")) {
								throw e;
							}
							if (validatestr.equals("Exception-Abandon")) {
								continue;
							}
							if (!validatestr.isEmpty()) {
								field.set(dest, validatemap.get(col));
							}
						}
					}
				}
				if (type.equals("class java.lang.Integer") || type.equals("int")) {
					try {
						field.set(dest, Integer.parseInt(si[col]));
					} catch (Exception e) {
						if (validatemap == null)
							field.set(dest, 0);
						else {
							String validatestr = validatemap.get(col);
							if (validatestr == null) {
								field.set(dest, 0);
								continue;
							}
							if (validatestr.equals("Exception-Exit")) {
								throw e;
							}
							if (validatestr.equals("Exception-Abandon")) {
								continue;
							}
							if (!validatestr.isEmpty()) {
								field.set(dest, Integer.parseInt(validatemap.get(col)));
							}
						}
					}
				}
				if (type.equals("class java.lang.Double") || type.equals("double")) {
					try {
						field.set(dest, Double.parseDouble(si[col]));
					} catch (Exception e) {
						if (validatemap == null)
							field.set(dest, 0);
						else {
							String validatestr = validatemap.get(col);
							if (validatestr == null) {
								field.set(dest, 0);
								continue;
							}
							if (validatestr.equals("Exception-Exit")) {
								throw e;
							}
							if (validatestr.equals("Exception-Abandon")) {
								continue;
							}
							if (!validatestr.isEmpty()) {
								field.set(dest, Double.parseDouble(validatemap.get(col)));
							}
						}
					}

				}
				if (type.equals("class java.util.Date")) {
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

					try {
						Date date = format.parse(si[col]);
						field.set(dest, date);
					} catch (Exception e) {
						if (validatemap == null)
							field.set(dest, 0);
						else {
							String validatestr = validatemap.get(col);
							if (validatestr == null) {
								field.set(dest, 0);
								continue;
							}
							if (validatestr.equals("Exception-Exit")) {
								throw e;
							}
							if (validatestr.equals("Exception-Abandon")) {
								continue;
							}
							if (!validatestr.isEmpty()) {

								field.set(dest, format.parse(validatemap.get(col)));
							}
						}
					}

				}

			}

			// should have duplicate judgement
			listbean.add(dest);
			// savedb(dest);
			/*
			 * index++; System.out.println(index);
			 */
		}

		totalObjectCount = listbean.size();

		return listbean;

	}

	/**
	 * excel文件导出成为entity list
	 * 
	 * @param o
	 *            entity对象的一个实例
	 * @param file
	 *            需要导入的excel文件句柄
	 * @return entity list
	 * @throws Exception
	 */
	public List<Object> excel2db(Object o, File file) throws Exception {
		// File testfile = new File("E:\\temp\\0610\\tt-2007.xls");
		System.out.println("Eoip.java422导入的文件路径： " + file.getAbsolutePath());
		
		List<String[]> list = readexcle(file);
		return list2bean(o, list);
	}

	// 将excel内容的列行所在位置配置文件放入map中
	public void getstr2strmap(String strrule, Map<String, String> strmap) {
		String[] nc = strrule.split(";");
		for (int i = 0; i < nc.length; i++) {
			String[] onenc = nc[i].split(",");
			strmap.put(onenc[0].trim(), onenc[1].trim());
		}

	}

	/**
	 * 用于巡检统计 数据表内容导出到excel文件中
	 * 
	 * @param list
	 *            需要导出的entity list
	 * @return 成功导出的文件名列表，绝对路径
	 * @throws IOException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	public List<String> db2excel(List<Object> list) throws IOException, SecurityException, IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {

		List<String> filelist = new ArrayList<String>();
		// 模板文件路径
		String strtemplate = erule.getExptemplatefile();
		// 将excel基础信息内容的列行所在位置配置文件放入expd2emap中
		getstr2strmap(erule.getExpd2e(), expd2emap);
		// 绝对模式，详情导出
		if (erule.getExpmode()) {
			try {
				filelist = absmode(list, strtemplate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 列模式（总表导出）
		else {
			filelist = colmode(list, strtemplate);
		}

		return filelist;
	}

	/**
	 * 用于导出统计 列模式excel文件导出
	 * 
	 * @param list
	 *            数据源
	 * @param strtemplate
	 *            导出文件模板路径
	 * @return 成功导出的文件名称列表
	 */
	@SuppressWarnings( { "deprecation", "finally" })
	public List<String> colmode(List<Object> list, String path) {
		List<String> listfile = new ArrayList<String>();

		try {
			int rowaccess = 50;// 内存中缓存记录行数
			/* keep 100 rowsin memory,exceeding rows will be flushed to disk */
			XSSFWorkbook wbTemplate = (XSSFWorkbook) WorkbookFactory.create(new FileInputStream(path));
			SXSSFWorkbook wbExport = new SXSSFWorkbook(wbTemplate, rowaccess);

			Sheet stExport = wbExport.getSheetAt(0);
			// Sheet stTemplate = wbTemplate.getSheetAt(0);
			int STARTROW = erule.getExpstartcol();

			// 直接从startrow开始会报错
			int i = STARTROW + 1 ;
			int rownum = wbTemplate.getSheetAt(0).getRow(STARTROW-1).getLastCellNum();
			// Expstartcol行为数据模板的首行；读取之，并保存其cell样式
			//这个列的最大值从excel中也无法直接读到，因此需从外面传入
			//int rownum = stExport.getRow(maxrow-1).getRowNum(); //erule.getMaxcolnum();

			long curr_time = System.currentTimeMillis();
			;
			/**
			 * copy template header to export file include the first data row,
			 * the data all is null
			 */
			/*
			 * for(int h=0;h < STARTROW;h++){ Row r = stExport.createRow(h); Row
			 * rt = stTemplate.getRow(h); // 设置cell类型； for (int k = 0; k <
			 * rt.getLastCellNum(); k++) { Cell ct = r.createCell(k);
			 * ct.setCellStyle(rt.getCell(k).getCellStyle());
			 * ct.setCellType(rt.getCell(k).getCellType()); //假设head部分都为string类型
			 * if(h < STARTROW )
			 * ct.setCellValue(rt.getCell(k).getStringCellValue()); } }
			 */

			for (Object ts : list) {
				long t1 = System.currentTimeMillis();

				Row row = stExport.createRow(i);
				i++;
				// 设置cell类型；
				for (int k = 0; k < rownum; k++) {
					Cell celltype = row.createCell(k);
					/*
					 * 样式会带来极高的内存消耗，必须取消！ CellStyle borderStyle =
					 * wbExport.createCellStyle() ; // 设置单元格边框样式
					 * borderStyle.setBorderBottom(CellStyle.BORDER_THIN);
					 * borderStyle.setBorderTop(CellStyle.BORDER_THIN);
					 * borderStyle.setBorderLeft(CellStyle.BORDER_THIN);
					 * borderStyle.setBorderRight(CellStyle.BORDER_THIN);
					 * borderStyle.setAlignment(CellStyle.ALIGN_CENTER);
					 * borderStyle
					 * .setVerticalAlignment(CellStyle.VERTICAL_CENTER);
					 * 
					 * celltype.setCellStyle(borderStyle); borderStyle = null;
					 */
				}

				long t2 = System.currentTimeMillis();

				// 将巡检项数据赋给excel表中
				// 集合类型的字段的处理 erule.getSetfieldvalue()为 String setfieldvalue =
				// "xproname,xvalue";
				// erule.getSetfieldmap()为巡检项所在位置
				dealwithsetmap(ts, null, row);

				long t3 = System.currentTimeMillis();

				// 将基础信息导入值excel表中
				// expd2emap为基础信息的所在位置
				oneO2E(ts, expd2emap, erule.getExpmode(), erule.getExpenumstr(), null, row, null, null);

				long t4 = System.currentTimeMillis();

				// System.out.println("t21 t32 t43 :" + (t2-t1)/1000 +" ;" +
				// (t3-t2)/1000 + " ;" +(t4-t3)/1000);
				if (i % rowaccess == 0) {

					((SXSSFSheet) stExport).flushRows();
					long now_time = System.currentTimeMillis();
					// System.out.println("耗时:"+(now_time-curr_time)/1000+
					// " ;i=" +i);
					curr_time = now_time;
				}

			}
			File file = new File(erule.getExpfilename());
			if (file.exists()) {
				file.delete();
			}
			// 准备输出流对象
			FileOutputStream fos = null;
			fos = new FileOutputStream(erule.getExpfilename());
			wbExport.write(fos);

			fos.close();

			wbTemplate = null;

			listfile.add(erule.getExpfilename());
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable t) {
			System.out.println(t);
		} finally {
			return listfile;
		}

	}

	/**
	 * 绝对模式导出
	 * 
	 * @param list
	 *            需要导出的数据源
	 * @param strtemplate
	 *            导出的文件模板；绝对路径
	 * @return 成功导出的文件名列表
	 * @throws IOException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvalidFormatException
	 */
	public List<String> absmode(List<Object> list, String strtemplate) throws IOException, SecurityException, NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException, InvalidFormatException {

		List<String> listfile = new ArrayList<String>();

		InputStream inStream = new FileInputStream(new File(strtemplate));
		// 读取模板表格
		Workbook wk = WorkbookFactory.create(inStream);
		Sheet sheet = wk.cloneSheet(0);

		Integer posfixno = 0;

		for (Object ts : list) {
			posfixno++;

			try {
				// 集合类型的字段的处理
				dealwithsetmap(ts, sheet, null);

				// 自身object数据处理
				oneO2E(ts, expd2emap, erule.getExpmode(), erule.getExpenumstr(), sheet, null, null, null); // erule.getExppostfixrule()为name,bname
				// 例如："name,bname"，表示用字段“bname”的值作为名称；
				String[] nc = erule.getExppostfixrule().split(",");
				String postfix = posfixno.toString();
				if (nc[0].equals("name")) {
					String excelposition = expd2emap.get(nc[1]);
					CellReference cr = new CellReference(excelposition);
					int col = cr.getCol();
					int row = cr.getRow();
					Cell cell = sheet.getRow(row).getCell(col);
					postfix = getcellvalue(cell);
				}
				if (nc[0].equals("increase")) {
					// todo

				}
				// sheetnum:已产生的excel文件数目；sheetpos：当前sheet在excel文件中的位置
				int sheetnum = posfixno / erule.getExpnumsheetperwb();
				int sheetpos = posfixno % erule.getExpnumsheetperwb();
				if (sheetpos == 0)
					sheetpos = erule.getExpnumsheetperwb();
				// 设置sheet的名称
				if ("".equals(postfix)) {
					postfix = "1";
				}
				wk.setSheetName(sheetpos, postfix);
				// 产生新的sheet
				sheet = wk.cloneSheet(0);
				// 还没写满，继续
				if (posfixno < list.size()) { // 若已经是最后一条，则需写文件退出
					if (erule.getExpnumsheetperwb() > 1 && posfixno % erule.getExpnumsheetperwb() > 0)
						continue;
				}
				// 第0个sheet数据为空，导出时应删除
				wk.removeSheetAt(0);
				if (posfixno < list.size()) {
					wk.removeSheetAt(erule.getExpnumsheetperwb());
				}

				else {

					if (erule.getExpnumsheetperwb() > 1) {
						wk.removeSheetAt(posfixno % erule.getExpnumsheetperwb());
					}

				}

				String filename = erule.getExpfilename() + (sheetnum - 1) * erule.getExpnumsheetperwb() + "_" + (posfixno - 1) + ".xls";
				if (posfixno == list.size() && list.size() % erule.getExpnumsheetperwb() > 0) {
					filename = erule.getExpfilename() + (sheetnum) * erule.getExpnumsheetperwb() + "_" + (posfixno - 1) + ".xls";
				}

				File file = new File(filename);
				if (file.exists()) {
					file.delete();
				}
				// 准备输出流对象
				FileOutputStream fos = new FileOutputStream(filename);
				wk.write(fos);
				inStream.close();
				inStream = new FileInputStream(new File(strtemplate));
				wk = null;
				// 读取模板表格
				wk = WorkbookFactory.create(inStream);
				sheet = wk.cloneSheet(0);
				fos.close();
				listfile.add(filename);
			} catch (Exception e) {
				System.out.println("====================" + e.getMessage());

			}
		}
		return listfile;

	}

	/*
	 * 一个对象 赋值到excel对应位置中
	 * 
	 * setfileldmap: 巡检项目名称列表，以,分隔，如：馈线,天线 setfieldvalue:映射的值字段，如xvalue ts
	 * 在单个对象中为TInspectItemReport对象
	 * ，在处理集合对象方法dealwithsetmap中为TInspectItemDetailReport o2emap
	 * 在单个对象中调用是为为excel表中基本信息的所在位置，在处理集合对象方法dealwithsetmap中调用时为{xproname=A0}
	 * mode 为导出类型（绝对或列） setfileldmap 为excel表中详细字段（各种巡检项）的所在位置 setfieldvalue 为
	 * String setfieldvalue = "xproname,xvalue";
	 */
	/*
	 * oneO2E(it.next(), name2e, mode, enumFmap, sheet, hssfrow, setfileldmap,
	 * setfieldvalue); oneO2E(it.next(), name2e, sheet, hssfrow);
	 * dealwithsetmap(ts, erule.getSetdemap(), erule.getExpmode(),
	 * erule.getExpenumstr(), null, row, erule.getSetfieldmap(),
	 * erule.getSetfieldvalue());
	 */

	public void oneO2E(Object ts, Map<String, String> o2emap, Boolean mode, Map<String, String> enumFmap, Sheet sheet, Row hssfrow, String setfileldmap,
			String setfieldvalue) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

		for (String sn : o2emap.keySet()) {
			String excelposition = o2emap.get(sn);
			if (excelposition == null)
				continue;

			CellReference cr = new CellReference(excelposition);
			int col = cr.getCol();
			int row = cr.getRow();

			String strvalue = getfieldvalue(ts, sn);
			// 枚举值替换的处理，先备份key值
			String keyforenum = strvalue;

			// 集合类型的处理：应判断列值是否等于该字段名称，如是，则将真正的直列中的数据取出，并赋值给excel
			if (setfileldmap != null) {

				Map<String, String> name2e = new HashMap<String, String>();
				getstr2strmap(setfileldmap, name2e);
				if (name2e.containsKey(strvalue)) {
					// 获取对应set对象的行列位置
					excelposition = name2e.get(strvalue);
					CellReference crset = new CellReference(excelposition);
					col = crset.getCol();
					row = crset.getRow();
					// 航标灯、避雷器工作是否正常,j4;天线抱杆（包括GPS抱杆）是否紧固牢固j5;铁塔警示牌（广告牌）是否固定紧固,j6;
					// 铁塔和平台无锈蚀铁丝、螺丝、螺栓等杂物,j7;铁塔避雷系统下引线连接是否可靠、是否锈蚀、丢失,j8;
					// 检查爬梯、平台、过桥是否牢固,j9;天线与抱杆固定是否牢固,q4;天线紧固件有无缺损、锈蚀,q5;天线、馈线外观有无损伤,q6;
					// 馈线与天线接口处密封是否良好,q7;馈线接地处密封是否良好,q8;平台处的馈线、接地线走线是否规范,q9;馈线窗处密封是否良好,q11;
					// 馈线卡子紧固情况是否良好,q13;馈线卡子有无损缺有无锈蚀,q14
					String strvaluefield = setfieldvalue.split(",")[1];
					strvalue = getfieldvalue(ts, strvaluefield);

				} else { // 如果配置了集合，但巡检项未找到，不做处理；
					continue;
				}

			}

			// 枚举类型的替换处理：
			Boolean enumflag = false;
			String enumvalue = null;
			if (enumFmap != null)
				// 对于set类型，应将字段对应的其值作为key进行查询；
				// 对于普通类型，将其字段名称作为key查询
				if (setfileldmap != null)
					enumvalue = enumFmap.get(keyforenum);
				else
					enumvalue = enumFmap.get(sn);

			Map<String, String> enumSmap = new HashMap<String, String>();
			if (enumvalue == null)
				;
			else {
				getstr2strmap(enumvalue, enumSmap);
				enumflag = true;
			}

			// 枚举类型值处理
			if (enumflag)
				strvalue = enumSmap.get(strvalue);

			// 两种模式处理
			if (mode) { // 绝对模式
				// 特殊字段的特殊处理
				if (erule.getSpecialFields() != null)
					specialFieldprocess(keyforenum, strvalue, excelposition, sheet);
				else
					sheet.getRow(row).getCell(col).setCellValue(strvalue);
			} else
				hssfrow.getCell(col).setCellValue(strvalue);

		}
	}

	public void specialFieldprocess(String fieldname, String strvalue, String excelposition, Sheet sheet) {
		Map<String, String> name2e = new HashMap<String, String>();
		getstr2strmap(erule.getSpecialFields(), name2e);

		CellReference crset = new CellReference(excelposition);
		int col = crset.getCol();
		int row = crset.getRow();

		if (name2e.containsKey(excelposition)) {

			if (excelposition.equals("AB14")) { // 铁塔垂直度调整前测量结果（指标1/1500）
				CellReference cr = new CellReference("Y14");
				int colt = cr.getCol();
				int rowt = cr.getRow();
				sheet.getRow(row).getCell(col).setCellValue(strvalue + " cm");

				String v = fieldname.substring(7);
				sheet.getRow(rowt).getCell(colt).setCellValue(v);

				return;
			}

			if (excelposition.equals("AB15")) { // 铁塔垂直度调整后测量结果（指标1/1500）
				CellReference cr = new CellReference("Y14");
				int colt = cr.getCol();
				int rowt = cr.getRow();
				sheet.getRow(row).getCell(col).setCellValue(strvalue + " cm");

				String v = fieldname.substring(7);
				sheet.getRow(rowt).getCell(colt).setCellValue(v);

				return;
			}

			if (excelposition.equals("AB10")) { // 测量铁塔接地电阻
				sheet.getRow(row).getCell(col).setCellValue("RE= " + strvalue);

				return;
			}

			if (excelposition.equals("AB16")) { // 塔高：
				sheet.getRow(row).getCell(col).setCellValue(strvalue + " m");

				return;
			}
			/*
			 * if(excelposition.equals("C6") || excelposition.equals("D11")){
			 * //塔高： sheet.getRow(row).getCell(col).setCellValue(strvalue +
			 * "成功");
			 * 
			 * return; }
			 */

		}

		// 其他情况，设置初始值；
		sheet.getRow(row).getCell(col).setCellValue(strvalue);

	}

	/*
	 * 目前，仅支持对List集合的处理 如果要扩充的话，增加相应的判断即可
	 */
	/**
	 * ts TInspectItemReport对象 setmapv
	 * 为（com.inspect.model.monitor.TInspectItemDetailReport
	 * ", "inspectreportdetailmsgs"); mode 为判断是否是绝对模式还是列模式 enumFmap 为枚举配置
	 * hssfrow为某行里面的row setfilelmap 为字段位置 setfieldvalue 为 String setfieldvalue =
	 * "xproname,xvalue";
	 */

	@SuppressWarnings("unchecked")
	public void dealwithsetmap(Object ts, Sheet sheet, Row hssfrow) throws SecurityException, IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException, NoSuchMethodException, InvocationTargetException {

		// Map<String,Object> setmapv = erule.getSetmap();
		if (erule.getSetdemap() != null && erule.getSetdemap().isEmpty() == false) {
			Iterator iter = erule.getSetdemap().entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				String val = (String) entry.getValue();
				String setobjectname = "java.util.List<" + key + ">";
				Field fs = ts.getClass().getDeclaredField(val);
				String type = fs.getGenericType().toString();

				List<Object> tssetobj = null;

				if (type.equals(setobjectname)) {
					// Method m =
					// ts.getClass().getDeclaredMethod("getInspectreportdetailmsgs");
					// tssetobj = (Set<Object> )m.invoke(ts);

					fs.setAccessible(true);
					// 将TInspectItemReport对象中List<TInspectItemDetailReport>集合赋给tssetobj
					tssetobj = (List<Object>) fs.get(ts);

					// 处理该set类型object的名称映射等规则数据,这里的excel位置无意义；
					// 注意：这里仅支持两个字段之间名称-值的映射；！！！
					Map<String, String> name2e = new HashMap<String, String>();
					String[] setstr = erule.getSetfieldvalue().split(",");
					name2e.put(setstr[0], "A0");

					Iterator<Object> it = tssetobj.iterator();
					while (it.hasNext()) {
						oneO2E(it.next(), name2e, erule.getExpmode(), erule.getExpenumstr(), sheet, hssfrow, erule.getSetfieldmap(), erule.getSetfieldvalue());
					}

				}
			}
		}

	}

	/**
	 * 通过字段sn，返回ts对象中将指定字段（sn）的值
	 * 
	 * @param ts
	 * @param sn
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String getfieldvalue(Object ts, String sn) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = ts.getClass().getDeclaredField(sn);
		String type = field.getGenericType().toString();
		field.setAccessible(true);

		/*
		 * 根据不同的源数据类型判断 支持的类型可逐步完善 异常数据判断的逻辑有：--来自规则validaterule
		 * 如果没有配置“默认值”，则置为空值；对string:"";数值型：0；其他类型：待补充
		 * 该列如果配置了例外处理"Exeption"，直接抛出异常，退出 如果配置了"默认值"，则用规则中的值进行赋值替换；
		 */
		// 集合类型的处理,未来还应对其他的集合类型进行处理
		// java.util.Set<com.inspect.model.monitor.TInspectItemDetailReport>
		String strvalue = null;
		if (type.startsWith("java.util.Set") || type.startsWith("java.util.List")) {
			return null;
			// 不做set类型的嵌套处理；
		}
		if (type.equals("class java.lang.String")) {
			// 返回ts对象上此 Field 表示的字段的值。
			String str = (String) field.get(ts);
			strvalue = str;

		}
		if (type.equals("class java.lang.Integer") || type.equals("int")) {

			Integer intcell = (Integer) field.get(ts);
			strvalue = intcell.toString();

		}
		if (type.equals("class java.lang.Double") || type.equals("double")) {
			Double doublecell = (Double) field.get(ts);
			strvalue = doublecell.toString();
		}
		/*
		 * 其他的java类型处理。。。。。。
		 */
		return strvalue;
	}

	private void getAllFields(Class class1, List<Field> fields) {
		try {
			Field[] field = class1.getDeclaredFields();
			for (int i = 0; i < field.length; i++) {
				if (field[i].getType().getName().contains("com.inspect")) {
					getAllFields(field[i].getType(), fields);
				} else if (field[i].getType().getName().equals("java.util.Set")) {
					Type fc = field[i].getGenericType(); // 关键的地方，如果是Set
					// List类型，得到其Generic的类型
					if (fc == null)
						continue;
					if (fc instanceof ParameterizedType) // 【3】如果是泛型参数的类型
					{
						ParameterizedType pt = (ParameterizedType) fc;
						Class genericClazz = (Class) pt.getActualTypeArguments()[0]; // 【4】
						getAllFields(genericClazz, fields);
					}
				}
				// System.out.println(field[i]);
				fields.add(field[i]);
				/*
				 * Field f = field[i];
				 * 
				 * String fieldName = f.getName(); String stringLetter =
				 * fieldName.substring(0, 1).toUpperCase();
				 * 
				 * // 获得相应属性的getXXX和setXXX方法名称 String setName = "set" +
				 * stringLetter + fieldName.substring(1);
				 * 
				 * // 获取相应的方法 Method setMethod = class1.getMethod(setName, new
				 * Class[] { f .getType() }); methods.add(setMethod);
				 */
			}
			/*
			 * Class clazz = class1.getSuperclass(); if
			 * (clazz.getName().contains("com.huawei")) { getAllFields(clazz); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
