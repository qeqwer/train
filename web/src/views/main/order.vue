<script setup>
const dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};
console.log("下单的车次信息", dailyTrainTicket);
</script>

<template>
  <div>{{ dailyTrainTicket }}</div>
</template>

<style scoped>

</style>