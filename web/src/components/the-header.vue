<script setup>
import {ref, watch} from "vue";
import store from "@/store";
import router from "@/router";
let member = store.state.member;

const selectedKeys = ref([]);
watch(()=> router.currentRoute.value.path, (value)=> {
  console.log('watch', value);
  selectedKeys.value = [];
  selectedKeys.value.push(value);
}, {
  immediate: true
});

</script>

<template>
  <a-layout-header class="header">
    <div class="logo"/>
    <div style="float: right; color: white;">
      您好：{{member.mobile}} &nbsp;&nbsp;
      <router-link to="/login" style="color: white;"> 退出登录 </router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <coffee-outlined/> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/passenger">
        <router-link to="/passenger">
          <user-outlined/> &nbsp; 乘车人管理
        </router-link>
      </a-menu-item>
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