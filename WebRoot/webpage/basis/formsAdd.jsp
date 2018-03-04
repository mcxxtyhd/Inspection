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
	
	//加载地图页面
	function searchJW(){
	   $('#mapSearchId').show();   
	}
	</script>
<div style="padding: 5px;overflow: hidden;">
	<form id="stationForm" method="post">
			<table width="530px" height="320px" class="tableForm" >
				<tr>
					<th align="right">基站编号：</th>
					<td><input name="stnumber" class="easyui-validatebox" required="true" missingMessage="编号不能为空!"  style="width:180px;height:20px;"/><font color="red">*</font></td>
					<th align="right">经度：</th>
					<td><input name="stposx" style="width:180px;height:20px;"/>
				</tr>
				<tr>
					<th align="right">基站名称：</th>
					<td><input name="stname" class="easyui-validatebox" required="true" missingMessage="名称不能为空!"  style="width:180px;height:20px;"/><font color="red">*</font></td>
					<th align="right">纬度：</th>
					<td><input name="stposy" style="width:180px;height:20px;"/></td>
				</tr>
				<%--
				<div  id="mapSearchId" height="450px;"  style="display: none;">
					<IFRAME name= "iframe" src= "${pageContext.request.contextPath}/basis/searchmap.jsp"  width= "470px;" height= "350px;"  
						       marginwidth=0 marginheight=0 hspace=0 vspace=0 frameborder=0 scrolling=no id= "page2"> </IFRAME> 
				</div>
				--%>
				<tr>
				    <th align="right">天线挂高：</th>
					<td><input name="stantenna" style="width:180px;height:20px;"/></td>
					<th align="right">铁塔高度：</th>
					<td><input name="sttower" style="width:180px;height:20px;"/></td>
				</tr>
				
								<tr>
				    <th align="right">城市坐标：</th>
					<td><input name="staddress" style="width:180px;height:20px;"/></td>
					<th align="right">网络信息：</th>
					<td><input name="stnet" style="width:180px;height:20px;"/></td>
				</tr>
				
								<tr>
				    <th align="right">频率信息：</th>
					<td><input name="stfre" style="width:180px;height:20px;"/></td>
					<th align="right">共建共享：</th>
					<td><input name="stshare" style="width:180px;height:20px;"/></td>
				</tr>
				
								<tr>
				    <th align="right">手续办理：</th>
					<td><input name="stprocedure" style="width:180px;height:20px;"/></td>
					<th align="right">合法性：</th>
					<td><input name="stvalidaty" style="width:180px;height:20px;"/></td>
				</tr>
												<tr>
				    <th align="right">基站建设时间：</th>
					<td><input name="stdate" style="width:180px;height:20px;"/></td>
					
				</tr>
				
				
		</table>
			  </td>
		    </tr>
			</table>
	</form>
</div>