<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 2017/11/13
  Time: 19:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-新增销售机会</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@include file="../user/include/css.jsp"%>
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
                        <button type="button" class="btn btn-box-tool">
                            <i class="fa fa-plus"></i> 添加机会
                        </button>
                    </div>
                </div>
                <div class="box-body">
                    <form method="post" id="addSaleChanceForm">
                        <div class="form-group">
                            <label>机会名称</label>
                            <input type="text" class="form-control" name="housenum">
                        </div>
                        <div class="form-group">
                            <label>关联客户</label>
                            <select name="customerId" id="" class="form-control">
                                <option value=""></option>
                                <c:forEach items="${customerList}" var="customer">
                                    <option value="${customer.id}" name="">${customer.cusName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>机会价值</label>
                            <input type="text" class="form-control" name="price">
                        </div>
                        <div class="form-group">
                            <label>当前进度</label>
                            <select name="progress" class="form-control">
                                <c:forEach var="progress" items="${progressList}">
                                    <option value="${progress}">${progress}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>详细内容</label>
                            <textarea name="content" class="form-control"></textarea>
                        </div>
                    </form>
                </div>
                <!-- /.box-body -->
                <div class="box-footer">
                    <button class="btn btn-primary" id="addBtn">保存</button>
                </div>
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
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script>

    $(function () {

        $("#addBtn").click(function () {
            $("#addSaleChanceForm").submit();
        });

        $("#addSaleChanceForm").validate({
            errorClass : "text-danger",
            errorElement : "span",

            rules :{
                housenum : {
                    required : true
                },
                customerId : {
                    required : true
                },
                price : {
                    required : true
                }

            },

            messages :{
                housenum : {
                    required : "请输入产品名称"
                },
                customerId : {
                    required : "请选择对应客户"
                },
                price : {
                    required : "请输入价格"
                }


            }


        });

    });

</script>
</body>
</html>

