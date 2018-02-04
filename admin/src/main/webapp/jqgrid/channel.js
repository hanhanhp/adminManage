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
function pageInit(){
    var jqGrid = "#jqGrid";
    var jqGridPager = "#jqGridPager";

    //创建jqGrid组件
    $(jqGrid).jqGrid({
        url: 'channel/dataGrid',
        editurl : 'channel/saveOrUpdate',
        datatype: "json",
        mtype: "POST",
        caption: "",
        loadonce: true,
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
                label: '频道名称',
                name: 'name',
                width: 150,
                editable:true,
                formoptions: {
                    colpos: 1,
                    rowpos: 1
                }
            },
            {
                label: '父级ID',
                name: 'parent',
                hidden:true
            },
            {
                label: '排序',
                name: 'sorter',
                width: 150,
                editable:true,
                formoptions: {
                    colpos: 2,
                    rowpos: 1
                }
            },
            {
                label: 'URL地址',
                name: 'url',
                width: 150,
                editable:true,
                formoptions: {
                    colpos: 1,
                    rowpos: 2
                }
            },
            {
                label: '类型',
                name: 'type',
                width: 150,
                editable:true,
                edittype:"select",
                stype: "select",
                searchoptions: {value: ":[All];true:导航;false:非导航"},
                editoptions:{
                    value:"true:导航;false:非导航"
                },
                formatter:function (cellvalue,options,rowObject) {
                    var temp = "导航";
                    if (cellvalue == "false"){
                        temp = "非导航";
                    }
                    return temp;
                },
                formoptions: {
                    colpos: 2,
                    rowpos: 2
                }
            },
            {
                label: '状态',
                name: 'hide',
                width: 150,
                editable:true,
                edittype:"select",
                stype: "select",
                searchoptions: {value: ":[All];true:启用;false:关闭"},
                editoptions:{
                    value:"true:启用;false:关闭"
                },
                formatter:function (cellvalue,options,rowObject) {
                    var temp = "启用";
                    if (cellvalue == "false"){
                        temp = "关闭";
                    }
                    return temp;
                },
                formoptions: {
                    colpos: 1,
                    rowpos: 3
                }
            },
            {
                label: '跳转方式',
                name: 'target',
                width: 150,
                editable:true,
                edittype:"select",
                stype: "select",
                searchoptions: {value: ":[All];true:新开;false:原来"},
                editoptions:{
                    value:"true:新开;false:原来"
                },
                formatter:function (cellvalue,options,rowObject) {
                    var temp = "新开";
                    if (cellvalue == "false"){
                        temp = "原来";
                    }
                    return temp;
                },
                formoptions: {
                    colpos: 2,
                    rowpos: 3
                }
            },
            {
                label: '异步加载',
                name: 'loaded',
                width: 150,
                editable:true,
                edittype:"select",
                editoptions:{
                    value:"true:是;false:否"
                },
                formatter:function (cellvalue,options,rowObject) {
                    var temp = "是";
                    if (cellvalue == "false"){
                        temp = "否";
                    }
                    return temp;
                },
                formoptions: {
                    colpos: 1,
                    rowpos: 4
                }
            },
            {
                name: '操作中心',
                index: '',
                width: 100,
                fixed: true,
                sortable: false,
                resize: false,
                formatter: 'actions'
            }
        ],
        shrinkToFit:true,
        //colMenu : true,
        altRows:true,
        toppager:false,
        jqModal:true,
        rowNum:10000,
        rowList:[10,20,30],
        hoverrows:true,
        viewrecords: true,
        //gridview: false,
        sortable:true,
        sortname:'id',
        multiselect:true,
        multiboxonly:true,
        multiselectWidth:20,
        rownumbers: true,
        rownumWidth: 25,
        toolbar:[true,"top"],
        treeGrid:true,
        expandColumn:"name",
        expandColClick:true,
        treedatatype:"json",
        treeGridModel:"adjacency",
        treeReader:{
            level_field:"level",
            parent_id_field: "parent",  //值必须为父级菜单的id值。
            leaf_field:"isLeaf",
            expanded_field:"expanded",
            loaded:"loaded"
        },
        loadError : function(xhr, st, err) {
            layer.msg("Type: " + st + "; Response: " + xhr.status + " "+ xhr.statusText, {time:1800});
        },
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
            top:$(window).height()/2-200,
            left:$(window).width()/2-300,
            width:600,
            modal:true,
            drag:true,
            resize:true,
            closeOnEscape:true,
            /*dataheight:150,*/
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
            top:$(window).height()/2-200,
            left:$(window).width()/2-300,
            width:600,
            modal:true,
            drag:true,
            resize:true,
            closeOnEscape:true,
            /*dataheight:150,*/
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
    };
    $('#jqGrid').jqGrid('bindKeys');
}
