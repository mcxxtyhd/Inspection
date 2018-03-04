package com.inspect.action.summary;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.inspect.action.common.BaseAction;
import com.inspect.service.SummaryServiceI;
import com.inspect.vo.basis.SummaryConfigVo;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/summaryConfig")
@Action(value="summaryConfigAction",results={
		@Result(name="summaryConfig",location="/webpage/summary/summaryConfig.jsp")
		})
public class SummaryConfigAction extends BaseAction implements ModelDriven<SummaryConfigVo> {

	private SummaryConfigVo summaryConfigvo=new SummaryConfigVo();
	@Override
	public SummaryConfigVo getModel() {
		// TODO Auto-generated method stub
		return summaryConfigvo;
	}
	@Resource
	private SummaryServiceI summaryService;
	/**
	 * 跳转至excel配置页面
	 * @return
	 */
	public String excelEdit(){
		//System.out.println(getRequest().getParameter("xgid"));
		//	System.out.println(getRequest().getParameter("xequid"));
		//	System.out.println(getRequest().getParameter("sConfigFlag"));
		 getRequest().setAttribute("xgid",getRequest().getParameter("xgid"));
		 getRequest().setAttribute("xequid",getRequest().getParameter("xequid"));
		 getRequest().setAttribute("sConfigFlag",getRequest().getParameter("sConfigFlag"));//设备类型（铁塔或室内）
		return "summaryConfig";
	}
	/**
	 * 查找导出excel表的配置属性
	 */
	public void editExcel(){
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		int flag=Integer.parseInt(getRequest().getParameter("flag"));
		//System.out.println(flag);
		summaryConfigvo.setFlag(flag);
		Map<String, Object> map = summaryService.editExcel(summaryConfigvo,page,rows,querySql());
		writeJson(map);
	}

}
