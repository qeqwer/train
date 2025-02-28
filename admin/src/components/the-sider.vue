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
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
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
      <a-menu-item key="/station">
        <router-link to="/station">
          <user-outlined/> &nbsp; 车站管理
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-sider>
</template>

<style scoped>

</style>