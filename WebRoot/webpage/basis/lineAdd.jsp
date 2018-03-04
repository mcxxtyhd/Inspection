<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<script language="javascript">
	var ListUtil = new Object();
	//全部移动
	ListUtil.moveAll = function moveAll(oListboxFrom,lpIds){
		var options = oListboxFrom.options;
		for(var i = 0; i < options.length; i++){
			lpIds.appendChild(options[i]); 
			i -= 1;  //每删除一个选项后，每个选项的index会被重置
		}
	}
	//单个或多个移动
	ListUtil.moveMuti = function moveMuti(oListboxFrom,lpIds){
		var options = oListboxFrom.options;
		for(var i = 0; i < options.length; i++){
			if(options[i].selected){
				lpIds.appendChild(options[i]);// 默认选中
				i -= 1;
			}
		}
	}
	//上移
	ListUtil.shiftUp = function(oListbox) {
		if(oListbox.selectedIndex > 0){
			var oOption = oListbox.options[oListbox.selectedIndex];
			var oPrevOption = oListbox.options[oListbox.selectedIndex-1];
			oListbox.insertBefore(oOption,oPrevOption);
		}
	}
	//下移
	ListUtil.shiftDown = function(oListbox){
		if(oListbox.selectedIndex < oListbox.options.length-1){
			var oOption = oListbox.options[oListbox.selectedIndex];
			var oNextOption = oListbox.options[oListbox.selectedIndex+1];
			oListbox.insertBefore(oNextOption,oOption);
		}
	}	
	
	//查询操作
	function _search() {
		var eregion = $("#eregion").val();
		var poname = $("#poname").val();
		//alert("eregion="+eregion);
			$.ajax({
				type: "POST",
					url: '${pageContext.request.contextPath}/basis/lineAction!getEquipByRegion.action',
					cache: false,
					 data:  { bregion: eregion,
							  poname:poname
							},
					 dataType:"json",
					  success: function(data){
						if (data.length != 0) {
							$("#oListboxFrom").empty();
							for (var i = 0; i < data.length; i++) {
							//alert("data[i].id"+data[i].id+"\tdata[i].ename"+data[i].bname);
 							$("#oListboxFrom").append("<option value=\""+data[i].id+"\">"+data[i].poname+"</option>");
							}
						} 
						else{
						$("#oListboxFrom").empty();
						}
					}
			
				});
	}
	
	//清空操作 
	function cleanSearch() {
		$("#eregion").val("");
		$("#ename").val("");
	}
	</script>
<div style="padding: 5px;overflow: hidden;">
	<form id="lineForm" method="post">
			<table width="600px" height="410px" class="tableForm" >
			<tr>	
				<th  style="width: 100px;align="right">巡检点名称：</th>
					<td colspan="3" ><input name="poname" id="poname" style="width:400px;height:20px;"/></td>
				 
			</tr>
			<tr>	
				<th style="width: 100px;">区县:</th>
				<td >
					<select  id="eregion"name="eregion" id="bregion" style="width:120px;height:20px;">
						<option value="">请选择</option>
					      <c:forEach items="${eList}" var="g">
				  		  <option value='<c:out value="${g.eregion}"/>'><c:out value="${g.eregion}"/></option>
				  	    </c:forEach>
					    </select>
					</td>
					<td align="right">
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
				
			</tr>
			<tr>
					<th align="right">线路名称：</th>
					<td colspan="2"><input name="lname" class="easyui-validatebox" required="true" missingMessage="名称不能为空!"  style="width:240px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
			      <td colspan="4" align="center">
			        <table align="center">
			         <tr align="center">
			         <td>
			      <span style="color:red;align:center" >==可选巡检点==</span>
				   <select name="oListboxFrom" id="oListboxFrom" size="10" multiple="true" style="width:150px;height:300px;margin-left:20px;float:left">
					  <c:forEach items="${PointList}" var="point">
				  		<option value='<c:out value="${point.id}"/>'><c:out value="${point.poname}"/></option>
				  	</c:forEach>
				</select>
				</td>
				
				<td align="center" width="100">
				<input type="button" value=" >> " onclick="ListUtil.moveAll(oListboxFrom, lpIds);" style="width:80px;height:25px;float:center" />
				<br/><br/>
				<input type="button" value=" > " onclick="ListUtil.moveMuti(oListboxFrom, lpIds);" style="width:80px;height:25px;float:center"/>
				<br/><br/>
				<input type="button" value=" < " onclick="ListUtil.moveMuti(lpIds, oListboxFrom);" style="width:80px;height:25px;float:center"/>
				<br/><br/>
				<input type="button" value=" << " onclick="ListUtil.moveAll(lpIds, oListboxFrom);" style="width:80px;height:25px;float:center"/>
				<br/><br/>
				<input type="button" value=" ↑ " onclick="ListUtil.shiftUp(lpIds);" style="width:80px;height:25px;float:center"/>
				<br/><br/>
				<input type="button" value=" ↓ " onclick="ListUtil.shiftDown(lpIds);" style="width:80px;height:25px;float:center"/>
                </td>
                <td>
				<span style="color:red;align:center" >==已选巡检点==</span>
				<select name="lpIds" id="lpIds" multiple="multiple" size="10"	
				style="width:150px;height:300px;margin-left:20px;float:left">
				</select>
			</td>
		   </tr>
		  </table>
		 </td>
	    </tr>
	  </table>
	</form>
</div>