/**
 * Created by linkzz on 2017-05-06.
 */
$(function(){
    //页面加载完成之后执行
    $.jgrid.defaults.width = $(window).width();
    $.jgrid.defaults.height = $(window).height()-165;
    $.jgrid.defaults.responsive = true;
    $.jgrid.defaults.styleUI = 'Bootstrap';
    $.jgrid.styleUI.Bootstrap.base.rowTable = "table table-bordered table-striped";
    pageInit();
});
//自定义按钮
$.extend($.fn.fmatter, {
    actionFormatter: function(cellvalue, options, rowObject) {
        var urlRole = "menu/assignMenu?roleId="+rowObject['id'];
        var urlPermission = "permission/assignPermission?roleId="+rowObject['id'];
        var retVal = '<div class="layui-btn-group"><button onclick="openiframe(\'' + urlPermission + '\')" class="layui-btn layui-btn-mini">分配权限</button> <button onclick="openiframe(\'' + urlRole + '\')" class="layui-btn layui-btn-mini"> 分配菜单</button></div>';
        return retVal;
    }
});
function pageInit(){
    var jqGrid = "#jqGrid";
    var jqGridPager = "#jqGridPager";
    //创建jqGrid组件
    $(jqGrid).jqGrid({
        url: 'role/dataGrid',
        editurl : 'role/saveOrUpdate',
        datatype: "json",
        mtype: "POST",
        caption: "",
        loadonce: false,
        colModel: [
            {
                label: 'ID',
                name: 'id',
                hidden:true,
                key: true,
                width: 75,
                editable:false
            },
            {
                label: '名称',
                name: 'name',
                width: 150,
                editable:true
            },
            {
                label: '父级ID',
                name: 'pid',
                width: 150,
                hidden: true,
                editable:true
            },
            {
                label: '描述',
                name: 'intro',
                width: 150,
                editable:true
            },
            {
                label: '创建时间',
                name: 'created_at',
                width: 150,
                editable:false
            },
            {
                name : '操作中心',
                index : '',
                width : 150,
                fixed : true,
                sortable : false,
                resize : false,
                formatter: 'actionFormatter'
            }
        ],
        shrinkToFit:true,
        colMenu : true,
        altRows:true,
        toppager:false,
        jqModal:true,
        rowNum:10,
        rowList:[10,20,30],
        viewrecords: true,
        sortable:true,
        sortname:'id',
        multiselect:true,
        multiboxonly:true,
        multiselectWidth:20,
        rownumbers: true,
        rownumWidth: 25,
        toolbar:[true,"top"],
        pager: jqGridPager
    });
    /*创建filterToolbar*/
    $('#jqGrid').jqGrid('filterToolbar',{
        stringResult: true,
        searchOperators: true
    });
    /*创建jqGrid的操作按钮容器*/
    /*可以控制界面上增删改查的按钮是否显示*/
    $('#jqGrid').navGrid("#jqGridPager", {
            search: true, // show search button on the toolbar
            add: true,
            edit: true,
            del: true,
            refresh: true,
            view: true,
            columns:true,
            position: "left",
            cloneToTop: true
        },
        // options for the Edit Dialog
        {
            editCaption: "编辑",
            top:$(window).height()/2-130,
            left:$(window).width()/2-300,
            width:300,
            modal:true,
            recreateForm: true,
            checkOnUpdate : true,
            checkOnSubmit : true,
            closeAfterEdit: true,
            reloadAfterSubmit:true,
            afterComplete: afterCompleteFun,
            errorTextFormat: function (data) {
                return 'Error: ' + data.responseText
            }
        },
        // options for the Add Dialog
        {
            top:$(window).height()/2-130,
            left:$(window).width()/2-300,
            width:300,
            modal:true,
            closeAfterAdd: true,
            reloadAfterSubmit:true,
            recreateForm: true,
            afterComplete: afterCompleteFun,
            errorTextFormat: function (data) {
                return 'Error: ' + data.responseText
            }
        },
        // options for the Delete Dailog
        {
            top:$(window).height()/2-130,
            left:$(window).width()/2-300,
            width:300,
            afterComplete: afterCompleteFun,
            errorTextFormat: function (data) {
                return 'Error: ' + data.responseText
            }
        },
        // search options - define multiple search
        {
            top:$(window).height()/2-130,
            left:$(window).width()/2-300,
            multipleSearch: true,
            multipleGroup: false,
            showQuery: true
        },{
            top:$(window).height()/2-130,
            left:$(window).width()/2-300
        }
    );
    function afterCompleteFun(response,postdata) {
        var obj=jQuery.parseJSON(response.responseText);
        layer.msg(obj.msg, {time:1800});
    }
    $("#t_jqGrid").append('<div class="layui-btn-group"><button id="export" class="layui-btn layui-btn-small"><i class="layui-icon">&#xe654;</i> </button> <button id="exportxlsx" class="layui-btn layui-btn-small"> <i class="layui-icon">&#xe642;</i> </button> <button class="layui-btn layui-btn-small"> <i class="layui-icon">&#xe640;</i> </button> <button class="layui-btn layui-btn-small"> <i class="layui-icon">&#xe602;</i> </button> </div>');
    $("#export").on("click", function(){
        $("#jqGrid").jqGrid("exportToCsv",{
            separator: ",",
            separatorReplace : "", // in order to interpret numbers
            quote : '"',
            escquote : '"',
            newLine : "\r\n", // navigator.userAgent.match(/Windows/) ?	'\r\n' : '\n';
            replaceNewLine : " ",
            includeCaption : true,
            includeLabels : true,
            includeGroupHeader : true,
            includeFooter: true,
            fileName : "jqGridExport.csv",
            returnAsString : false
        })
    });
}
