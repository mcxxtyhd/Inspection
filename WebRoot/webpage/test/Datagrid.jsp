<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<script type="text/javascript" charst="utf-8">var editFlag = undefined;//设置一个编辑标记
//因为layout框架指向href时，只取html页面body中间的部分，所以该页面这样写即可
//有datagrid包含属性较多，所以尽量使用js的方式初始化datagrid框架
$(function () {
$("#dg").datagrid({
url: "GetJson.ashx", //指向一个一般处理程序或者一个控制器，返回数据要求是Json格式，直接赋值Json格式数据也可，我以demo中自带的Json数据为例，就不写后台代码了，但是我会说下后台返回的注意事项
iconCls: "icon-add",
fitColumns: false, //设置为true将自动使列适应表格宽度以防止出现水平滚动,false则自动匹配大小
//toolbar设置表格顶部的工具栏，以数组形式设置
idField: 'id', //标识列，一般设为id，可能会区分大小写，大家注意一下
loadMsg: "正在努力为您加载数据", //加载数据时向用户展示的语句
pagination: true, //显示最下端的分页工具栏
rownumbers: true, //显示行数 1，2，3，4...
pageSize: 10, //读取分页条数，即向后台读取数据时传过去的值
pageList: [10, 20, 30], //可以调整每页显示的数据，即调整pageSize每次向后台请求数据时的数据
//由于datagrid的属性过多，我就不每个都介绍了，如有需要，可以看它的API
sortName: "name", //初始化表格时依据的排序 字段 必须和数据库中的字段名称相同
sortOrder: "asc", //正序
columns: [[
{
field: 'code', title: 'Code', width: 100,
editor: {//设置其为可编辑
type: 'validatebox',//设置编辑样式 自带样式有：text，textarea，checkbox，numberbox，validatebox，datebox，combobox，combotree 可自行扩展
options: {}
}
},
{
field: 'name', title: 'Name', width: 100, sortable: true,
editor: {//设置其为可编辑
type: 'validatebox',//设置编辑格式
options: {
required: true//设置编辑规则属性
}
}
},//sortable:true点击该列的时候可以改变升降序
{
field: 'addr', title: 'addr', width: 100,
editor: {//设置其为可编辑
type: 'datetimebox',//这里我们将进行一个datetimebox的扩展
options: {
required: true//设置编辑规则属性
}
}
}
]],//这里之所以有两个方括号，是因为可以做成水晶报表形式，具体可看demo
toolbar: [{//在dategrid表单的头部添加按钮
text: "添加",
iconCls: "icon-add",
handler: function () {
if (editFlag != undefined) {
$("#dg").datagrid('endEdit', editFlag);//结束编辑，传入之前编辑的行
}
if (editFlag == undefined) {//防止同时打开过多添加行
$("#dg").datagrid('insertRow', {//在指定行添加数据，appendRow是在最后一行添加数据
index: 0, // 行数从0开始计算
row: {
code: '',
name: '请输入姓名',
addr: ''
}
});
$("#dg").datagrid('beginEdit', 0);//开启编辑并传入要编辑的行
editFlag = 0;
}
}
}, '-', {//'-'就是在两个按钮的中间加一个竖线分割，看着舒服
text: "删除",
iconCls: "icon-remove",
handler: function () {
//选中要删除的行
var rows = $("#dg").datagrid('getSelections');
if (rows.length > 0) {//选中几行的话触发事件
$.message.confirm("提示", "您确定要删除这些数据吗？", function (res) {//提示是否删除
if (res) {
var codes = {};
for (var i = 0; i < rows.length; i++) {
codes.push(rows[i].code);
}
console.info(codes.join(','));//拼接字符串并传递到后台处理数据，循环删除，成功后刷新datagrid
}
});
}
}
}, '-', {
text: "修改",
iconCls: "icon-edit",
handler: function () {
//选中一行进行编辑
var rows = $("#dg").datagrid('getSelections');
if (rows.length == 1) {//选中一行的话触发事件
if (editFlag != undefined) {
$("#dg").datagrid('endEdit', editFlag);//结束编辑，传入之前编辑的行
}
if (editFlag == undefined) {
var index = $("#dg").datagrid('getRowIndex', rows[0]);//获取选定行的索引
$("#dg").datagrid('beginEdit', index);//开启编辑并传入要编辑的行
editFlag = index;
}
}
}
}, '-', {
text: "保存",
iconCls: "icon-save",
handler: function () {
$("#dg").datagrid('endEdit', editFlag);
}
}, '-', {
text: "撤销",
iconCls: "icon-redo",
handler: function () {
editFlag = undefined;
$("#dg").datagrid('rejectChanges');
}
}, '-'],
onAfterEdit: function (rowIndex, rowData, changes) {//在添加完毕endEdit，保存时触发
console.info(rowData);//在火狐浏览器的控制台下可看到传递到后台的数据，这里我们就可以利用这些数据异步到后台添加，添加完成后，刷新datagrid
editFlag = undefined;//重置
}, onDblClickCell: function (rowIndex, field, value) {//双击该行修改内容
if (editFlag != undefined) {
$("#dg").datagrid('endEdit', editFlag);//结束编辑，传入之前编辑的行
}
if (editFlag == undefined) {
$("#dg").datagrid('beginEdit', rowIndex);//开启编辑并传入要编辑的行
editFlag = rowIndex;
}
}
});
});

//点击查找按钮出发事件
function searchFunc() {
alert("123");
$("#dg").datagrid("load", sy.serializeObject($("#searchForm").form()));//将searchForm表单内的元素序列为对象传递到后台
//这里介绍reload的使用，使用reload时，会默认记住当前页面，当点击查询时，如果我们查到的数据只有三条，我们每页显示10挑数据，当前页码是2，那么我们将无法在当前页面看到我们查询出的结果，只有将页面向前跳转才会看到，但是用load就不会出现这种情况
}

//点击清空按钮出发事件
function clearSearch() {
$("#dg").datagrid("load", {});//重新加载数据，无填写数据，向后台传递值则为空
$("#searchForm").find("input").val("");//找到form表单下的所有input标签并清空
}
</script>
<div class="easyui-tabs" fit="true" border="false">
<div title="数据展示表格" border="false" fit="true">
<div class="easyui-layout" fit="true" border="false">
<!--由于查询需要输入条件，但是以toolbar的形式不好，所以我们在Layout框架的头部north中书写查询的相关信息-->
<!-- 这里我们尽量使其展示的样式与toolbar的样式相似，所以我们先查找toolbar的样式，并复制过来-->
<div data-options="region:'north',title:'高级查询'" style="height: 100px; background: #F4F4F4;">
<form id="searchForm">
<table>
<tr>
<th>用户姓名：</th>
<td>
<input name="name" /></td>
</tr>
<tr>
<th>创建开始时间</th>
<td>
<input class="easyui-datetimebox" editable="false" name="subStartTime" /></td>
<!--由于datebox框架上面的数据必须是时间格式的，所以我们用editable="false"来禁止用户手动输入，以免报错-->
<th>创建结束时间</th>
<td>
<input class="easyui-datetimebox" editable="false" name="nsubEndTimeame" /></td>
<td><a class="easyui-linkbutton" href="javascript:void(0);" onclick="searchFunc();">查找</a></td>
<td><a class="easyui-linkbutton" href="javascript:void(0);" onclick="clearSearch();">清空</a></td>
</tr>
</table>
</form>
</div>
<div data-options="region:'center',split:false">
<table id="dg">
</table>
</div>
</div>
</div>
</div>

</html>
