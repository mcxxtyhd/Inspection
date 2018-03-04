package com.inspect.action.basis;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.inspect.action.common.BaseAction;
import com.inspect.annotation.LogAnnotation;
import com.inspect.dao.BaseDaoI;
import com.inspect.model.baseinfo.TBaseInfo;
import com.inspect.model.basis.TEnumParameter;
import com.inspect.model.basis.TEquipment;
import com.inspect.model.basis.TEquipmentProjectGroup;
import com.inspect.model.basis.TProject;
import com.inspect.model.basis.TProjectGroup;
import com.inspect.service.InspectItemServiceI;
import com.inspect.service.SystemServiceI;
import com.inspect.util.common.ExceptionUtil;
import com.inspect.util.common.StringUtils;
import com.inspect.util.excel.Eoip;
import com.inspect.util.excel.Eoiprule;
import com.inspect.vo.basis.EquipmentVo;
import com.inspect.vo.basis.PointVo;
import com.inspect.vo.basis.UnicomVo;
import com.inspect.vo.comon.Json;
import com.inspect.vo.summary.EquipmentSummaryVo;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 巡检设备业务流程控制Action
 * @version 1.0
 */
@Namespace("/basis")
@Action(value="docAction",results={
		@Result(name="docPrint",location="/webpage/forms/recordtable.jsp"),
		@Result(name="docList",location="/webpage/forms/index.jsp")})
public class DocAction extends BaseAction implements ModelDriven<UnicomVo> {

	@Resource
	private BaseDaoI baseDao;
	private InspectItemServiceI inspectItemService;
	private SystemServiceI systemService;
	
	private UnicomVo unicomVo=new UnicomVo();
	
public static SimpleJdbcTemplate config(String url) {
		
		MysqlDataSource mds = new MysqlDataSource();
		mds.setUrl(url);
		
		try {
			mds.getConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new SimpleJdbcTemplate(mds);
	}
static String urlLocal = "jdbc:mysql://localhost:3306/inspect?user=root&password=123";

	public String docList(){
		System.out.println("docList()");
		//getRequest().setAttribute("UnicomList",systemService.comboboxUnicom(0));
		String sql = "select administrativedivision,basename from unicominfo";
		SimpleJdbcTemplate jdbcLocal = config(urlLocal);
		List<Map<String, Object>> doclist = jdbcLocal.queryForList(sql);
		getRequest().setAttribute("UnicomList",doclist);
		
		return "docList";
	}
	
	public String docPrint(){
		System.out.println("Print()");
		//getRequest().setAttribute("UnicomList",systemService.comboboxUnicom(0));
	
		return "docPrint";
	}
	
	public InspectItemServiceI getInspectItemService() {
		System.out.println("getInspectItemService()");
		return inspectItemService;
	}

	@Override
	public UnicomVo getModel() {
		// TODO Auto-generated method stub
		System.out.println("getModel()");
		return unicomVo;
	}
	//docDatagrid
	public void docDatagrid(){
		System.out.println("docDatagrid()");
		int page =Integer.parseInt(ServletActionContext.getRequest().getParameter("page") );
		int rows =Integer.parseInt(ServletActionContext.getRequest().getParameter("rows") );
		Map<String, Object> map = inspectItemService.findUnicomDatagrid(unicomVo,page,rows,"");
		
		
		System.out.println("100docactionUnicommap "+map);
		
		writeJson(map);
	}
	
}

	