package com.inspect.vo.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.bcel.generic.NEW;

import com.inspect.model.BaseModel;
import com.inspect.model.monitor.TInspectItemDetailReport;
import com.inspect.model.monitor.TInspectItemRaltionReport;

/**
 * 巡检信息实时上报实体类
 * @author wzs
 */

public class TermInspectItemReportVo1 {

	private Set<InspectItemDetailReportVo1> inspectreportdetailmsgs=new HashSet<InspectItemDetailReportVo1>();

	public Set<InspectItemDetailReportVo1> getInspectreportdetailmsgs() {
		return inspectreportdetailmsgs;
	}

	public void setInspectreportdetailmsgs(
			Set<InspectItemDetailReportVo1> inspectreportdetailmsgs) {
		this.inspectreportdetailmsgs = inspectreportdetailmsgs;
	}


	


}
