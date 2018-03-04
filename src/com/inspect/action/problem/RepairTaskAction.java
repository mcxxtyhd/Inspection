package com.inspect.action.problem;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.problem.TRepairTask;
import com.inspect.service.InspectUserServiceI;
import com.inspect.service.ProblemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.comon.Json;
import com.inspect.vo.problem.RepairTaskVo;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 抢修任务流程控制Action
 * @version 1.0
 */
@Namespace("/problem")
@Action(value="repairAction",results={
		@Result(name="repairList",location="/webpage/problem/repairList.jsp"),
		@Result(name="repairAdd",location="/webpage/problem/repairAdd.jsp"),
		@Result(name="repairEdit",location="/webpage/problem/repairEdit.jsp")})
public class RepairTaskAction extends BaseAction implements ModelDriven<RepairTaskVo> {
	private static final long serialVersionUID = -4369757369188521234L;
	private static final Logger logger = Logger.getLogger(RepairTaskAction.class);
	private ProblemServiceI problemService;
	private SystemServiceI systemService;
	private InspectUserServiceI inspectUserService;
	@Resource
	public void setInspectUserService(InspectUserServiceI inspectUserService) {
		this.inspectUserService = inspectUserService;
	}
	private RepairTaskVo repairTaskVo=new RepairTaskVo();
	@Resource
	public void setProblemService(ProblemServiceI problemService) {
		this.problemService = problemService;
	}
	@Resource
	public void setSystemService(SystemServiceI systemService) {
		this.systemService = systemService;
	}
	@Resource
	private BaseDaoI baseDao;
	@Override
	public RepairTaskVo getModel() {
		return repairTaskVo;
	}
	
	public String repairList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		getRequest().setAttribute("GroupList",inspectUserService.getGroupList(queryEnterpriseByWhere()));
		return "repairList";
	}
	
	public String repairAdd(){
		getRequest().setAttribute("GroupList",problemService.getGroupOnLineList(queryEnterpriseByWhere()));
		return "repairAdd";
	}
	
	public String repairEdit(){
		getRequest().setAttribute("GroupList",problemService.getGroupOnLineList(queryEnterpriseByWhere()));
		return "repairEdit";
	}
	
	public void repairTaskDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = problemService.findRepairTaskDatagrid(repairTaskVo, page, rows, querySql());
		writeJson(map);
	}
	
	@LogAnnotation(event="添加抢修任务信息",tablename="t_repair_task")
	public void addRepairTask(){
		Json j=new Json();
		try{
			repairTaskVo.setEntid(getSessionUserName().getEntid());
			problemService.addRepairTask(repairTaskVo);
			j.setMsg("添加成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("添加失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="修改抢修任务信息",tablename="t_repair_task")
	public void editRepairTask(){
		Json j=new Json();
		try{
			problemService.editRepairTask(repairTaskVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	@LogAnnotation(event="删除抢修任务信息",tablename="t_repair_task")
	public void removeRepairTask(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(repairTaskVo.getIds())){
				problemService.removeRepairTask(repairTaskVo.getIds());
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
	/**
	 * 总导出
	 */
	@LogAnnotation(event="抢修任务导出",tablename="t_repair_task")
	public void repairTasktoExcel(){

		Json j=new Json();
		try{
			String rcontent1=getRequest().getParameter("rcontent");
			if(StringUtils.isNotEmpty(rcontent1)){
				String rcontent=new String(rcontent1.getBytes("ISO-8859-1"),"utf-8");
				repairTaskVo.setRcontent(rcontent);
			}
			String filePath= problemService.repairTaskVotoexcel(repairTaskVo,querySql());
			//	System.out.println("1");
			if(filePath==null){
				setOperstatus(1);
				j.setMsg("导出失败！");
				writeJson(j);
				return ;
			}
			//下载文件
			downloadFile( filePath ,true  ) ;
			//System.out.println("dd");
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
			//outoutname采用filePath的动态名称（包含设备名称，此前的outoutname是一个固定的名称，后面不会加上设备名称）
			String outputName =  filePath.substring( filePath.toLowerCase().lastIndexOf(File.separator)+1,  filePath.toLowerCase( ).indexOf( ".xlsx" ) );
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
		flag=baseDao.isadmin(TRepairTask.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
}
