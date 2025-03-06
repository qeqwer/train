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
        :openKeys="['batch','base','business']"
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
      <a-sub-menu key="batch">
        <template #title>
          <span>
            <UnorderedOutlined />
            跑批管理
          </span>
        </template>
      <a-menu-item key="/batch/job">
        <router-link to="/batch/job">
          <MenuUnfoldOutlined/> &nbsp; 定时任务
        </router-link>
      </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="business">
        <template #title>
          <span>
            <UnorderedOutlined/>
            业务管理
          </span>
        </template>
        <a-menu-item key="/business/train-ticket">
          <router-link to="/business/train-ticket">
            <user-outlined/> &nbsp; 余票信息
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/train">
          <router-link to="/business/train">
            <user-outlined/> &nbsp; 每日车次
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/train-station">
          <router-link to="/business/train-station">
            <user-outlined/> &nbsp; 每日车站
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/train-carriage">
          <router-link to="/business/train-carriage">
            <user-outlined/> &nbsp; 每日车厢
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/train-seat">
          <router-link to="/business/train-seat">
            <user-outlined/> &nbsp; 每日座位
          </router-link>
        </a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="base">
        <template #title>
          <span>
            <UnorderedOutlined/>
            基础数据
          </span>
        </template>
        <a-menu-item key="/base/station">
          <router-link to="/base/station">
            <user-outlined/> &nbsp; 车站管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train">
          <router-link to="/base/train">
            <user-outlined/> &nbsp; 火车管理
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-station">
          <router-link to="/base/train-station">
            <user-outlined/> &nbsp; 火车车站
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-carriage">
          <router-link to="/base/train-carriage">
            <user-outlined/> &nbsp; 火车车厢
          </router-link>
        </a-menu-item>
        <a-menu-item key="/base/train-seat">
          <router-link to="/base/train-seat">
            <user-outlined/> &nbsp; 火车座位
          </router-link>
        </a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>

<style scoped>

</style>