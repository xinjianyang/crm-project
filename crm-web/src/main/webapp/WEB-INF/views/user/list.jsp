<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 2017/11/6
  Time: 14:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

<%@include file="include/css.jsp"%>
    <link rel="stylesheet" href="/static/plugins/datatables/jquery.dataTables.css">


</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
    
    <!-- 顶部导航栏部分 -->
<%@include file="include/header.jsp"%>

    <!-- =============================================== -->
<%--左侧导航栏--%>
<jsp:include page="include/left.jsp">
    <jsp:param name="menu" value="user"/>
</jsp:include>
    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-md-2">
                    <div class="box">
                        <div class="box-body">
                            <button class="btn btn-default" id="addDept">添加部门</button>
                            <input type="hidden" class="deptId">

                            <ul id="ztree" class="ztree"></ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-10">
                    <!-- Default box -->
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">员工管理</h3>
                            <c:if test="${not empty message}">
                                <div class="alert-info">${message}</div>
                            </c:if>
                            <div class="box-tools pull-right">
                                <button type="button" class="btn btn-box-tool"  title="Collapse">
                                    <i class="fa fa-plus"></i> <a href="/user">添加员工</a></button>
                            </div>
                        </div>
                        <div class="box-body">
                            <table class="table" id="dataTable">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>姓名</th>
                                        <th>部门</th>
                                        <th>手机</th>
                                        <th>#</th>
                                    </tr>
                                </thead>

                            </table>
                            <%--<ul id="pagination-demo" class="pagination-sm"></ul>--%>
                        </div>
                    </div>
                    <!-- /.box -->
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->


    <%--添加账号模态框--%>
    <!-- Modal -->
    <div class="modal fade" id="addEmployeeModel" >
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">添加账号</h4>
                </div>
                <div class="modal-body">
                    <form id="addEmployeeForm">
                        <div class="form-group">
                            <label>姓名</label>
                            <input type="text" class="form-control" name="userName">
                        </div>
                        <div class="form-group">
                            <label>手机号码</label>
                            <input type="text" class="form-control" name="mobile">
                        </div>
                        <div class="form-group">
                            <label>密码(默认000000)</label>
                            <input type="password" class="form-control" name="password" value="000000">
                        </div>
                        <div class="form-group">
                            <label>所属部门</label>
                            <div id="checkboxList"></div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="addEmployeeFormBtn">保存</button>
                </div>
            </div>
        </div>
    </div>
    <%--添加账号模态框结束--%>


    <!-- 底部 -->
    <footer class="main-footer">
        <div class="pull-right hidden-xs">
            <b>Version</b> 1.0
        </div>
        <strong>Copyright &copy; 2010 -2017 <a href="http://almsaeedstudio.com">凯盛软件</a>.</strong> All rights
        reserved.
    </footer>

</div>
<!-- ./wrapper -->
<%@include file="include/js.jsp"%>
<script src="/static/plugins/tree/js/jquery.ztree.all.min.js"></script>
<script src="/static/plugins/layer/layer.js"></script>
<script src="/static/plugins/datatables/jquery.dataTables.js"></script>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script src="/static/js/jquery.twbsPagination.js"></script>
<script>
    $(function(){

        var dataTable = $("#dataTable").DataTable({
            "processing": true,
            "serverSide": true,
            "ajax": {
                "url" : "/user/load.json",
                "data" : function(data){
                    data.deptId = $("#deptId").val();
                }
            },
            "lengthChange": false,
            "pageLength": 25,
            "columns":[
                {"data":"id"},
                {"data":"userName"},
                {"data":function(row){
                    var deptArray = row.deptList;
                    var str = "";
                    for(var i = 0;i < deptArray.length;i++) {
                        str += deptArray[i].deptName + " ";
                    }
                    return str;
                }},
                {"data":"mobile"},
                {"data":function(row){
                    return "<a href='javascript:;' rel='"+row.id+"' class='delEmployeeLink'>删除</a>";
                }}
            ],
            "columnDefs": [
                {
                    "targets": [2,3,4],
                    "orderable": false
                },
                {
                    "targets": [0],
                    "visible": false
                }
            ],
            language:{
                "search":"账号:",
                "info": "显示从 _START_ 到 _END_ 条数据，共 _TOTAL_ 条",
                "infoEmpty":"没有任何数据",
                "emptyTable":"暂无数据",
                "processing":"加载中...",
                "paginate": {
                    "first":      "首页",
                    "last":       "末页",
                    "next":       "上一页",
                    "previous":   "下一页"
                }
            }
        });


        //添加员工





        /*$('#pagination-demo').twbsPagination({
            totalPages: ${pageInfo.pages},
            visiblePages: 10,
            first:'首页',
            last:'末页',
            prev:'上一页',
            next:'下一页',
            href:"?p={{number}}"
        });*/

        $("#addDept").click(function () {
            layer.prompt({title:"请输入部门"},function (text,index) {
                layer.close(index);

                $.post("/user/dept/new",{"deptName":text}).done(function (data) {

                    if(data.state == "success"){
                        layer.msg("添加部门成功");
                        var treeObj = $.fn.zTree.getZTreeObj("ztree");
                        treeObj.reAsyncChildNodes(null, "refresh");
                    }else{
                        layer.msg(data.message);
                    }
                }).error(function () {
                    layer.msg("服务器忙,请稍后再试");
                });
            });
        });

        var setting = {
            data: {
                simpleData: {
                    enable: true
                },
                key:{
                    name:"deptName"
                }
            },
            async:{
                enable:true,
                url:"/user/dept.json",
                type:"get",
                dataFilter:ajaxDataFilter
            },
            callback:{
                onClick:function(event,treeId,treeNode,clickFlag){
                    //alert(treeNode.id + treeNode.deptName + treeNode.pId);

                    $("#deptId").val(treeNode.id);
                    dataTable.ajax.reload();
                }
            }
        };

        function ajaxDataFilter(treeId, parentNode, responseData) {
            if (responseData) {
                for(var i =0; i < responseData.length; i++) {
                    if(responseData[i].id == 1) {
                        responseData[i].open = true;
                        break;
                    }
                }
            }
            return responseData;
        }

        $.fn.zTree.init($("#ztree"), setting);



        /*
         $(".tbody").html("");

         $.get("/finduser/bydept",{"deptId" : treeNode.id}).done(function (json) {
         for(var i = 1; i < json.list.length; i++){
         var user = json.list[i];
         if(user){
         console.log(user);
         var html = "<td>${user.userName}</td><td><c:forEach items="${user.deptList}" var="dept">${dept.deptName}&nbsp</c:forEach></td><td>${user.mobile}</td><td><a href='javaScript:;'>禁用</a><a href='javaScript:;'>删除</a><a href='javaScript:;'>编辑</a></td>";
         $(".tbody").append(html);

         }
         }

         layer.msg("请求成功");

         }).error(function () {
         layer.msg("服务器异常,请联系管理员");
         });
         */


    });
</script>
</body>
</html>

