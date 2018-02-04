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
        var url = "role/assign_role?userId="+rowObject['id'];
        var retVal = '<button class="layui-btn layui-btn-mini" onclick="openiframe(\'' + url + '\')">分配角色</button>';
        return retVal;
    }
});

function pageInit(){
    var jqGrid = "#jqGrid";
    var jqGridPager = "#jqGridPager";
    //创建jqGrid组件
    $(jqGrid).jqGrid({
        url: 'user/dataGrid',
        editurl : 'user/saveOrUpdate',
        datatype: "json",
        mtype: "POST",
        caption: "",
        loadonce: false,
        colModel: [
            {
                label: 'ID',
                name: 'id',
                index: 'id',
                hidden:true,
                key: true,
                width: 75,
                editable:false
            },
            {
                label: '用户名',
                name: 'username',
                width: 150,
                editable:true
            },
            {
                label: '邮箱',
                name: 'email',
                width: 150,
                editable:true
            },
            {
                label: '电话',
                name: 'phone',
                width: 150,
                editable:true
            },
            {
                name : '操作中心',
                index : '',
                width : 100,
                fixed : true,
                sortable : false,
                resize : false,
                formatter: 'actionFormatter'
            }
        ],
        shrinkToFit:true,
        colMenu : true,
        subGrid: true,
        subGridRowExpanded:showChildGrid,
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
        },
        {
            top:$(window).height()/2-130,
            left:$(window).width()/2-300
        }
    );
    function afterCompleteFun(response,postdata) {
        var obj=jQuery.parseJSON(response.responseText);
        layer.msg(obj.msg, {time:1800});
    }
    //子表格
    function showChildGrid(parentRowID, parentRowKey) {
        $.ajax({
            url: 'user/detail?id='+parentRowKey,
            type: "GET",
            success: function (html) {
                $("#" + parentRowID).append(html);
            }
        });
    };
    $("#t_jqGrid").append('<div class="layui-btn-group"><button id="export" class="layui-btn layui-btn-small"><i class="layui-icon">&#xe62d;</i> </button> <button id="exportxlsx" class="layui-btn layui-btn-small"> <i class="layui-icon">&#xe642;</i> </button> <button class="layui-btn layui-btn-small"> <i class="layui-icon">&#xe640;</i> </button> <button class="layui-btn layui-btn-small"> <i class="layui-icon">&#xe602;</i> </button> </div>');
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

