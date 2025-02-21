<template>
  <a-row class="login">
    <a-col :span="8" :offset="8" class="login-main">
      <h1 style="text-align: center">
        <rocket-two-tone/>&nbsp;12306售票系统
      </h1>
      <a-form
          :model="loginForm"
          name="basic"
          autocomplete="off"
      >
        <a-form-item
            label=""
            name="mobile"
            :rules="[{ required: true, message: '请输入手机号！' }]"
        >
          <a-input v-model:value="loginForm.mobile" placeholder="手机号"/>
        </a-form-item>

        <a-form-item
            label=""
            name="code"
            :rules="[{ required: true, message: '请输入验证码！' }]"
        >
          <a-input v-model:value="loginForm.code">
            <template #addonAfter>
              <a @click="sendCode">获取验证码</a>
            </template>
          </a-input>
        </a-form-item>

        <a-form-item>
          <a-button type="primary" block @click="login">登录</a-button>
        </a-form-item>
      </a-form>
    </a-col>
  </a-row>
</template>

<script setup>
import { reactive } from 'vue';
import  axios  from 'axios';
import { useRouter } from "vue-router";
// 通知组件
import {notification} from "ant-design-vue";

const loginForm = reactive({
  mobile: '18943191561',
  code: '',
});

const router = useRouter();


const sendCode = () => {
  axios.post('/member/member/send-code', {
    mobile: loginForm.mobile,
  }).then(res => {
    let data = res.data;
    if (data.success) {
      notification.success({description: '验证码发送成功'});
      loginForm.code = '8888';
    } else {
      notification.error({duration: data.message});
    }
  })
};
const login = () => {
   axios.post('/member/member/login', loginForm).then(res => {
   let data = res.data;
   if (data.success) {
      notification.success({description: '登录成功'});
      //  登陆成功，跳转控台主页
      router.push('/');
   } else {
      notification.error({duration: data.message});
   }
  });
};
</script>

<style>
.login-main h1 {
  font-size: 25px;
  font-weight: bold;
}
.login-main {
  margin-top: 100px;
  padding: 30px 30px 20px;
  border: 2px solid grey;
  border-radius: 10px;
  background-color: #fcfcfc;
}
</style>
