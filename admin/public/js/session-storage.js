/**
 * 封装sessionStorage为SessionStorage
 * （sessionStorage只能操作字符串，故需封装）
 */
SESSION_ALL_TRAIN = "SESSION_ALL_TRAIN";
SESSION_ALL_STATION ="SESSION_ALL_STATION";
// sessionStorage，会话缓存，浏览器关闭失效；
// LocalStorage,浏览器关闭后重新打开有效
SessionStorage = {
    get: function (key) {
        var v = sessionStorage.getItem(key);
        if (v && typeof(v) !== "undefined" && v !== "undefined") {
            return JSON.parse(v);
        }
    },
    set: function (key, data) {
        sessionStorage.setItem(key, JSON.stringify(data));
    },
    remove: function (key) {
        sessionStorage.removeItem(key);
    },
    clearAll: function () {
        sessionStorage.clear();
    }
};
