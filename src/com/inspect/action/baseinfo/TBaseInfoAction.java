package com.inspect.action.baseinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.service.BaseInfoServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.util.excel.Eoip;
import com.inspect.util.excel.Eoiprule;
import com.inspect.vo.basis.TBaseInfoVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/baseInfo")
@Action(value="baseInfoAction",results={
		@Result(name="baseInfoList",location="/webpage/baseInfo/baseInfoList.jsp"),
		@Result(name="baseEquipmentList",location="/webpage/baseInfo/baseEquipmentList.jsp"),
		@Result(name="baseInfoAdd",location="/webpage/baseInfo/baseInfoAdd.jsp"),
		@Result(name="baseInfoExcel",location="/webpage/baseInfo/baseInfoExcel.jsp"),
		@Result(name="baseInfoEdit",location="/webpage/baseInfo/baseInfoEdit.jsp"),
		@Result(name="baseInfoView",location="/webpage/baseInfo/baseInfoView.jsp")})
public class TBaseInfoAction extends BaseAction implements ModelDriven<TBaseInfoVo> {
	private static final long serialVersionUID = 4082315459614276457L;
	
	private static final Logger logger = Logger.getLogger(TBaseInfoAction.class);
	private File files ;
	private File file ;
	private String str ;
	final SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
	@Resource
	private SystemServiceI systemService;
	
	private TBaseInfoVo baseInfoVo=new TBaseInfoVo();
	@Resource
	private BaseInfoServiceI baseInfoService;
	@Resource
	private BaseDaoI baseDao;
	@Override
	public TBaseInfoVo getModel() {
		return baseInfoVo;
	}
	
	public File getFiles() {
		return files;
	}

	public void setFiles(File files) {
		this.files = files;
	}

