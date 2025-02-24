<script setup>
import { ref } from 'vue';

const open = ref(false);
const showModal = () => {
  open.value = true;
};
const handleOk = e => {
  console.log(e);
  open.value = false;
};
</script>

<template>
  <div>
    <a-button type="primary" @click="showModal">新增</a-button>
    <a-modal v-model:open="open" title="乘车人" @ok="handleOk">
      <p>Some contents...</p>
      <p>Some contents...</p>
      <p>Some contents...</p>
    </a-modal>
  </div>
</template>

<style scoped>

</style>