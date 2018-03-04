<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
  <SCRIPT LANGUAGE="JavaScript">
   function isOK(pre){ 
   var  result =pre;
  if(result==0){
    $("#tr1").hide();
    $("#tr2").css("display","none");
    $("#tr3").css("display","none");
  }
  if(result==1){
   $("#tr1").css("display","none");
    $("#tr2").show();
    $("#tr3").css("display","none");
  }
  if(result==2){
    $("#tr1").css("display","none");
    $("#tr2").css("display","none");
    $("#tr3").show();
  }
   }
  </SCRIPT>
<div style="padding: 5px;overflow: hidden;">
	<form id="groupForm" method="post">
	        <table width="100%" height="300px" border="1px" cellspacing="0px" style="border-collapse:collapse" >
				<tr>
					<th align="right">计划名称：</th>
					<td><input name="pname" class="easyui-validatebox" required="true" missingMessage="任务么名称不能为空!" style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">计划时间：</th>
					<td><input class="Wdate" name="pstartdate" id="beginDate"  onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd ',alwaysUseStartDate:true})"/>--<input class="Wdate" name="penddate" id="dissucsdate"  onfocus="WdatePicker({stopDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:false,minDate:'#F{$dp.$D(\'beginDate\')}'})"/><font color="red">*</font></td>
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
					<th align="right">巡检方式：</th>
					<td align="left"><input type="radio" name="ptype" id="ptype" onclick="isOK(0)" value="0" checked="checked">每天一次</input>&nbsp;&nbsp;&nbsp;
					<input type="radio" name="ptype" id="ptype" onclick="isOK(1)" value="1">按星期制巡检</input>&nbsp;&nbsp;&nbsp; 
				 <input type="radio" name="ptype" id="ptype" onclick="isOK(2)" value="2">每月一次</input></td>
				</tr>
				<tr style="display: none;" id="tr1">	
					<th align="right">巡检时间：</th>
					<td><input class="Wdate" name="start_date" id="beginDateTime" value="00:00:00"  onfocus="WdatePicker({onclearing:true,startDate:'00:00:00',dateFmt:'HH:mm:ss',alwaysUseStartDate:true,maxDate:' %H:%m:%s'})"/>--
					<input class="Wdate" name="stop_date" id="dissucsdateTime"  onfocus="WdatePicker({onclearing:true,stopDate:'%H:%m:%s',dateFmt:'HH:mm:ss',alwaysUseStartDate:false,minDate:'#F{$dp.$D(\'beginDateTime\')}',maxDate:'%y-%M-%d'})"/></td>				
				</tr>	
				<tr style="display:none" id="tr2">
					<th align="right">巡检日期：</th>				
			      <td><input type="checkbox" name="pweekday" value="1"/>星期一<input type="checkbox" name="pweekday" value="2"/>星期二<input type="checkbox" name="pweekday" value="3"/>星期三<input type="checkbox" name="pweekday" value="4"/>星期四<input type="checkbox" name="pweekday" value="5"/>星期五<input type="checkbox" name="pweekday" value="6"/>星期六<input type="checkbox" name="pweekday" value="0"/>星期日</td>				
				</tr>
				<tr style="display: none" id="tr3">
					<th align="right">巡检日期：</th>				
			      <td><input name="pinspecttime" class="Wdate" type="text" onfocus="WdatePicker({stopDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',alwaysUseStartDate:false,minDate:'#F{$dp.$D(\'beginDate\')}',maxDate:'#F{$dp.$D(\'dissucsdate\')}'})"/></td>
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
					   <textarea name="pdesc" style="height: 60px;width:320px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>