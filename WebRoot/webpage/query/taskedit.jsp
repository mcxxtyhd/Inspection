<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>

<div style="padding: 5px;overflow: hidden;">
	<form id="projectForm" method="post">
	 <input name="id" type="hidden" />
			<table width="100%" height="300px" border="1px" cellspacing="0px" style="border-collapse:collapse" >
				<tr>
					<th align="right">计划名称：</th>
					<td><input name="pname" class="easyui-validatebox" required="true" missingMessage="任务么名称不能为空!" style="width:220px;height:20px;" readonly="readonly"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">计划时间：</th>
					<td><input class="Wdate" name="pstartdate" id="beginDate"  onfocus="WdatePicker({startDate:'%y-%M-01',dateFmt:'yyyy-MM-dd ',alwaysUseStartDate:true})" readonly="readonly"/>--<input class="Wdate" name="penddate" id="dissucsdate"  onfocus="WdatePicker({stopDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:false,minDate:'#F{$dp.$D(\'beginDate\')}'})" readonly="readonly"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">巡检路线：</th>
					<td><select name="plineid">
					<c:forEach var="line" items="${requestScope.LineList}">
					<option value="${line.id}">${line.lname}</option>
					</c:forEach>
					</select> </td>
				</tr>
				<tr>
					<th align="right">巡检时间：</th>
					<td><input class="Wdate" name="pstarttime" id="beginDateTime" value="00:00:00"  onfocus="WdatePicker({onclearing:true,startDate:'00:00:00',dateFmt:'HH:mm:ss',alwaysUseStartDate:true,maxDate:' %H:%m:%s'})"/>--
					<input class="Wdate" name="pendtime" id="dissucsdateTime"  onfocus="WdatePicker({onclearing:true,stopDate:'%H:%m:%s',dateFmt:'HH:mm:ss',alwaysUseStartDate:false,minDate:'#F{$dp.$D(\'beginDateTime\')}',maxDate:'%y-%M-%d'})"/></td>
				</tr>
						
				<tr>
					<th align="right">巡检人员：</th>
					<td><select name="puid">
					<c:forEach var="user" items="${requestScope.InspectUserList}">
					 <option value="${user.id}">${user.iuname}</option>
					</c:forEach>
					</select> </td>
				</tr>				
				<tr>
					<th align="right">任务描述：</th>
					<td>
					   <textarea name="pdesc" style="height: 60px;width:320px;" ></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>