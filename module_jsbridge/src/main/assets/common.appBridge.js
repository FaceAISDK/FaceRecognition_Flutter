(function (w){
    w.common = {};
    w.common.appBridge = {};
    w.common.appBridge.communicate = function (method, data, JSCallBackfunction, JSCallBackString) {
        var userAgent = w.navigator.userAgent.toLowerCase(),
            isiOS = userAgent.match(/\(i[^;]+;( u;)? cpu.+mac os x/),
            isAndroid = userAgent.match(/android/i) == "android";
        if (isiOS) {
            var getDataFromJs = function (method, data, callback) {
                return {
                    'method': method,
                    'data': data,
                    'callback': callback || null
                }
            } 
            getDataFromJs(method, data, JSCallBackfunction);
        }
        if (isAndroid) {
            var uri = 'JSBridge://NativeBridgeClsName:' + (JSCallBackString ? JSCallBackString : '') + '/' + method +'?' + JSON.stringify(data);
            w.prompt(uri);
        }
    };
})(window)
