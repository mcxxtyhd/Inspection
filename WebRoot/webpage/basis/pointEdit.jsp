<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp"%>
<script language="javascript">
	var ListUtil = new Object();
	//全部移动
	ListUtil.moveAll = function moveAll(oListboxFrom,poeIds){
		var options = oListboxFrom.options;
		for(var i = 0; i < options.length; i++){
			poeIds.appendChild(options[i]); 
			i -= 1;  //每删除一个选项后，每个选项的index会被重置
		}
	}
	//单个或多个移动
	ListUtil.moveMuti = function moveMuti(oListboxFrom,poeIds){
		var options = oListboxFrom.options;
		for(var i = 0; i < options.length; i++){
			if(options[i].selected){
				poeIds.appendChild(options[i]);// 默认选中
				i -= 1;
			}
		}
	}
//查询操作
	function _search() {
		var eregion = $("#eregion").val();
		var ename = $("#ename").val();
		//alert("eregion="+eregion);
			$.ajax({
				type: "POST",
					url: '${pageContext.request.contextPath}/basis/pointAction!getEquipByRegion.action',
					cache: false,
					 data:  { bregion: eregion,
							  ename:ename
							},
					 dataType:"json",
					  success: function(data){
						if (data.length != 0) {
							$("#oListboxFrom").empty();
							for (var i = 0; i < data.length; i++) {
							//alert("data[i].id"+data[i].id+"\tdata[i].ename"+data[i].bname);
 							$("#oListboxFrom").append("<option value=\""+data[i].id+"\">"+data[i].ename+"</option>");
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
	<form id="pointForm" method="post">
	 <input name="id" type="hidden" />
			<table width="530px" height="320px" class="tableForm" >
			<tr>	
				<th  style="width: 100px;align="right">设备名称：</th>
					<td colspan="3" ><input name="ename" id="ename" style="width:400px;height:20px;"/></td>
				 
			</tr>
			<tr>	
				<th style="width: 100px;">区县:</th>
				<td >
					<select  id="eregion"name="eregion" id="bregion" style="width:220px;height:20px;">
						<option value="">请选择</option>
					      <c:forEach items="${eList}" var="g">
				  		  <option value='<c:out value="${g.eregion}"/>'><c:out value="${g.eregion}"/></option>
				  	    </c:forEach>
					    </select>
					</td>
					<td align="right"colspan="2">
					<a href="javascript:void(0);" iconCls="icon-search" class="easyui-linkbutton" onclick="_search();">查询</a>&nbsp;&nbsp;
				
			</tr>
			<tr>
				<th align="right">巡检点名称：</th>
					<td><input name="poname" class="easyui-validatebox" required="true" missingMessage="名称不能为空!"  style="width:180px;height:20px;"/><font color="red">*</font></td>
					<th align="right">地址：</th>
					<td><input name="poaddress" style="width:180px;height:20px;"/></td>
			</tr>		
			<tr>
				<th align="right">经度：</th>
					<td><input name="poposx" style="width:180px;height:20px;"/>
				<th align="right">纬度：</th>
					<td><input name="poposy" style="width:180px;height:20px;"/></td>
			</tr>
				<tr>
			        <td colspan="4" align="center">
			        <table align="center">
			         <tr align="center">
			         <td>
			       <span style="color:red;align:left" >==可选巡检设备==</span>
				   <select name="oListboxFrom" id="oListboxFrom" size="10" multiple="true" style="width:150px;height:260px;margin-left:20px;float:left">
					  <c:forEach items="${EquipmentList}" var="equipment">
				  		<option value='<c:out value="${equipment.id}"/>'><c:out value="${equipment.ename}"/></option>
				  	</c:forEach>
				</select>
				</td>
				
				<td align="center" width="100">
					<input type="button" value=" >> " onclick="ListUtil.moveAll(oListboxFrom, poeIds);" style="width:80px;height:25px;float:center" />
					<br/><br/>
					<input type="button" value=" > " onclick="ListUtil.moveMuti(oListboxFrom, poeIds);" style="width:80px;height:25px;float:center"/>
					<br/><br/>
					<input type="button" value=" < " onclick="ListUtil.moveMuti(poeIds, oListboxFrom);" style="width:80px;height:25px;float:center"/>
					<br/><br/>
					<input type="button" value=" << " onclick="ListUtil.moveAll(poeIds, oListboxFrom);" style="width:80px;height:25px;float:center"/>
					<br/><br/>
                </td>
                <td>
				<span style="color:red;align:center" >==已选巡检设备==</span>
				<select name="poeIds" id="poeIds" multiple="multiple" size="10"	
					style="width:150px;height:260px;margin-left:20px;float:left">
					<c:forEach items="${pointSelectEquipmentList}" var="equipments">
				  		<option value='<c:out value="${equipments.id}"/>'><c:out value="${equipments.ename}"/></option>
				  	</c:forEach>
				</select>
			   </td>
		    </tr>
		    </table>
			  </td>
		    </tr>
		</table>
	</form>
</div>

