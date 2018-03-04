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
	</script>
<div style="padding: 5px;overflow: hidden;">
	<form id="equipmentForm" method="post">
	    <input name="id" type="hidden" />
			<table width="450px" height="410px" class="tableForm" >
				<tr>
					<th align="right">设备编号：</th>
					<td><input name="enumber" style="width:320px;height:20px;" readonly="readonly"/></td>
				</tr>
				<tr>
					<th align="right">设备名称：</th>
					<td><input name="ename" style="width:320px;height:20px;" readonly="readonly"/></td>
				</tr>
				<%--<tr>
					 <th align="right">设备类型：</th>
					<td><input name="etype" style="width:180px;height:20px;"/></td>
				    <th align="right">设备厂家：</th>
					<td><input name="efactory" style="width:180px;height:20px;"/></td>
				</tr>
				<tr>
					<th align="right">经度：</th>
					<td><input name="eposx" style="width:180px;height:20px;"/></td>
					<th align="right">二维码标识：</th>
					<td>
					   <select name="etwocodeid" id="etwocodeid" style="width:180px;">
				        <option value="">请选择</option>
					    <c:forEach items="${TwoList}" var="code">
				  		  <option value='<c:out value="${code.id}"/>'><c:out value="${code.cname}"/></option>
				  	    </c:forEach>
				      </select>
					
					</td>
				</tr>
				<tr>
					<th align="right">纬度：</th>
					<td><input name="eposy" style="width:180px;height:20px;"/></td>
					<th align="right">RFID标识：</th>
					<td>
					  <select name="erfid" id="erfid" style="width:180px;">
				        <option value="">请选择</option>
					    <c:forEach items="${RfidList}" var="rfid">
				  		  <option value='<c:out value="${rfid.id}"/>'><c:out value="${rfid.rname}"/></option>
				  	    </c:forEach>
				      </select>
					</td>
					
				</tr>
				<tr>
				    <th align="right">地址：</th>
					<td><input name="eaddress" style="width:180px;height:20px;"/></td>
					<th align="right">描述：</th>
					<td><input name="sdesc" style="width:180px;height:20px;"/></td>
					
				</tr>
			
			--%><tr>
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
						<c:forEach items="${EquipmentSelectProjectList}" var="projects">
				  			<option value='<c:out value="${projects.id}"/>'><c:out value="${projects.pgname}"/></option>
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