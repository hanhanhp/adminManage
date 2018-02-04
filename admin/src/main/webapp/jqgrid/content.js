/**
 * Created by linkzz on 2017-05-06.
 */
$(function(){
    //页面加载完成之后执行
    $.jgrid.defaults.width = $(window).width();
    $.jgrid.defaults.height = $(window).height()-170;
    $.jgrid.defaults.responsive = true;
    $.jgrid.defaults.styleUI = 'Bootstrap';
    $.jgrid.styleUI.Bootstrap.base.rowTable = "table table-bordered table-striped";
    pageInit();
});
//自定义按钮
$.extend($.fn.fmatter, {
    actionFormatter: function(cellvalue, options, rowObject) {
        var url = "content/edit?id="+rowObject['id'];
        var retVal = '<button class="layui-btn layui-btn-mini" onclick="openiframe(\'' + url + '\')">编辑</button>';
        return retVal;
    }
});

function pageInit(){
    var jqGrid = "#jqGrid";
    var jqGridPager = "#jqGridPager";
    $.ajax({
        url : "channel/treeChannel",
        type:'POST',
        dataType : "json",
        cache : false,
        //是否异步发送
        async : false,
        success : function(data){
            pnames = data;
        },
        error : function(textStatus, errorThrown) {
            alert("系统ajax交互错误: " + textStatus);
        }
    });
    var s = "";
    for (k in pnames) s += ';' + k + ':' + pnames[k]; //转换为jqGrid select编辑需要的value值
    s = s.substring(1); //去掉第一个;
    //创建jqGrid组件
    $(jqGrid).jqGrid({
        url: 'content/dataGrid',
        editurl : 'content/saveOrUpdate',
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
                label: '标题',
                name: 'title',
                width: 150,
                editable:true
            },
            {
                label: '所属栏目',
                name: 'cid',
                width: 150,
                formatter: function (v, opt, rec) { return pnames[v]; }, //将value转换为对应的text
                editable:true
            },
            {
                label: '加粗',
                name: 'bold',
                width: 150,
                editable:true,
                edittype:"select",
                stype: "select",
                searchoptions: {value: ":[All];on:加粗;off:正常"},
                editoptions:{
                    value:"on:加粗;off:正常"
                },
                formatter:function (cellvalue,options,rowObject) {
                    var temp = "加粗";
                    if (cellvalue == "off"){
                        temp = "正常";
                    }
                    return temp;
                }
            },{
                label: '斜体',
                name: 'italic',
                width: 150,
                editable:true,
                edittype:"select",
                stype: "select",
                searchoptions: {value: ":[All];on:斜体;off:正常"},
                editoptions:{
                    value:"on:斜体;off:正常"
                },
                formatter:function (cellvalue,options,rowObject) {
                    var temp = "斜体";
                    if (cellvalue == "off"){
                        temp = "正常";
                    }
                    return temp;
                }
            },
            {
                label: '排序',
                name: 'sorter',
                width: 150,
                editable:true
            },
            {
                label: '标题颜色',
                name: 'color',
                width: 150,
                editable:true
            },
            {
                label: '状态',
                name: 'status',
                width: 150,
                editable:true,
                edittype:"select",
                stype: "select",
                searchoptions: {value: ":[All];on:发布;off:未发布"},
                editoptions:{
                    value:"on:发布;off:未发布"
                },
                formatter:function (cellvalue,options,rowObject) {
                    var temp = "发布";
                    if (cellvalue == "off"){
                        temp = "未发布";
                    }
                    return temp;
                }
            },
            {
                label: '置顶',
                name: 'hits',
                width: 150,
                editable:true,
                edittype:"select",
                stype: "select",
                searchoptions: {value: ":[All];on:置顶;off:不置顶"},
                editoptions:{
                    value:"on:置顶;off:不置顶"
                },
                formatter:function (cellvalue,options,rowObject) {
                    var temp = "置顶";
                    if (cellvalue == "off"){
                        temp = "不置顶";
                    }
                    return temp;
                }
            },
            {
                label: '推荐',
                name: 'good',
                width: 150,
                editable:true,
                edittype:"select",
                stype: "select",
                searchoptions: {value: ":[All];on:推荐;off:不推荐"},
                editoptions:{
                    value:"on:推荐;off:不推荐"
                },
                formatter:function (cellvalue,options,rowObject) {
                    var temp = "推荐";
                    if (cellvalue == "off"){
                        temp = "不推荐";
                    }
                    return temp;
                }
            },
            {
                label: '创建时间',
                name: 'createtime',
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
        altRows:true,
        toppager:false,
        jqModal:true,
        rowNum:10,
        rowList:[10,20,30],
        viewrecords: true,
        sortable:true,
        sortname:'createtime',
        sord:'asc',
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
            add: false,
            edit: false,
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
    $("#t_jqGrid").append('<div class="layui-btn-group"><button id="add" class="layui-btn layui-btn-small"> <i class="layui-icon">&#xe654;</i> 增加</button></div>');
    $("#add").on("click", function(){
        var url = "content/add";
        layer.full(openiframe(url));
    });
}

