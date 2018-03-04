<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<script type="text/javascript" charst="utf-8">

$(function() {
  datagrid = $('#datagrid').datagrid({
   url : '请求路径',
   toolbar : '#toolbar',//工具条
   singleSelect:false,//是否是单选  false是多选
   sortName:'id',//默认根据什么排序
   sortOrder:'asc',//升序
   iconCls : 'icon-save',//显示的图标
   remoteSort:true,//远程获取数据
   pagination : true,//显示分页条
   pageSize : 10,//每页条数
   pageList : [ 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 ],
   fit : true,//充满表格
   fitColumns : true,//列自动充满
   nowrap : false,//是否不换行
   border : false,//是否显示边框
   idField : 'id',//主键
   frozenColumns : [ [ {
    title : 'id',//列头
    field : 'id',//对应的字段
    width : 50,//宽度
    checkbox : true
   }, {
    field : 'name',
    title : '材料名称',
    width : 100,
    sortable : true
   } ] ],
   columns : [ [{
    field : 'descri',
    title : '材料描述',
    width : 200,
    sortable : false
   },{
    field : 'addDate',
    title : '添加时间',
    width : 100,
    sortable : false
   }] ],
   //右击菜单
   onRowContextMenu : function(e, rowIndex, rowData) {
    e.preventDefault();
    $(this).datagrid('unselectAll');
    $(this).datagrid('selectRow', rowIndex);
    $('#menu').menu('show', {
     left : e.pageX,
     top : e.pageY
    });
   }
  });
 });


function append() {
  /*visamaterialDialog.dialog({
   href:'visamaterial-save.action'
  });*/
  visamaterialDialog.dialog('open');
  visamaterialForm.form('clear');
 }


 function edit() {
  var rows = datagrid.datagrid('getSelections');
  if (rows.length != 1 && rows.length != 0) {
   var names = [];
   for ( var i = 0; i < rows.length; i++) {
    names.push(rows[i].name);
   }
   $.messager.show({
    msg : '只能择一项进行编辑！您已经选择了【' + names.join(',') + '】' + rows.length + '个材料',
    title : '提示'
   });
  } else if (rows.length == 1) {
   visamaterialDialog.dialog('open');
   visamaterialForm.form('clear');
   visamaterialForm.form('load', {
    id : rows[0].id,
    name : rows[0].name,
    mount : rows[0].mount,
    filename : rows[0].fileName,
    descri : rows[0].descri
   });
   /*$.ajax( {
    url : "visamaterial!toUpdate.action",
    data:"id="+rows[0].id,
    success : function(r) {
     console.info(r);
     visamaterialDialog.dialog( {
 
      width : 500,
      height : 300,
      href : 'visamaterial-update.action'
     });
     visamaterialDialog.dialog('open');
    }
   });*/
  }
 }


 function remove() {
  var id = [];
  var rows = datagrid.datagrid('getSelections');
  if (rows.length > 0) {
   $.messager.confirm('请确认', '您要删除当前所选材料？', function(r) {
    if (r) {
     for ( var i = 0; i < rows.length; i++) {
      id.push(rows[i].id);
     }
     $.ajax( {
      url : 'visamaterial!deleteBatch.action',
      data : {
       ids : id.join(',')
      },
      cache : false,
      dataType : "json",
      success : function(response) {
       datagrid.datagrid('unselectAll');
       datagrid.datagrid('load');
       $.messager.show( {
        title : '提示',
        msg : '删除成功！'
       });
      }
     });
    }
   });
  } else {
   $.messager.alert('提示', '请选择要删除的记录！', 'error');
  }
 }

 </script>
 
 <html>
 <div region="center">
  <div id="toolbar" class="datagrid-toolbar" style="height: auto; display: none;">
   <a class="easyui-linkbutton" iconCls="icon-add" onclick="append();" plain="true" href="javascript:void(0);">增加</a> 
   <a class="easyui-linkbutton" iconCls="icon-edit" onclick="edit();" plain="true" href="javascript:void(0);">编辑</a> 
   <a class="easyui-linkbutton" iconCls="icon-remove" onclick="remove();" plain="true" href="javascript:void(0);">删除</a> 
   <a class="easyui-linkbutton" iconCls="icon-reload" onclick="datagrid.datagrid('reload');" plain="true" href="javascript:void(0);">刷新</a> 
  </div>
  <table id="datagrid"></table>
 </div>

</html>
