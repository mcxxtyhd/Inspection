package com.inspect.action.problem;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TInspectUser;
import com.inspect.service.BaseInfoServiceI;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.InspectUserServiceI;
import com.inspect.service.ProblemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.DateUtils;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.InspectUserVo;
import com.inspect.vo.comon.Json;
import com.inspect.vo.problem.ProblemVo;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 巡检问题流程控制Action
 * @version 1.0
 */
@Namespace("/problem")
@Action(value="ProblemAction",results={
		@Result(name="problemList",location="/webpage/problem/problemList.jsp"),
		@Result(name="problemAdd",location="/webpage/problem/problemAdd.jsp"),
		@Result(name="test",location="/webpage/problem/test.jsp"),
		@Result(name="problemEdit",location="/webpage/problem/problemEdit.jsp")})
public class ProblemAction extends BaseAction implements ModelDriven<ProblemVo> {
	private static final long serialVersionUID = -9011086728759375544L;

	private static final Logger logger = Logger.getLogger(ProblemAction.class);
	@Resource
	private ProblemServiceI problemService;
	@Resource
	private SystemServiceI systemService;
	@Resource
	private BaseDaoI baseDao;
	@Resource
	private BaseInfoServiceI baseInfoService;
	@Resource
	private InspectItemServiceI inspectItemService;
	
	private ProblemVo problemVo=new ProblemVo();
	
	@Resource
	private InspectUserServiceI inspectUserService;

	@Override
	public ProblemVo getModel() {
		return problemVo;
	}
	
