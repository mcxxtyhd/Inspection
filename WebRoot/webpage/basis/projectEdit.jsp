<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/tld.jsp" %>
  <script type="text/javascript" charset="utf-8">
  $(function(){
	  <c:if test="${ProjectBean ne null}">
	    <c:choose>
	     <c:when test="${ProjectBean.ptype eq '0'}">
	         $("#tr2").css("display","none");
	      //   $("#tr1").show();
	     </c:when>
	     <c:when test="${ProjectBean.ptype eq '1'}">
	    //    $("#tr1").css("display","none");
	        $("#tr2").show();
	     </c:when>
	     <c:otherwise>
	   //    $("#tr1").css("display","none");
	       $("#tr2").css("display","none");
	     </c:otherwise>
	     </c:choose>
	  </c:if>
  });
   function isOK(result){ 
	  if(result==0){
	    $("#tr2").css("display","none");
	  //  $("#tr1").show();
	  }
	  if(result==1){
	  // $("#tr1").css("display","none");
	   $("#tr2").show();
	  }
	  if(result==2){
	 //   $("#tr1").css("display","none");
	    $("#tr2").css("display","none");
	  }
   }
  </SCRIPT>
<div style="padding: 5px;overflow: hidden;">
	<form id="projectForm" method="post">
	 <input name="id" type="hidden" />
			<table width="420px" height="220px" class="tableForm" >
			    <tr>
					<th align="right">大类名称：</th>
					<td>
					    <select name="pgroupid" style="width:220px;height:20px;" class="easyui-validatebox" required="true" missingMessage="大类名称不能为空!">
				        <option value="">请选择</option>
				        <c:forEach items="${ProjectGroupList}" var="pgroup">
				  		 <option value='<c:out value="${pgroup.id}"/>'><c:out value="${pgroup.pgname}"/></option>
				  	    </c:forEach>
				      </select><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">小类名称：</th>
					<td><input name="pname" class="easyui-validatebox" readonly="readonly" equired="true" missingMessage="小类名称不能为空!"  style="width:220px;height:20px;"/><font color="red">*</font></td>
				</tr>
				<tr>
					<th align="right">数据类型：</th>
					<td>
					 <select name="ptype" id="ptype" onchange="isOK(this.value);" class="easyui-validatebox" required="true" missingMessage="数据类型不能为空!" style="width:220px;height:20px;">
					   <option value="0">数字</option>
					   <option value="1">枚举</option>
					   <option value="2">字符串</option>
					 </select>
					<font color="red">*</font></td>
				</tr> 
				<tr id="tr1">
				 <th align="right">数据范围：</th>
				 <td>
				    <input name="pminvalue" style="width:140px;height:20px;"/>-至 -
				    <input name="pmaxvalue" style="width:140px;height:20px;"/>
				 </td>
				</tr>
				 <tr id="tr2" style="display: none;">
				 <th align="right">关联枚举值：</th>
				 <td>
				     <select name="penumvalue" style="width:220px;height:20px;">
				        <option value="">请选择</option>
				        <c:forEach items="${NumerTypeList}" var="numer">
				  		 <option value='<c:out value="${numer.pname}"/>'><c:out value="${numer.pname}"/></option>
				  	    </c:forEach>
				      </select><font color="red">*</font>
				 </td>
				</tr>
				<tr>
					<th align="right">描述：</th>
					<td>
					   <textarea name="pdesc" style="height: 60px;width:320px;"></textarea>
					</td>
				</tr>
			</table>
	</form>
</div>