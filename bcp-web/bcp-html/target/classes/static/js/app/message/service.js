
app.factory('msgService', function ($scope, $websocket, toaster, localStorageService) {
    return {
        /**
         * 获取未读消息列表
         * @returns {number|Number|*|string|string}
         */
        getUnreadMessages: function() {
            
        },
        send: function(message) {
            if (angular.isString(message)) {
                ws.send(message);
            } else if (angular.isObject(message)) {
                ws.send(JSON.stringify(message));
            }
        }
    };
});