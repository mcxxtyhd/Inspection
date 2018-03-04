<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
<script language="javascript">
$(document).ready(function(){
 //设备列表
	/* 	$('#prosite').combogrid({    
				    panelWidth:340,
				    panelHeight :300,
				    title :'设备信息',
				    idField:'id',    
				    textField:'ename', 
				    url:'${pageContext.request.contextPath}/basis/equipmentAction!equipmentInfoDatagridNotPage.action',
				    //pagination : true,
			        //pagePosition : 'bottom',
			        //pageSize :8,
			        //pageList : [8,16,24,32],
				    fitColumns:true,   
				    required:true,
				    missingMessage:'巡检站点不能为空!',
				    columns:[[  
				    	{title : '设备编号',field : 'enumber',width : 120},
				        {title : '设备名称',field : 'ename',width : 220}
				    ]]    
				}); 
 */
	});
</script>
<div style="padding: 5px;overflow: hidden;">
	<form id="problemForm" method="post">
	      <input type="hidden" name="id"/>
	       <table width="450px" height="330px" class="tableForm" >
			   <tr>
					<th align="right">任务名称：</th>
					<td>
					  <select name="proitaskid" style="width:220px;height:20px;" class="easyui-validatebox" required="true" missingMessage="任务名称不能为空!">
					        <option value="">请选择</option>
						    <c:forEach items="${TaskList}" var="task">
					  		 <option value='<c:out value="${task.id}"/>'><c:out value="${task.pname}"/></option>
					  	    </c:forEach>
					      </select>
					  </td>
				</tr>
				<tr>
					<th align="right">巡检周期：</th>
					<td><input name="procycle" class="Wdate" onfocus="WdatePicker({startDate:'%y-%M',dateFmt:'yyyy-MM ',alwaysUseStartDate:true})" editable="false" style="width:220px;height:20px;" /></td>
				</tr>
				<tr>
					<th align="right">设备编号：</th>
					<td><input name="prositenum" id="prositenum" style="width:220px;height:20px;" required="true" missingMessage="设备编号不能为空!"/></td>
				</tr>
				<tr>
					<th align="right">问题类型：</th>
					<td><input name="protype"  style="width:220px;height:20px;"/></td>
				</tr>
				<tr>
				    <th>维护队：</th>
				  <td>  <select name="iuserid" style="width:220px;height:20px;">
					        <option value="">请选择</option>
						    <c:forEach items="${GroupList}" var="group">
					  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
					  	    </c:forEach>
					      </select></td>
				   </tr>
					<tr>
					<th align="right">终端编号：</th>
					<td><input name="ternumber" style="width:220px;height:20px;" required="true" missingMessage="终端编号不能为空!"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">问题描述：</th>
					<td> <textarea name="prodesc" style="height: 60px;width:320px;"></textarea></td>
				</tr>
				<tr>
					<th align="right">备注：</th>
					<td>
					   <textarea name="proremark" style="height: 60px;width:320px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>