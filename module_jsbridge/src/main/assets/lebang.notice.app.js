var userAgent = navigator.userAgent.toLowerCase(),
    isiOS = userAgent.match(/iphone/)[0] == 'iphone',
    isAndroid = userAgent.match(/android/i) == 'android',
    $content = $('#content'),
    bridgeMethod = 'getPrivateUrl',
    assetsData = [];

function renderImage (response) {
    if (response.code == 0) {
        var urls = response.result;
        urls.forEach(function (item, index, arr) {
            var img = '<img src="' + item + '" />';
            $content.find('.picture').eq(index).append(img);
        });
    } else {
        alert(response.error)
    }
}

$content.find('.picture').each(function () {
    var $this = $(this);
    assetsData.push({
        key: $this.attr('data-key'),
        bucket: $this.attr('data-bucket')
    });
});

if(isiOS){
    function start () {
        getDataFromJs(bridgeMethod, JSON.stringify(assetsData), 'renderImage')
    } 
    function getDataFromJs (method, data, callback) {
        return {
            'method': method,
            'data': data,
            'callback': callback
        }
    }
}
if (isAndroid) {
    common.appBridge.toAndroid(bridgeMethod, assetsData, 'renderImage');
}