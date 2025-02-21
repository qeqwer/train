<script setup>
import {ref} from "vue";
import store from "@/store";
let member = store.state.member;

const selectedKeys1 = ref(['2']);
</script>

<template>
  <a-layout-header class="header">
    <div class="logo"/>
    <div style="float: right; color: white;">
      您好：{{member.mobile}} &nbsp;&nbsp;
      <router-link to="/login"> 退出登录 </router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys1"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="1">nav 11</a-menu-item>
      <a-menu-item key="2">nav 22</a-menu-item>
      <a-menu-item key="3">nav 33</a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<style scoped>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}
</style>