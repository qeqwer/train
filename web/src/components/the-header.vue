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
    <div class="logo">
      <router-link to="/welcome">
        niko12306
      </router-link>
    </div>
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
      <a-menu-item key="/ticket">
        <router-link to="/ticket">
          <user-outlined/> &nbsp; 余票查询
        </router-link>
      </a-menu-item>
      <a-menu-item key="/my-ticket">
        <router-link to="/my-ticket">
          <user-outlined/> &nbsp; 我的车票
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<style scoped>
.logo {
  float: left;
  width: 150px;
  height: 31px;
  color: white;
  font-size: 20px;
}
</style>