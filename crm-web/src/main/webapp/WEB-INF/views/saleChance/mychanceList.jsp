<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 2017/11/13
  Time: 11:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-销售机会</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

    <%@include file="../user/include/css.jsp"%>
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
        <jsp:param name="menu" value="saleChance_my"/>
    </jsp:include>
    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">我的销售机会</h3>

                    <div class="box-tools pull-right">
                        <a href="/saleChance/new" type="button" class="btn btn-box-tool">
                            <i class="fa fa-plus"></i> 添加机会
                        </a>
                    </div>
                </div>
                <div class="box-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <td>机会名称</td>
                            <td>关联客户</td>
                            <td>机会价值</td>
                            <td>当前进度</td>
                            <td>最后跟进时间</td>
                            <td>
                                #
                            </td>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="saleChance" items="${pageInfo.list}">
                            <tr>
                                <td><a href="/saleChance/MyDetail/${saleChance.id}" >${saleChance.housenum}</a></td>
                                <td>${saleChance.customer.cusName}</td>
                                <td>￥${saleChance.price}</td>
                                <td>${saleChance.progress}</td>
                                <td><fmt:formatDate value="${saleChance.customer.updatetime}"/></td>
                                <td></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <ul id="pagination-demo" class="pagination-sm"></ul>
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

<!-- jQuery 2.2.3 -->
<%@include file="../user/include/js.jsp"%>
<script src="/static/plugins/jquery.twbsPagination.js"></script>>

<script>
    $(function(){
        //分页
        $('#pagination-demo').twbsPagination({
            totalPages: ${pageInfo.pages},
            visiblePages: 10,
            first:'首页',
            last:'末页',
            prev:'上一页',
            next:'下一页',
            href:"p={{number}}"
        });
    });
</script>
</body>
</html>
