<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>	

<script language="javascript">
/*$(document).ready(function(){
	var groupidSelect = $(".groupid").children("select");
	var useridSelect = $(".userid").children("select");
	groupidSelect.change(function(){
		var groupValue = $(this).val();
		if (groupValue != "") {
				$.post('${pageContext.request.contextPath}/problem/ProblemAction!getUserBygroup.action',
					  { groupid: groupValue },
					  function(data){
						var json=new Array();
						json = $.parseJSON(data);
						if (json.length != 0) {
							useridSelect.html("");
							$("<option value=''>请选择用户</option>").appendTo(useridSelect);
							for (var i = 0; i < json.length; i++) {
							$("<option value='" + json[i].id + "'>" + json[i].iuname + "</option>").appendTo(useridSelect);
							}
						} 
					}
				);
		} else {
			alert("else");
			//3.如果值为空，那么第二个下拉框所在span要隐藏起来，第一个下拉框后面的指示图片也要隐藏
			useridSelect.parent().hide();
			groupidSelect.next().hide();
		}
	});

})
*/
/*$(document).ready(function(){
	$("#groupid").change(function(){
		var groupValue = $(this).val();
		if (groupValue != "") {
				$.ajax({
				type: "POST",
					url: '${pageContext.request.contextPath}/problem/ProblemAction!getUserBygroup.action',
					cache: false,
					 data:  { groupid: groupValue },
					 dataType:"json",
					  success: function(data){
						if (data.length != 0) {
							$("#iuserid").empty();
							$("#iuserid").append("<option value=''>请选择用户</option>");
							for (var i = 0; i < data.length; i++) {
								$("#iuserid").append("<option value=\""+data[i].id+"\" selected>"+data[i].iuname+"</option>");
							}
						} 
						else{
						$("#iuserid").empty();
							$("#iuserid").append("<option value=''>请选择用户</option>");
						}
					}
			
				});
		} else {
			alert("else");
			//3.如果值为空，那么第二个下拉框所在span要隐藏起来，第一个下拉框后面的指示图片也要隐藏
			useridSelect.parent().hide();
			groupidSelect.next().hide();
		}
	});

})

	function check_validate1(){  
	  //定义正则表达式部分   
	  alert("dd");
	  var value=document.getElementById("iuserid").value;
		alert(value);
	   var reg = /^\d+$/;   
	    if( reg.test(value)){ 
           alert("shi shuzi");   
             return true;   
	     }   
	     else{
		     alert("is not num");
		  	return false;
	     }
	  }*/
</script>
<div style="padding: 5px;overflow: hidden;">
	<form id="problemForm" method="post">
		 <!--   <div class="group">
			<span class="groupid">
				代维商:
				<td>
					<select  id="groupid"name="groupid" style="width:220px;height:20px;">
					      <option value="">请选择</option>
					      <c:forEach items="${GroupList}" var="g">
				  		  <option value='<c:out value="${g.id}"/>'><c:out value="${g.gname}"/></option>
				  	    </c:forEach>
					    </select>
					 </td>
			</span>
			<span class="userid">
				用户名:
				<select id="iuserid" name="iuserid" class="easyui-validatebox" required="true" missingMessage="用户名不能为空!"  style="width:220px;height:20px;"></select>
			
			</span>
	
		</div> -->
		 
	        <table width="450px" height="330px" class="tableForm" >

			
			<tr>
					<th align="right">问题编号：</th>
					<td><input name="pronumber"  style="width:220px;height:20px;"/></td>
				</tr>
				<tr>
					<th align="right">问题类型：</th>
					<td><input name="protype"  style="width:220px;height:20px;"class="easyui-validatebox" required="true" missingMessage="问题类型不能为空!"/><font color="red">*</font></td>
				</tr>
			
				<tr>
					<th align="right">巡检站点：</th>
					<td><input name="prosite" style="width:220px;height:20px;"class="easyui-validatebox" required="true" missingMessage="巡检站点不能为空!" /><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">巡检周期：</th>
					<td><input name="procycle" style="width:220px;height:20px;" /></td>
				</tr>
				<tr>
				    <th>提交人：</th>
				    <td><select name="iuserid" id="iuserid"  style="width:100px;">
				        <option value="">所有</option>
					    <c:forEach items="${InspectUserList}" var="user">
				  		 <option value='<c:out value="${user.id}"/>'><c:out value="${user.iuname}"/></option>
				  	    </c:forEach>
				      </select></td>
				   </tr>
<!-- 				<tr>
					<th align="right">提交人：</th>
					<td><input name="iuserid" style="width:220px;height:20px;"class="easyui-numberbox"  required="true" missingMessage="请填入数字"/></td> 
					<td><input name="iuserid" style="width:220px;height:20px;"class="easyui-validatebox" required="true" missingMessage="提交人不能为空!" /><font color="red">*</font></td>
				</tr>-->
					<tr>
					<th align="right">终端编号：</th>
					<td><input name="ternumber" style="width:220px;height:20px;" /></td>
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