	public String problemList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		getRequestBean();
		return "problemList";
	}
	
	public String problemAdd(){
		getRequestBean();
		return "problemAdd";
	}
	
	
	public void test(){
		//getRequestBean();
		Json j=new Json();
	String flag1=getRequest().getParameter("flag");
		String flag=inspectItemService.test("33", 11, flag1);
		if("1".equals(flag)){
			j.setMsg("成功！");
		}
		else{
			j.setMsg("失败");
		}
		writeJson(j);
	}
	public String problemEdit(){
		getRequestBean();
		return "problemEdit";
	}
	
	public void problemDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		
		String buf1="";
		String buf2="";
		if(!StringUtils.isEmpty(problemVo.getProsite())){
			buf1=inspectItemService.getEquipment1Byenameornum(problemVo.getProsite().trim(), queryEnterpriseByWhere(),0);
		}
		if(!StringUtils.isEmpty(problemVo.getPrositenum())){
			buf2=inspectItemService.getEquipment1Byenameornum(problemVo.getPrositenum().trim(), queryEnterpriseByWhere(),1);
		}
		Map<String, Object> map = problemService.findProblemDatagrid1(problemVo, page, rows, querySql(),buf1,buf2);
		//	Map<String, Object> map = problemService.findProblemDatagrid(problemVo, page, rows, querySql(),buf1);
		writeJson(map);
	}
	
	@LogAnnotation(event="添加巡检问题问题",tablename="t_inspect_problem")
	public void addProblem(){
		Json j=new Json();
		try{
			boolean flag=false;
			problemVo.setEntid(getSessionUserName().getEntid());
			flag=problemService.isAddorEdit(problemVo.getTernumber(),problemVo.getPrositenum(), queryEnterpriseByWhere());
			
			if(flag==true){
				TBaseInfo b=baseInfoService.getBaseInfoByBnum(problemVo.getPrositenum(), queryEnterpriseByWhere());
				if(b!=null){
					problemVo.setPrositenum(b.getBnumber());
					problemVo.setProsite(String.valueOf(b.getId()));
					problemVo.setPrositename(b.getBname());
				}
				problemService.addProblem(problemVo);
				j.setMsg("添加成功！");
				j.setSuccess(true);
			}
			
			else{
				setOperstatus(1);
				j.setMsg("添加失败,请输入正确设备编号和终端编号！");
			}
			
		
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="修改巡检问题",tablename="t_inspect_problem")
	public void editProblem(){

		Json j=new Json();
		try{
			boolean flag=false;
			problemVo.setEntid(getSessionUserName().getEntid());
			flag=problemService.isAddorEdit(problemVo.getTernumber(),problemVo.getPrositenum(), queryEnterpriseByWhere());
			
			if(flag==true){
				TBaseInfo b=baseInfoService.getBaseInfoByBnum(problemVo.getPrositenum(), queryEnterpriseByWhere());
				if(b!=null){
					problemVo.setPrositenum(b.getBnumber());
					problemVo.setProsite(String.valueOf(b.getId()));
					problemVo.setPrositename(b.getBname());
				}
				problemService.editProblem(problemVo);
				j.setMsg("修改成功！");
				j.setSuccess(true);
			}
			else{
				setOperstatus(1);
				j.setMsg("修改失败,请输入正确设备编号和终端编号!");
			}
		
			
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除巡检问题",tablename="t_inspect_problem")
	public void removeProblem(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(problemVo.getIds())){
				problemService.removeProblem(problemVo.getIds());
				j.setSuccess(true);
				j.setMsg("删除成功！");
			}
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("删除失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	public void getUserBygroup(){
		String  groupid=getRequest().getParameter("groupid");
		List<TInspectUser> inspectUserList = new ArrayList<TInspectUser>();
		@SuppressWarnings("unused")
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		inspectUserList=inspectUserService.findInspectUserByGroupid(groupid);
		List<InspectUserVo> voList=new ArrayList<InspectUserVo>();
		//方法一
		if(inspectUserList!=null&&inspectUserList.size()>0){
			for(int i=0;i<inspectUserList.size();i++){
				InspectUserVo inspectUserVo=new InspectUserVo();
			    BeanUtils.copyProperties(inspectUserList.get(i), inspectUserVo);
			    voList.add(inspectUserVo);
			}
		/*	方法二
		 * for(int i=0;i<inspectUserList.size();i++){
				Map<String, Object> mapClazz = new HashMap<String, Object>();
				mapClazz.put("id", inspectUserList.get(i).getId()+"");
				mapClazz.put("iuname", inspectUserList.get(i).getIuname());
				mapClazz.put("iuname1", inspectUserList.get(i));
				mapList.add(mapClazz);
			}*/
		}
	//	writeJson(mapList);
		writeJson(voList);
	}
	public void getRequestBean(){
		getRequest().setAttribute("TaskList",inspectItemService.getPlanTaskList(queryEnterpriseByWhere()));
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(queryEnterpriseByWhere()));
	}
	/**
	 * 总导出
	 */
	@LogAnnotation(event="巡检问题导出",tablename="t_inspect_problem")
	public void problemtoExcel(){

		Json j=new Json();
		try{
			String prosite1=getRequest().getParameter("prosite");
			if(StringUtils.isNotEmpty(prosite1)){
				String prosite=new String(prosite1.getBytes("ISO-8859-1"),"utf-8");
				problemVo.setProsite(prosite);
			}
		
			String filePath= problemService.problemtoexcel(problemVo,querySql(),queryEnterpriseByWhere());
			//System.out.println("1");
			if(filePath==null){
				setOperstatus(1);
				j.setMsg("导出失败！");
				writeJson(j);
				return ;
			}
			//下载文件
			downloadFile(filePath ,true  ) ;
			//	System.out.println("dd");
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("导出失败！");
			writeJson(j);
		}
	}
	/***
	 * 导出excel文件
	 * @param filePath   需要下载文件的路径
	 * @param isDelete   是否需要删除文件的路径下的文件
	 * @author liao
	 */
	public void downloadFile( String filePath,boolean isDelete ){
		InputStream inStream = null;
		OutputStream os = null;
		try {
			// 下载本地文件
			//String fileName = (System.currentTimeMillis() + ".xls").toString(); // 文件的默认保存名
			// 读到流中
			//传过来的outoutname不再用，而采用filePath的动态名称（包含设备名称，此前的outoutname是一个固定的名称，后面不会加上设备名称）
			String outputName  =  filePath.substring( filePath.toLowerCase().lastIndexOf(File.separator)+1,  filePath.toLowerCase( ).indexOf( ".xlsx" ) );
			String newpath = toUtf8String( getRequest() , outputName + ".xlsx" ) ; //URLEncoder.encode(fileName, "utf-8");
			inStream = new FileInputStream(filePath);// 文件的存放路径
			// 设置输出的格式
			getResponse().reset();
			os = ServletActionContext.getResponse().getOutputStream();
			ServletActionContext.getResponse().setContentType("application/x-msdownload;charset=UTF-8");
			ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment; filename=" + newpath);
			
			// 循环取出流中的数据
			byte[] b = new byte[100];
			int len;
			while ((len = inStream.read(b)) > 0) {
				os.write(b, 0, len);
				len = 0;
			}
			os.flush();
			os.close();
			inStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
					os = null;
				}
				if (inStream != null) {
					inStream.close();
					inStream = null;
				}
				if( isDelete ){
					File file = new File(filePath);
					if (file.isFile() && file.exists()) {
						file.delete();
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void isadmin(){
		Json j=new Json();
		boolean flag=false;
		String ids=getRequest().getParameter("ids");
		flag=baseDao.isadmin(TEnumParameter.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
}
