<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<script language="javascript">
	var ListUtil = new Object();
	//全部移动
	ListUtil.moveAll = function moveAll(oListboxFrom,epids){
		var options = oListboxFrom.options;
		for(var i = 0; i < options.length; i++){
			epids.appendChild(options[i]); 
			i -= 1;  //每删除一个选项后，每个选项的index会被重置
		}
	}
	//单个或多个移动
	ListUtil.moveMuti = function moveMuti(oListboxFrom,epids){
		var options = oListboxFrom.options;
		for(var i = 0; i < options.length; i++){
			if(options[i].selected){
				epids.appendChild(options[i]);// 默认选中
				i -= 1;
			}
		}
	}
	
	//选择基础信息
	function addBaseInfoDetail() {
		  var p = parent.sy.dialog({
			title : '基础选择',
			iconCls : 'icon-tip',
			resizable: true,
			collapsible:true,
			href: '${pageContext.request.contextPath}/baseInfo/baseInfoAction!baseInfoView.action',
			width : 650,
			height : 500,
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler : function() {
				 var rows = $('#basis_equipmentAdd_datagrid').datagrid('getSelections');
				 if (rows.length > 0) {
					 $('#epid').val(rows[0].id);
					 $('#epname').val(rows[0].bname);
					 p.dialog('close');
				 }else{
					 $.messager.alert('提示', '请选择基础信息！', 'error');
				 }
				}
			} ,{
			    text : '关闭',
			    iconCls : 'icon-cancel',
				handler : function(){
				  p.dialog('close');
				}
			}]
		});
	}
	
	</script>
<div style="padding: 5px;overflow: hidden;">
	<form id="equipmentForm" method="post">
	<input type="hidden" name="epid" id="epid"/>
			<table width="450px" height="410px" class="tableForm" >
			<tr>
				<th align="right">设备名称：</th>
				<td colspan="3">
				 <input name="epname" id="epname" class="easyui-validatebox" required="true" missingMessage="设备名称不能为空!"  style="width:240px;height:20px;"/><font color="red">*</font>
				 <input type="button" name="btn" value="选择基础信息" onclick="javascript:addBaseInfoDetail();"  style="width:100px;height:25px;float:center"/>
				</td>
			</tr>
			<tr>
			  <td colspan="4" align="center">
			    <table align="center">
			      <tr align="center">
			         <td>
				      <span style="color:red;align:left" >==可选巡检项==</span>
					   <select name="oListboxFrom" id="oListboxFrom" size="10" multiple="true" style="width:150px;height:320px;margin-left:20px;float:left">
						  <c:forEach items="${ProjectList}" var="project">
					  		<option value='<c:out value="${project.id}"/>'><c:out value="${project.pgname}"/></option>
					  	</c:forEach>
					   </select>
					</td>
				<td align="center" width="100">
					<input type="button" value=" >> " onclick="ListUtil.moveAll(oListboxFrom, epids);" style="width:80px;height:25px;float:center" />
					<br/><br/>
					<input type="button" value=" > " onclick="ListUtil.moveMuti(oListboxFrom, epids);" style="width:80px;height:25px;float:center"/>
					<br/><br/>
					<input type="button" value=" < " onclick="ListUtil.moveMuti(epids, oListboxFrom);" style="width:80px;height:25px;float:center"/>
					<br/><br/>
					<input type="button" value=" << " onclick="ListUtil.moveAll(epids, oListboxFrom);" style="width:80px;height:25px;float:center"/>
					<br/><br/>
                </td>
                <td>
				<span style="color:red;align:center" >==已选巡检项==</span>
					<select name="epids" id="epids" multiple="multiple" size="10"	
						style="width:150px;height:320px;margin-left:20px;float:left">
					</select>
			    </td>
			      </tr>
			    </table>
			  </td>
		    </tr>
			</table>
	</form>
</div>
