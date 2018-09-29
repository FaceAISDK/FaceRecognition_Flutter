(function (win) {
    var hasOwnProperty = Object.prototype.hasOwnProperty;
    var JSBridge = win.JSBridge || (win.JSBridge = {});
    var JSBRIDGE_PROTOCOL = 'JSBridge';  //定义协议类型,类比 HTTP 协议
    var Inner = {

        callbacks: {},    //缓存CallBack方法

        call: function (obj, method, params, callback) {
            var temp='# '+params;
            //我们在js中调用native方法的时候，在js中注册一个callback，然后将该callback在指定的位置上缓存起来，然后native层执行完毕对应方法后通过WebView.
            //loadUrl调用js中的方法，回调对应的callback。那么js怎么知道调用哪个callback呢？于是我们需要将callback的一个存储位置传递过去，
            //那么就需要native层调用js中的方法的时候将存储位置回传给js，js再调用对应存储位置上的callback，进行回调
            var port = Util.getCallBackPort();

            this.callbacks[port] = callback;
            var uri=Util.getUri(obj,method,params,port);

            //2.形成JSBridge 协议 - jsbridge://www:callback/getQRCode?params={"key":"value"}
            window.prompt(uri, "defvale");  //window.prompt 会调用Webview 重写的onJsPrompt(...)方法
        },

        //Native 执行完后回调注册的JsCallBack
        onFinish: function (port, jsonObj){
            var callback = this.callbacks[port];
            callback && callback(jsonObj);
            delete this.callbacks[port];
        },

    };

    var Util = {

        //1.获取缓存callBack 的key - port
        getCallBackPort: function () {
            return Math.floor(Math.random() * (1 << 30));
        },

        //2.形成类似于Http 协议的的JSBRIDGE 协议
        getUri:function(obj, method, params, port){
            params = this.getParam(params);
            var uri = JSBRIDGE_PROTOCOL + '://' + obj + ':' + port + '/' + method + '?' + params;
            return uri;
        },

        //3.获取Param
        getParam:function(obj){
            if (obj && typeof obj === 'object') {
                return JSON.stringify(obj);
            } else {
                return obj || '';
            }
        }
    };

    //4.不要端口
    for (var key in Inner) {
        if (!hasOwnProperty.call(JSBridge, key)) {
            JSBridge[key] = Inner[key];
        }
    }

})(window);
