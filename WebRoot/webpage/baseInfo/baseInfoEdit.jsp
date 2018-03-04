<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
  <script type="text/javascript" charset="utf-8">
  $(function(){
	  <c:if test="${BaseInfoBean ne null}">
	    <c:choose>
	     <c:when test="${BaseInfoBean.btype eq '1'}">
	        $("#tieta_1").show();
	        $("#tieta_2").show();
	        $("#tieta_3").show();
	        
	     </c:when>
	     <c:otherwise>
	       $("#shifen_1").show();
		   $("#shifen_2").show();
	     </c:otherwise>
	     </c:choose>
	  </c:if>
  });
  </SCRIPT>
<div style="padding: 5px;overflow: hidden;">
	<form id="baseinfoForm" method="post">
	       <input type="hidden" name="id" >
	       <table width="400px" height="330px" class="tableForm" >
	       		<tr>
				  <th width="9%">设备类型：</th>
				  <td width="8%">
				     <select name="btype" disabled="disabled" style="width:220px;height:20px;">
				        <option value="">请选择</option>
				        <option value="1">铁塔、天馈线</option>
				        <option value="2">室内分布及WLAN</option>
				      </select>
				  </td>
				</tr>
					<th align="right">基站名称：</th>
					<td><input name="bname"  style="width:220px;height:20px;" class="easyui-validatebox" required="true" missingMessage="基站名称不能为空!"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">基站编号：</th>
					<td><input name="bnumber"  readonly="readonly" style="width:220px;height:20px;"/></td>
				</tr>
				<tr>
				    <th align="right">地市：</th>
					<td><input name="bcity"  style="width:220px;height:20px;"class="easyui-validatebox" required="true" missingMessage="城市不能为空!"/><font color="red">*</font></td>
				</tr>
					<tr>
				    <th align="right">区县：</th>
					<td><input name="bregion"  style="width:220px;height:20px;"class="easyui-validatebox" required="true" missingMessage="区县不能为空!"/><font color="red">*</font></td>
					</tr>
				<tr>
				<tr id="tieta_3" style="display: none;">
				    <th align="right">塔高：</th>
					<td><input name="bheight"  style="width:220px;height:20px;"/></td>
				</tr>
			
					<th align="right">地址：</th>
					
					<td><input name="baddress"  style="width:220px;height:20px;"/></td>
				</tr>
			
				<tr>
					<th align="right">经度：</th>
					<td><input name="bposx"  style="width:220px;height:20px;"/></td>
					</tr>
				<tr>
					<th align="right">纬度：</th>
					<td><input name="bposy" style="width:220px;height:20px;"/></td>
				</tr>
				<tr id="tieta_1" style="display: none;">
					<th align="right">铁塔类型：</th>
					<td><input name="btowertype" style="width:220px;height:20px;" /></td>
					</tr>
				<tr id="tieta_2" style="display: none;">
					<th align="right">自建(共享)塔：</th>
					<td><input name="btower" style="width:220px;height:20px;"/></td>
				</tr>
					<tr>
					<th align="right">厂家：</th>
					<td><input name="bfactory" style="width:220px;height:20px;"/></td>
				</tr>
				<tr id="shifen_1" style="display: none;">
					<th align="right">维护等级：</th>
					<td><input name="blevel" style="width:220px;height:20px;" /></td>
					</tr>
				<tr id="shifen_2" style="display: none;">
					<th align="right">有源设备数量：</th>
					<td><input name="beqcount" style="width:220px;height:20px;"/></td>
				</tr>
				
				<tr>
					<th align="right">描述：</th>
					<td><textarea name="bdesc" style="height: 30px;width:320px;"></textarea></td>
				</tr>
			</table>
	</form>
</div>
