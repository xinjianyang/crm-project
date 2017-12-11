<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 2017/11/16
  Time: 10:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-公司网盘</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <%@include file="../user/include/css.jsp"%>

    <link rel="stylesheet" href="/static/plugins/webuploader/webuploader.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style>
        tr{
            height: 50px;
            line-height: 50px;
        }
        .table>tbody>tr>td{
            vertical-align: middle;
        }
        .file_icon {
            font-size: 30px;
            text-align: center;
        }
        .table>tbody>tr:hover{
            cursor: pointer;
        }
        .webuploader-container {
            display: inline-block;
        }
        .webuploader-pick {
            padding: 5px 10px;
            overflow: visible;
            font-size: 12px;
            line-height:1.5;
            font-weight: 400;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../user/include/header.jsp"%>

    <!-- =============================================== -->

    <!-- 左侧菜单栏 -->
<jsp:include page="../user/include/left.jsp">
    <jsp:param name="menu" value="disk_home"/>
</jsp:include>
    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">公司网盘</h3>

                    <div class="box-tools pull-right">
                        <input id="currentId" type="hidden" value="<shiro:principal property="id"/> ">
                        <div id="picker" class="btn btn-success btn-sm"><i class="fa fa-upload"></i> 上传文件</div>
                        <button id="newFolder" class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新建文件夹</button>
                    </div>
                </div>
                <div class="box-body no-padding">

                    <table class="table table-hover">
                        <c:forEach items="${diskList}" var="disk">
                        <tr class="tr" rel="${disk.id}">
                            <td width="50" class="file_icon"><i class="fa fa-folder-o"></i></td>
                            <td>${disk.name}</td>
                            <td><fmt:formatDate value="${disk.updateTime}"/></td>
                            <td width="100"></td>
                            <td width="150">
                                <div class="btn-group">
                                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                        <i class="fa fa-ellipsis-h"></i>
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li><a href="">打开</a></li>
                                        <li><a href="#">重命名</a></li>
                                        <li><button id="delbtn">删除</button></li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                        </c:forEach>
                    </table>


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
<script src="/static/plugins/moment/moment.js"></script>
<script src="/static/plugins/art-template/art-template.js"></script>
<script src="/static/plugins/webuploader/webuploader.min.js"></script>

<script type="text/template" id="trTemplate">
    <tr class="tr" rel="{{id}}">
        <td width="80" class="file_icon">
            <?if(type == 'file') { ?>
            <i class="fa fa-file-o"></i>
            <? } else if(type == 'dir') { ?>
            <i class="fa fa-folder-o"></i>
                <? } ?>
        </td>
        <td>{{name}}</td>
        <td>{{updateTime}}</td>
        <td width="100">{{fileSize}}</td>
        <td width="100">
            <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                    <i class="fa fa-ellipsis-h"></i>
                </button>
                <ul class="dropdown-menu">
                    <li><a href="">下载</a></li>
                    <li><a href="">重命名</a></li>
                    <li><a href="">删除</a></li>
                </ul>
            </div>
        </td>
    </tr>

</script>
<script>

    $(function () {

        var pid = ${not empty requestScope.disk ? requestScope.disk.id : '0'};
        var userId = 2;
        
        
        
        
        $("#newFolder").click(function () {
           layer.prompt({title:"请输入文件夹名称"},function (text,index) {
               layer.close(index);
               $.post("/disk/new/folder",{"pId":pid,"name":text,"userId":userId}).done(function (res){
                   if(res.state == 'success'){
                       layer.msg("创建成功");

                   }
               }).error(function () {
                   layer.msg("服务器异常");
               })
           });
        });

        //删除当前用户上传的公司网盘资料
       /*
        $("#delbtn").click(function () {
           $.get("disk/del/foder",{"id":${disk.id}}).done(function (res) {
               if(res.state == 'success'){
                   layer.msg("删除成功");
               }
           }).error(function () {
              layer.msg("服务器异常,稍后重试"); 
           });
        });*/

        //行点击事件
        $(document).delegate(".tr","click",function () {
           var id = $(this).attr("rel");
           window.location.href="/disk/list?id="+id;
        });
        
        //添加新文件夹
        /*$("#showNewFolderModal").click(function () {
            layer.prompt({title:"请输入文件夹名称"},function(text,index){
                layer.close(index);
                $.post("/disk/new/folder",{"pId":pid,"name":text,"accountId":accountId}).done(function(resp){
                    if(resp.state == 'success') {
                        layer.msg("创建成功");
                        /!*$("#dataTable").html("");
                         var html = template("trTemplate",resp);
                         $("#dataTable").append(html);*!/
                        $("#dataTable").html("");
                        for(var i = 0;i < resp.data.length;i++) {
                            var obj = resp.data[i]; //{id:1,name:'',fileSize:}
                            obj.updateTime = moment(obj.updateTime).format("MM月DD日"); //将时间戳格式化
                            var html = template("trTemplate", obj); //将JSON对象传递给模板对象，转换为HTML
                            $("#dataTable").append(html);
                        }
                    } else {
                        layer.msg(resp.message);
                    }
                }).error(function(){
                    layer.msg("服务器异常");
                });
            });
        });*/


    });


</script>
</body>
</html>
