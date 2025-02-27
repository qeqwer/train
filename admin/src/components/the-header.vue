<script setup>
import {ref, watch} from "vue";
import router from "@/router";

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
        12306控制台
      </router-link>
    </div>
    <div style="float: right; color: white;">
      欢迎使用控台管理
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
      <a-menu-item key="/about">
        <router-link to="/about">
          <user-outlined/> &nbsp; 关于
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