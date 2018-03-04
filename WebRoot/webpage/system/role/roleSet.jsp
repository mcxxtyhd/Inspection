<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
	$(function() {
		//系统权限加载
		$('#functionid').tree({
			checkbox : true,
			url : '${pageContext.request.contextPath}/system/roleAction!setAuthority.action?roleId=${roleId}',
			onLoadSuccess : function(node) {
				expandAll();
			}
		});
	});
	//系统权限设置保存
	function mysubmit() {
		var roleId = $("#rid").val();
		var s = GetNode();
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			url:'${pageContext.request.contextPath}/system/roleAction!updateAuthority.action',
	        data:"rolefunctions="+ s +"&roleId="+roleId,
			error : function() {// 请求失败处理函数
			},
			success : function(data) {
				var d = $.parseJSON(data);
				if (d.success) {}
				parent.sy.messagerShow({
				   msg : d.msg,
				   title : '提示'
				   });
			}
	  });
	}
	//获取选择系统权限菜单
	function GetNode() {
		var node = $('#functionid').tree('getChecked');
		var cnodes = '';
		var pnodes = '';
		var prevNode = ''; //保存上一步所选父节点
		for ( var i = 0; i < node.length; i++) {
			if ($('#functionid').tree('isLeaf', node[i].target)) {
				cnodes += node[i].id + ',';
				var pnode = $('#functionid').tree('getParent', node[i].target); //获取当前节点的父节点
				if (prevNode != pnode.id) //保证当前父节点与上一次父节点不同
				{
					pnodes += pnode.id + ',';
					prevNode = pnode.id; //保存当前节点
				}
			}
		}
		cnodes = cnodes.substring(0, cnodes.length - 1);
		pnodes = pnodes.substring(0, pnodes.length - 1);
		return cnodes + "," + pnodes;
	};
	
	//展开节点
	function expandAll() {
		var node = $('#functionid').tree('getSelected');
		if (node) {
			$('#functionid').tree('expandAll', node.target);
		} else {
			$('#functionid').tree('expandAll');
		}
	}
	//节点全选
	function selecrAll() {
		var node = $('#functionid').tree('getRoots');
		for ( var i = 0; i < node.length; i++) {
			var childrenNode =  $('#functionid').tree('getChildren',node[i].target);
			for ( var j = 0; j < childrenNode.length; j++) {
				$('#functionid').tree("check",childrenNode[j].target);
			}
	    }
	}
	//重置节点
	function reset() {
		$('#functionid').tree('reload');
	}

	$('#selecrAllBtn').linkbutton({}); 
	$('#resetBtn').linkbutton({});   
</script>
  <input type="hidden" name="roleId" value="${roleId}" id="rid">
  <a id = "selecrAllBtn" onclick="selecrAll();">全选</a>
  <a id = "resetBtn"  onclick="reset();">重置</a>
  <ul id="functionid"></ul>

