<script setup>
import TheHeader from "@/components/the-header.vue";
import TheSider from "@/components/the-sider.vue";
</script>

<template>
  <a-layout id="components-layout-demo-top-side-2">
    <TheHeader></TheHeader>
    <a-layout>
      <TheSider></TheSider>
      <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <router-view></router-view>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<style>

</style>