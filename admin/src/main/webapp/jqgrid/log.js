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
function pageInit(){
    var jqGrid = "#jqGrid";
    var jqGridPager = "#jqGridPager";
    //创建jqGrid组件
    $(jqGrid).jqGrid({
        url: 'log/dataGrid',
        editurl : 'log/saveOrUpdate',
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
                label: '访问地址',
                name: 'actionkey',
                width: 150,
                editable:true
            },
            {
                label: '操作时间',
                name: 'opertime',
                width: 150,
                hidden: false,
                editable:true
            },
            {
                label: 'IP地址',
                name: 'ip',
                width: 100,
                editable:true
            },
            {
                label: '用户ID',
                name: 'userid',
                width: 100,
                editable:false
            },
            {
                label: '用户名',
                name: 'username',
                width: 100,
                editable:false
            },
            {
                label: '参数',
                name: 'params',
                width: 150,
                editable:false,
                searchoptions : {
                    // show search options
                    sopt: ["in","ni"]
                }
            },
            {
                label: '返回结果',
                name: 'result',
                width: 150,
                editable:false
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
        toolbar:[false,"top"],
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
            add: false,
            edit: false,
            del: false,
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
            top:$(window).height()/2-180,
            left:$(window).width()/2-300
        }
    );
    function afterCompleteFun(response,postdata) {
        var obj=jQuery.parseJSON(response.responseText);
        layer.msg(obj.msg, {time:1800});
    }

}
