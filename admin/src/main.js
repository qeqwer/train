import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import * as Icons from '@ant-design/icons-vue'
import axios from "axios";
import './assets/js/enums';

const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

//全局图标
const icons = Icons;
for(const i in icons){
    app.component(i,icons[i]);
}

axios.interceptors.request.use(config => {
    console.log('请求参数：', config);
    const _token = store.state.member.token;
    if(_token){
        config.headers.token = _token; //请求头增加token,headers.token中的‘token’ 需要和后端(gateway中过滤器)一致
        console.log("请求headers增加token：" , _token);
    }
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(response => {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    if(error.response.status === 401){
        console.log("未登录或登录超市，跳到登录页");
        store.commit('setMember',{});
        router.push('/login');
    }
    return Promise.reject(error);
});

axios.defaults.baseURL = process.env.VUE_APP_SERVER;

console.log('环境：', process.env.NODE_ENV)
console.log('服务端：', process.env.VUE_APP_SERVER)