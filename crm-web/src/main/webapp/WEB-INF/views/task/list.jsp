<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 2017/11/14
  Time: 10:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-首页</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
<%@include file="../user/include/css.jsp"%>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
<%@include file="../user/include/header.jsp"%>

    <!-- =============================================== -->

    <!-- 左侧菜单栏 -->
<jsp:include page="../user/include/left.jsp">
    <jsp:param name="menu" value="task_list"/>
</jsp:include>
    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">待办事项</h3>

                    <div class="box-tools pull-right">
                        <a href="/task/new" class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新增任务</a>
                        <c:choose>
                            <c:when test="${not (param.show == 'all')}">
                                <a href="/task?show=all" class="btn btn-primary btn-sm"><i class="fa fa-eye"></i> 显示所有任务</a>
                            </c:when>
                            <c:otherwise>
                                <a href="/task" class="btn btn-primary btn-sm"><i class="fa fa-eye"></i> 显示未完成任务</a>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
                <div class="box-body">

                    <ul class="todo-list">
                        <c:forEach var="task" items="${taskList}">
                            <li id="" class="${task.status == '0' ? '' : 'done'}" >
                                <input type="checkbox" ${task.status == '0' ? '' : 'checked'}  value="${task.id}" class="task_checkbox">
                                <span class="text">${task.title}</span>
                                <a href=""><i class="fa fa-user-o"></i> 张三</a>
                                <small class="label ${task.isOverTime() ? 'label-danger' : 'label-success'}"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${task.finishTime}"/></small>
                                <div class="tools">

                                    <i class="fa fa-trash-o del_task" rel="${task.id}"></i>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

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

<%@include file="../user/include/js.jsp"%>
<script src="/static/plugins/layer/layer.js"></script>
<script>


    $(function () {

        $(".del_task").click(function () {
             var id = $(this).attr("rel");

             layer.confirm("确定要删除吗?", function () {
                 window.location.href="/task/" + id + "/del";
             })
        })

        $(".task_checkbox").click(function () {
            var id = $(this).val();

            var checked = $(this)[0].checked;

            if(checked) {

                window.location.href = "/task/"+id+"/state/done";
            } else {
                window.location.href = "/task/"+id+"/state/undone"
            }
        });
    });


</script>
</body>
</html>

