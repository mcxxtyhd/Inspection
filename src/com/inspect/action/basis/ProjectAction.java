package com.inspect.action.basis;

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
import com.inspect.model.basis.TProject;
import com.inspect.model.basis.TProjectGroup;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.vo.basis.ProjectVo;
import com.inspect.vo.comon.Json;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 巡检项信息业务流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="projectAction",results={
		@Result(name="projectList",location="/webpage/basis/projectList.jsp"),
		@Result(name="projectAdd",location="/webpage/basis/projectAdd.jsp"),
		@Result(name="projectEdit",location="/webpage/basis/projectEdit.jsp")})
public class ProjectAction extends BaseAction implements ModelDriven<ProjectVo> {

	private static final long serialVersionUID = -4978847358442835752L;

	private static final Logger logger = Logger.getLogger(ProjectAction.class);
	@Resource
	private BaseDaoI baseDao;
	
	
	private InspectItemServiceI inspectItemService;
	
	private SystemServiceI systemService;
	
	private ProjectVo projectVo=new ProjectVo();
	
	public InspectItemServiceI getInspectItemService() {
		return inspectItemService;
	}
	@Resource
	public void setInspectItemService(InspectItemServiceI inspectItemService) {
		this.inspectItemService = inspectItemService;
	}
	
	public SystemServiceI getSystemService() {
		return systemService;
	}

	@Resource
	public void setSystemService(SystemServiceI systemService) {
		this.systemService = systemService;
	}
	@Override
	public ProjectVo getModel() {
		return projectVo;
	}
	
	public String projectList(){
		getRequest().setAttribute("EnterpriseList",systemService.comboboxEnterprise(queryEnterpriseByWhere()));
		getRequest().setAttribute("ProjectGroupList",inspectItemService.getProjectGroupList(querySql()));
		
		return "projectList";
	}
	
	public String projectAdd(){
		getRequest().setAttribute("ProjectGroupList",inspectItemService.getProjectGroupList(querySql()));
		getRequest().setAttribute("NumerTypeList",inspectItemService.getEnumParameListByParameType(querySql(),"0"));
		return "projectAdd";
	}
	
	public String projectEdit(){
		getRequest().setAttribute("ProjectGroupList",inspectItemService.getProjectGroupList(querySql()));
		getRequest().setAttribute("NumerTypeList",inspectItemService.getEnumParameListByParameType(querySql(),"0"));
		getRequest().setAttribute("ProjectBean",inspectItemService.getEntityById(TProject.class,Integer.parseInt(getRequest().getParameter("PID"))));
		return "projectEdit";
	}
	
	public void projectDatagrid(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectItemService.findProjectDatagrid(projectVo,page,rows,querySql());
	  
		writeJson(map);
	}

	@LogAnnotation(event="添加巡检项信息",tablename="t_project")
	public void addProject(){
		Json j=new Json();
		try{
			long cname=this.inspectItemService.countProjectName(projectVo.getPname(),queryEnterpriseByWhere());
			if(cname>0){
				 j.setMsg("【"+projectVo.getPname()+"】已存在,不能重复添加!");
				 setOperstatus(1);
			}else{
				projectVo.setEntid(getSessionUserName().getEntid());
				inspectItemService.addProject(projectVo);
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
	
	@LogAnnotation(event="修改巡检项信息",tablename="t_project")
	public void editProject(){
		Json j=new Json();
		try{
			inspectItemService.editProject(projectVo);
			j.setMsg("修改成功！");
			j.setSuccess(true);
		}catch(Exception e){
			setOperstatus(1);
			j.setMsg("修改失败！");
			logger.error(ExceptionUtil.getExceptionMessage(e));
		}
		writeJson(j);
	}
	
	@LogAnnotation(event="删除巡检项信息",tablename="t_project")
	public void deleteProject(){
		Json j=new Json();
		try{
			if(!StringUtils.isEmpty(projectVo.getIds())){
				inspectItemService.removeProject(projectVo.getIds());
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
	public void toTXT(){

		Json j=new Json();
		try{
			String pgroupid = getRequest().getParameter("pgroupid");
			String entid = getRequest().getParameter("entid");
			String pname = getRequest().getParameter("pname");
			String ptype = getRequest().getParameter("ptype");
			String summanyname="巡检项名称";
			StringBuffer buf=new StringBuffer("1=1") ;
			if(StringUtils.isNotEmpty(pgroupid)){
				buf.append("and pgroupid=").append(pname);			
			}
			if(StringUtils.isNotEmpty(pname)){
				buf.append("and pname like '%").append(pgroupid);			
			}
			if(StringUtils.isNotEmpty(ptype)){
				buf.append("and ptype = '").append(pname).append("'");			
			}
			if(StringUtils.isNotEmpty(entid)){
				buf.append("and entid =").append(entid);			
			}
			//System.out.println(projectVo.getPgroupid());
			//getProjectName(String pgroupid,)
			String path="d:" + File.separator + "巡检项名称.txt";
			//downloadTXT( summanyname, path ,true  ) ;
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
	public void downloadTXT( String outputName , String filePath,boolean isDelete ){
		InputStream inStream = null;
		OutputStream os = null;
		try {
			// 下载本地文件
			//String fileName = (System.currentTimeMillis() + ".xls").toString(); // 文件的默认保存名
			// 读到流中
			String newpath = toUtf8String( getRequest() , outputName + ".txt" ) ; //URLEncoder.encode(fileName, "utf-8");
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
		flag=baseDao.isadmin(TProject.class,ids,queryEnterpriseByWhere());
		j.setSuccess(flag);
		writeJson(j);
	}
}