	public String baseInfoEdit(){
		String bid=getRequest().getParameter("BaseInfoId");
		getRequest().setAttribute("BaseInfoBean",baseInfoService.getEntityById(TBaseInfo.class,Integer.parseInt(bid)));
		return "baseInfoEdit";
	}
	public String baseInfoAdd() {
		return "baseInfoAdd";
	}
	public String baseInfoExcel() {
		String type=getRequest().getParameter("type");
		getRequest().setAttribute("type",type);
		return "baseInfoExcel";
	}
	public String baseInfoList(){
		/*addCookie("name123","value123");
		try {
			readShoppingCartFromCookie();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "baseInfoList";
	}
	public String baseEquipmentList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		return "baseEquipmentList";
	}
	
	public String baseInfoView(){
		return "baseInfoView";
	}
	
	public void baseInfoDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = baseInfoService.findBaseInfoDatagrid(baseInfoVo,page,rows,querySql());
//		System.out.println(map.get("total"));
//		getRequest().setAttribute("basesum",map.get("total"));
		writeJson(map);
	}
	
	private String getFileLastName(){
		String fileName =baseInfoVo.getExcelFileFileName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1); 
		return extension;
	}

	// 创建EXCEl工作簿
	private Workbook createWorkBook(InputStream is) throws Exception {
		if (getFileLastName().equalsIgnoreCase("xls")) {
			return new HSSFWorkbook(is);
		}
		if (getFileLastName().equalsIgnoreCase("xlsx")) {
			return new XSSFWorkbook(is);
		}
		return null;
	}
	
	@LogAnnotation(event="添加基础数据",tablename="t_base_info")
	public void addBaseInfo(){
		Json j=new Json();
		try{
			long cnumber=baseInfoService.isExistBaseInfo(baseInfoVo.getBnumber(),queryEnterpriseByWhere());
			if(cnumber>0){
				 j.setMsg("数据已存在,不能重复添加!");
			}else{
				baseInfoVo.setEntid(getSessionUserName().getEntid());
				baseInfoService.addBaseInfo(baseInfoVo);
				j.setMsg("添加成功！");
				j.setSuccess(true);
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="修改基础数据",tablename="t_base_info")
	public void editBaseInfo(){
		Json j=new Json();
		try {
		//	long cnumber=baseInfoService.isExistBaseInfo(baseInfoVo.getBnumber(),queryEnterpriseByWhere());
			baseInfoService.editBaseInfo(baseInfoVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
			
		} catch (Exception e) {
			setOperstatus(1);
			j.setMsg("修改失败");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除基础数据",tablename="t_base_info")
	public void removeBaseInfo(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(baseInfoVo.getIds())){
				baseInfoService.removeBaseInfo(baseInfoVo.getIds());
				j.setSuccess(true);
				j.setMsg("删除成功！");
			 }
			}	
		catch(Exception e){
			setOperstatus(1);
			j.setMsg("删除失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	/**
	 * 
	 * @param flag 1表示铁塔 2表示室内
	 */
	public void saveExecl(){
		
		System.out.println("saveExcel()获取文件名。");
		int flag=Integer.parseInt(getRequest().getParameter("flag"));
		str = ServletActionContext.getServletContext().getRealPath("/WEB-INF");
		file = new File( str ) ;
		if( !file.exists() ){
			file.mkdirs();
		}
		baseInfoVo.getExcelFile().renameTo( file );
		//file = new File( str.append(files.getName()).toString() ) ;
		//readExcel( str.append("\\").append( files.getName() ).toString() ) ;
		int remark[] = readExcel(baseInfoVo.getExcelFile(),flag) ;
		Json j=new Json();
		j.setMsg( remark[1] > 0&&remark[0]==remark[1] ? "规划数据成功导入" + remark[0] + "行数据" : "导入失败：导入文档格式不正确，请选择正确文件导入,请检查第"+remark[0]+"行内容" );
		j.setSuccess( true );
		writeJson(j) ;
		//return listQuery() ;
	}
	
	
	
	@LogAnnotation(event="基础信息导入",tablename="t_base_info")
	public int[] readExcel(File file,int flag){
		List<TBaseInfo> list=new ArrayList<TBaseInfo>();
		boolean mark = true ;
		Workbook book = null;
		FileInputStream fis = null;
		int remark[] = {0,0} ;
		try {
			fis = new FileInputStream(file);
			try {
				book = WorkbookFactory.create(fis);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		//	book = getExcelWorkbook(filePath);
			
			Sheet sheet = getSheetByNum(book,1);
//			System.out.println("sheet名称是："+sheet.getSheetName());
			int lastRowNum = sheet.getLastRowNum();
			Row row = sheet.getRow(0) ; 
			int lastCellNum = row.getLastCellNum();
		/*	if( lastCellNum != 6 ){
				remark = -1 ;
				mark = false ;
			}*/
			if( mark ){
			for(int i=0;i<=lastRowNum;i++){
				row = sheet.getRow(i);
				if(row != null){
					//System.out.println("正在读第"+(i+1)+"行：");
					//每一行都会创建新的对象
					lastCellNum = row.getLastCellNum();					
					Cell cell = null;
					StringBuilder sb = null;
					for(int j=0;j<lastCellNum;j++){
						TBaseInfo bInfo=new TBaseInfo();
						cell = row.getCell(j);
						if(cell != null){
							sb = new StringBuilder("第"+(j+1)+"列的单元格内容是：");
							String type_cn = null;
							String type_style = cell.getCellStyle().getDataFormatString().toUpperCase();
							int type = cell.getCellType();
							String value = "";
							switch (type) {
								case 0:
									if(DateUtil.isCellDateFormatted(cell)){
										type_cn = "NUMBER-DATE";
										Date date = cell.getDateCellValue();
										value = sFormat.format(date);
									}else {
										type_cn = "NUMBER";
										double tempValue = cell.getNumericCellValue();
										value = String.valueOf(tempValue);
									}
									break;
								case 1:
									type_cn = "STRING";
									value = cell.getStringCellValue();
									break;
								case 2:
									type_cn = "FORMULA";
									value = cell.getCellFormula();
									break;
								case 3:
									type_cn = "BLANK";
									value = cell.getStringCellValue();
									break;
								case 4:
									type_cn = "BOOLEAN";
									boolean tempValue = cell.getBooleanCellValue();
									value = String.valueOf(tempValue);
									break;
								case 5:
									type_cn = "ERROR";
									byte b = cell.getErrorCellValue();
									value = String.valueOf(b);
								default:
									break;
							}
						
							sb.append(value + ",内容类型是："+type_cn );
							if(flag==0){
								flag=2;
							//	System.out.println("flag为空==========================");
							}
							if(flag==2){
								if( j == 0 ){
									bInfo.setBnumber(value);
								}
								if( j ==1  ){
									bInfo.setBname(value);
								}
								if( j ==2  ){
									bInfo.setBcity(value);
								}
								if( j ==3  ){
									bInfo.setBregion(value);
								}
								if( j ==4  ){
									bInfo.setBaddress(value);
								}
								if( j == 5 &&"NUMBER".equals(type_cn)){
									//出现的问题：如果此value不是数字类型，则转换错误，此处得做判断-------------
									bInfo.setBposx(Double.parseDouble(value));
								}
								if( j ==6&&"NUMBER".equals(type_cn)  ){
									bInfo.setBposy(Double.parseDouble(value));
								}
								if( j ==7  ){
									bInfo.setBfactory(value);
								}
								if( j ==8  ){
									bInfo.setBlevel(value);
								}
								if( j ==9  ){
									bInfo.setBeqcount(value);
								}
								//如果有10条数据，在第七条数据中出现了问题，是否回滚需要考虑
								list.add(bInfo);
							}
							else if(flag==1){

								if( j == 0 ){
									
								}
								if( j ==1  ){
									bInfo.setBnumber(value);
									
								}
								if( j ==2  ){
									bInfo.setBname(value);
									
								}
								if( j ==3  ){
									bInfo.setBcity(value);
									
								}
								if( j ==4  ){
									bInfo.setBregion(value);
									bInfo.setBaddress(value);
								}
								if( j ==5  ){
									bInfo.setBaddress(value);
								}
								
								if( j == 6 &&"NUMBER".equals(type_cn)){
									//出现的问题：如果此value不是数字类型，则转换错误，此处得做判断-------------
									bInfo.setBposx(Double.parseDouble(value));
								}
								if( j ==7&&"NUMBER".equals(type_cn)  ){
									bInfo.setBposy(Double.parseDouble(value));
								}
								if( j ==8  ){
									bInfo.setBtowertype(value);
								}
								if( j ==9  ){
									bInfo.setBtower(value);
								}
								
								//如果有10条数据，在第七条数据中出现了问题，是否回滚需要考虑
								list.add(bInfo);
								
							}
						}
					}
				}
				//remark = i ;
			}
			}
			remark[0]= baseInfoService.addBaseInfoList(list);
			remark[1]=list.size();
		} catch (Exception e) {
			setOperstatus(1) ;
		} 
		return remark ;
	}
	
	/**
	 * 获取excel的Workbook
	 * @throws IOException 
	 */
	public static Workbook getExcelWorkbook(String filePath) throws IOException{
		Workbook book = null;
		File file  = null;
		FileInputStream fis = null;	
		
		try {
			file = new File(filePath);
			if(!file.exists()){
				throw new RuntimeException("文件不存在");
			}else{
				fis = new FileInputStream(file);
				book = WorkbookFactory.create(fis);
				//initStyleMap(book);
			}
		} catch (Exception e) {
			//	System.out.println("dddddddddd");
			throw new RuntimeException(e.getMessage());
		} finally {
			if(fis != null){
				try {
					
					fis.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
					System.out.println(e2.getMessage());
				}
			}
		}
		//System.out.println("dd");
		return book;
	}
	/**
	 * 根据索引 返回Sheet
	 * @param number
	 */
	public static Sheet getSheetByNum(Workbook book,int number){
		Sheet sheet = null;
		try {
			sheet = book.getSheetAt(number-1);
//			if(sheet == null){
//				sheet = book.createSheet("Sheet"+number);
//			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return sheet;
	}
	
	public String listQuery(){
		return "query" ;
	}
	public void uploadFile() throws Exception {
		
		System.out.println("调用uploadFile()");
		String realPath = ServletActionContext.getServletContext().getRealPath("webpage/upload");

		if (baseInfoVo.getExcelFile() != null) {
			File uploadfile = new File(new File(realPath),baseInfoVo.getExcelFileFileName());
			if (!uploadfile.getParentFile().exists()) {
				uploadfile.getParentFile().mkdir();
			}
			if (uploadfile.exists()) {
				uploadfile.delete();
			}
			FileUtils.copyFile(baseInfoVo.getExcelFile(),uploadfile);
			
		}
	}
	@LogAnnotation(event="基础信息导入",tablename="t_base_info")
	public void testExel() throws Exception{
		Json j=new Json();
		List<Object> db = new ArrayList<Object>();
		
		int flag=Integer.parseInt(getRequest().getParameter("flag"));
		
		System.out.println("flag: " + flag);

		  //设置导入规则
		try {
			 Eoiprule rule = new Eoiprule();
			 
			  rule.setSheetnumber((short)0);		//读第一个sheet
			  /*
			   * entity对象字段名与excle列值的映射
			   * 名称,列值;名称,列值;名称,列值;
			   */
			  //室内
				if(flag==2){
					rule.setRowcontentspos((short)1);		//数据内容从第2行开始
					//rule.setValidaterule("1,NotNull;6,0;7,0;10,0");
					rule.setNametocol("bnumber,1;bname,2;bcity,3;bregion,4;baddress,5;bposx,6;bposy,7;bfactory,8;blevel,9;beqcount,10");	//映射三个字段，
				}
				//铁塔
				else{
					rule.setRowcontentspos((short)4);		//数据内容从第5行开始
					//rule.setValidaterule("1,NotNull;6,Exception-Abandon;7,Exception-Abandon;10,0");
					rule.setValidaterule("1,NotNull;6,0;7,0;10,0");
					rule.setNametocol("bnumber,1;bcity,2;bregion,3;bname,4;baddress,5;bposx,6;bposy,7;bfactory,8;btowertype,9;bheight,10;btower,11");	//映射三个字段，
				}
			  Eoip eoip = new Eoip();
			  eoip.setErule(rule);
			  
			  //指定导入的数据库表
			  TBaseInfo mirror = new TBaseInfo();  
		//	  uploadFile();
			  //获得bean list，excle每行对应一个bean对象
			  db = eoip.excel2db(mirror,baseInfoVo.getExcelFile());
			 
			  //加入到数据库中
			  int entid=getSessionUserName().getEntid();
			String count1=baseInfoService.saveList(db,entid,flag);
			
				j.setMsg("导入成功！excel总行数："+eoip.getTotalExcelColumns()+",excel有效数目："+eoip.getTotalExcelDataLines()+",实际保存行数："+count1);
//				j.setMsg("导入成功！");
				j.setSuccess(true);
		} catch (Exception e) {
			// TODO: handle exception
			j.setMsg("导入失败！");
			j.setSuccess(true);
		}
		writeJson(j);
	}
	
	
	
	/**
	 * 级联查询
	 * 通过城市名称找區縣
	 */
	public void getRegionByCity(){

		String  bcity=getRequest().getParameter("bcity");
		List<TBaseInfoVo> pvoList = new ArrayList<TBaseInfoVo>();
		
		pvoList=baseInfoService.getRegionByCity(queryEnterpriseByWhere(),bcity);

		writeJson(pvoList);
	}
	
	
	public void isadmin(){
		Json j=new Json();
		boolean flag=false;
		String ids=getRequest().getParameter("ids");
		flag=baseDao.isadmin(TBaseInfo.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
	
	//添加cookie
	 public void addCookie(String name, String value) {
		        Cookie cookie = new Cookie(name.trim(), value.trim());
		        cookie.setMaxAge(2 * 60 * 60 * 1000);// 设置为2个钟
		        ServletActionContext.getResponse().addCookie(cookie);
	 }
	 
	  public void readShoppingCartFromCookie() throws Exception {
		        System.out.println("======================================================");
		        Cookie cookies[] = ServletActionContext.getRequest().getCookies();
		          if (cookies == null || cookies.length < 0) {
		            // System.out.println("there is no any cookie ..");
		               // 没有cookie
		           } else {
		              for (Cookie c : cookies) {
		               //    System.out.println("haha there are many cookies :" + c.getName() + "    " + c.getValue());
		             }
		         }
		      }

}
