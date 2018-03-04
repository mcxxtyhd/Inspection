<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>	
<div style="padding: 5px;overflow: hidden;">
	<form id="problemForm" method="post">
	        <table width="450px" height="180px" class="tableForm" >
				<tr>
					<th align="right">维护队：</th>
				    <td>  <select name="rgid" style="width:220px;height:20px;" class="easyui-validatebox" required="true" missingMessage="维护队不能为空!">
					        <option value="">请选择</option>
						    <c:forEach items="${GroupList}" var="group">
					  		 <option value='<c:out value="${group.id}"/>'><c:out value="${group.gname}"/></option>
					  	    </c:forEach>
					      </select>
					 </td>
				 </tr>
				 <tr>
					<th align="right">抢修类别：</th>
				    <td>  <select name="rcategory" style="width:220px;height:20px;" class="easyui-validatebox" required="true" missingMessage="维护队不能为空!">
					        <option value="">请选择</option>
					  		 <option value='抢修'><c:out value="抢修"/></option>
					  		 <option value='优化'><c:out value="优化"/></option>
					      </select>
					 </td>
				 </tr>
				
				 <tr>
				 <th width="10%">截止日期：</th>
				<td width="6%"><input class="Wdate"  class="easyui-datebox" required="true" missingMessage="日期不能为空!"style="width:130px;height:20px;" name="rendtime" id="beginDate" onblur="rendtime()"  onfocus="WdatePicker({isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH-mm'})"/></td>
				 </tr>
				  <tr>
					<th align="right">抢修内容：</th>
					<td><textarea name="rcontent" style="height: 110px;width:340px;" class="easyui-validatebox" required="true" missingMessage="抢修内容不能为空!"></textarea></td>
				</tr>
			</table>
	</form>
</div>