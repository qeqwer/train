<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

const passengerOptions = ref([]); //选项
const passengerChecks = ref([]);  //选中值

const dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};
console.log("下单的车次信息", dailyTrainTicket);

const SEAT_TYPE = window.SEAT_TYPE;
console.log(SEAT_TYPE)
// 本车次提供的座位类型seatTypes，含票价，余票等信息，例：
// {
//   type: "YDZ",
//   code: "1",
//   desc: "一等座",
//   count: "100",
//   price: "50",
// }
// 关于SEAT_TYPE[KEY]：当知道某个具体的属性xxx时，可以用obj.xxx，当属性名是个变量时，可以使用obj[xxx]
const seatTypes = [];
// const PASSENGER_TYPE_ARRAY = window.PASSENGER_TYPE_ARRAY;
// const visible = ref(false);

for (let KEY in SEAT_TYPE) {
  let key = KEY.toLowerCase();
  if (dailyTrainTicket[key] >= 0) {
    seatTypes.push({
      type: KEY,
      code: SEAT_TYPE[KEY]["code"],
      desc: SEAT_TYPE[KEY]["desc"],
      count: dailyTrainTicket[key],
      price: dailyTrainTicket[key + 'Price'],
    })
  }
}
console.log("本车次提供的座位：", seatTypes)

const passengersList = ref([]);
const handleQueryPassenger = () => {
  axios.get('/member/passenger/query-mine').then(res => {
    let data = res.data;
    if (data.success) {
      passengersList.value = data.content;
      passengersList.value.forEach(item => {
        passengerOptions.value.push({
          value: item.id,
          label: item.name
        })
      });
    } else {
      notification.error({description: data.message});
    }
  });
}

onMounted(() => {
  handleQueryPassenger();
})
</script>

<template>
  <div class="order-train">
    <span class="order-train-main">{{dailyTrainTicket.date}}</span>&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.trainCode}}</span>次&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.start}}</span>站
    <span class="order-train-main">({{dailyTrainTicket.startTime}})</span>&nbsp;
    <span class="order-train-main">——</span>&nbsp;
    <span class="order-train-main">{{dailyTrainTicket.end}}</span>站
    <span class="order-train-main">({{dailyTrainTicket.endTime}})</span>&nbsp;

    <div class="order-train-ticket">
      <span v-for="item in seatTypes" :key="item.type">
        <span>{{item.desc}}</span>：
        <span class="order-train-ticket-main">{{item.price}}￥</span>&nbsp;
        <span class="order-train-ticket-main">{{item.count}}</span>&nbsp;张票&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </span>
    </div>
  </div>
  <a-divider></a-divider>
  <b>勾选要购票的乘客：</b>&nbsp;
  <a-checkbox-group v-model:value="passengerChecks" :options="passengerOptions" />
  <br/>
  {{passengerChecks}}
</template>

<style scoped>
.order-train .order-train-main {
  font-size: 18px;
  font-weight: bold;
}

.order-train .order-train-ticket {
  margin-top: 15px;
}

.order-train .order-train-ticket .order-train-ticket-main {
  color: red;
  font-size: 18px;
}

</style>