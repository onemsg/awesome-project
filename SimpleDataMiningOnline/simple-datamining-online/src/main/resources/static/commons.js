//得到查询字符串
function getQueryString(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    let url = "?id=2&name=iris";
    let r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    };
    return "";
 }

 function getRequestParams() {
    var url = ""
    // var url = window.location.search; //获取url中"?"符后的字串 
    var params = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            params[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
        }
        return params;
    }
    return null;
}

