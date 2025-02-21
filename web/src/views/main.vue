<script setup>
import TheHeader from "@/components/the-header.vue";
import TheSider from "@/components/the-sider.vue";
import axios from "axios";
import {notification} from "ant-design-vue";
import {ref} from "vue";

const content = ref();

axios.get('/member/member/count').then(res => {
      let data = res.data;
      if (data.success) {
        content.value = data.content;
      } else {
        notification.error({duration: data.message});
      }
    }
)


</script>

<template>
  <a-layout id="components-layout-demo-top-side-2">
    <TheHeader></TheHeader>
    <a-layout>
      <TheSider></TheSider>
      <a-layout style="padding: 0 24px 24px">
        <a-breadcrumb style="margin: 16px 0">
          <a-breadcrumb-item>Home</a-breadcrumb-item>
          <a-breadcrumb-item>List</a-breadcrumb-item>
          <a-breadcrumb-item>App</a-breadcrumb-item>
        </a-breadcrumb>
        <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
          {{content}}
        </a-layout-content>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<style scoped>
.site-layout-background {
  background: #fff;
}
</style>