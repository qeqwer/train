import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import * as Icons from '@ant-design/icons-vue'
import axios from "axios";

const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

//全局图标
const icons = Icons;
for(const i in icons){
    app.component(i,icons[i]);
}

axios.interceptors.request.use(config => {
    console.log('请求参数：', config);
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(response => {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    return Promise.reject(error);
});
