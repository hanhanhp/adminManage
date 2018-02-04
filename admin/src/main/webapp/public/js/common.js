/**
 * Created by linkzz on 2017-06-07.
 */
function openiframe(url) {
    layer.full(
        layer.open({
            type: 2,
            maxmin: true,
            area: ['420px', '240px'],
            fixed: false, //不固定
            content: url
        })
    );
};

function openiframepwd(url) {
    layer.open({
        type: 2,
        maxmin: true,
        area: ['420px', '240px'],
        fixed: true, //不固定
        content: url
    })
}