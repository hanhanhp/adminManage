/**
 * Created by linkzz on 2017-06-26.
 */
layui.use(['layer', 'form','element','jquery'], function(){
    var $ = layui.jquery,
        element = layui.element(),
        layer = layui.layer;
    layer.msg('欢迎访问！');

    var mainHeight = $(window).height()-45-40-55-78;

    $('.leftNav').on('click',function () {
        var url = $(this).children('a').attr('data-url');
        var id = $(this).children('a').attr('data-id');     //tab唯一Id
        var icon = $(this).children('a').attr('data-icon1');     //icon
        var title = $(this).children('a').text();           //菜单名称
        layer.msg("切换导航："+url);
        if (title == "首页") {
            element.tabChange('tab', 0);
            return;
        }
        if (url == undefined) return;
        var tabTitleDiv = $('.layui-tab[lay-filter=\'body-tab\']').children('.layui-tab-title');
        var exist = tabTitleDiv.find('li[lay-id=' + id + ']');
        if (exist.length > 0) {
            //切换到指定索引的卡片
            element.tabChange('body-tab', id);
        } else {
            var index = layer.load(1);
            //由于Ajax调用本地静态页面存在跨域问题，这里用iframe
            setTimeout(function () {
                //模拟菜单加载
                layer.close(index);
                element.tabAdd('body-tab', { title: title,content: '<iframe src="' + url + '" seamless="seamless" style="width:100%;height:'+mainHeight+'px;border:none;outline:none;"></iframe>', id: id });
                //切换到指定索引的卡片
                element.tabChange('body-tab', id);
            }, 500);
        }
    });
});

