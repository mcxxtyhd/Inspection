<%@ page language="java" pageEncoding="UTF-8"%>
<div style="padding: 5px;overflow: hidden;">
	        <table width="300px" height="150px" class="tableForm" >
				<tr>
					<th align="right" width="40%">用户名称：</th>
					<td>${user.username}</td>
				</tr>
				<tr>
					<th align="right">真实姓名：</th>
					<td>${user.realname}</td>
				</tr>
				<tr>
					<th align="right">性别：</th>
					<td>${user.sex}</td>
				</tr>
				<tr>
					<th align="right">手机：</th>
					<td>${user.mobile}</td>
				</tr>
				<tr>
					<th align="right">邮箱：</th>
					<td>${user.email}</td>
				</tr>
				<tr>
					<th align="right">用户描述：</th>
					<td>${user.udesc}</td>
				</tr>
			</table>
</div>

