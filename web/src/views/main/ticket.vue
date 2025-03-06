<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import StationSelect from "@/components/station-select.vue";
import dayjs from "dayjs";
import router from "@/router";

const loading = ref(false);
const params = ref({
  date: null,
  trainCode:null,
  start: null,
  end: null
});

const dailyTrainTicket = ref({
  id: null,
  trainCode: null,
  start: null,
  end: null,
  startTime: null,
  endTime: null,
  seats: null,
  index: null,
  date: null,
  trainTypeCode: null,
  trainTypeName: null,
  startIndex: null,
  endIndex: null,
  startTimeIndex: null,
  endTimeIndex: null,
  duration: null,
  ydz: null,
  edz: null,
  rw: null,
});


// 分页的三个属性名是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 3,
});

const dailyTrainTicketlist = ref([]);
// let passengerlist = reactive({list:[]});

const columns = [
  {
    title: '车次编号',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '车站',
    dataIndex: 'station',
  },
  {
    title: '时间',
    dataIndex: 'time',
  },
  {
    title: '历时',
    dataIndex: 'duration',
  },
  {
    title: '一等座',
    dataIndex: 'ydz',
    key: 'ydz',
  },
  {
    title: '二等座',
    dataIndex: 'edz',
    key: 'edz',
  },
  {
    title: '软卧',
    dataIndex: 'rw',
    key: 'rw',
  },
  {
    title: '硬卧',
    dataIndex: 'yw',
    key: 'yw',
  },
  {
    title: '操作',
    dataIndex: 'operation',
  },
];

const handleQuery = (param) => {
  if(Tool.isEmpty(params.value.date)){
    notification.error({description: '请选择日期'});
    return;
  }
  if(Tool.isEmpty(params.value.start)){
    notification.error({description: '请选择出发地'});
    return;
  }
  if(Tool.isEmpty(params.value.end)){
    notification.error({description: '请选择目的地'});
    return;
  }
  if(!param){
    param={
      page: 1,
      size: pagination.value.pageSize
    };
  }

  // 保存查询参数
  SessionStorage.set(SESSION_TICKET_PARAMS, params.value);

  loading.value = true;
  axios.get('/business/daily-train-ticket/query-list', {
    params: {
      page: param.page,
      size: param.size,
      date: params.value.date,
      trainCode: params.value.trainCode,
      Start: params.value.start,
      End: params.value.end
    }
  }).then((res) => {
    loading.value = false;
    let data = res.data;
    if (data.success) {
      dailyTrainTicketlist.value = data.content.list;
      // 设置分页控件的值
      pagination.value.current = param.page;
      pagination.value.total = data.content.total;
    } else {
      notification.error({description: data.message});
    }
  });
};

const handleTableChange = (page) => {
  // console.log("看看自带的分页参数都有啥：" + JSON.stringify(page));
  pagination.value.pageSize = page.pageSize;
  handleQuery({
    page: page.current,
    size: page.pageSize
  });
};

const calDuration = (startTime, endTime) => {
  let diff = dayjs(endTime, 'HH:mm:ss').diff(dayjs(startTime, 'HH:mm:ss'), 'seconds');
  return dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss');
};

const toOrder = (record) => {
  dailyTrainTicket.value = Tool.copy(record);
  SessionStorage.set(SESSION_ORDER, dailyTrainTicket.value);
  router.push("/order");
};

onMounted(() =>{
  params.value = SessionStorage.get(SESSION_TICKET_PARAMS) || {};
  if(Tool.isNotEmpty(params.value.date)){
    handleQuery({
      page: 1,
      size: pagination.value.pageSize
    });
  }
});
</script>

<template>
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期" />
      <station-select v-model="params.start" width="200px" placeholder="出发站"/>
      <station-select v-model="params.end" width="200px" placeholder="到达站"/>
      <a-button type="primary" @click="handleQuery()">查询</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainTicketlist"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{column, record}">
      <template v-if="column.dataIndex === 'operation'">
        <a-button type="primary" @click="toOrder(record)">预定</a-button>
      </template>
      <template v-else-if="column.dataIndex === 'station'">
        {{record.start}}<br/>
        {{record.end}}
      </template>
      <template v-else-if="column.dataIndex === 'time'">
        {{record.startTime}}<br/>
        {{record.endTime}}
      </template>
      <template v-else-if="column.dataIndex === 'duration'">
        {{calDuration(record.startTime, record.endTime)}}<br/>
        <div v-if="record.startTime.replaceAll(':', '') >= record.endTime.replaceAll(':', '')">
          次日到达
        </div>
        <div v-else>
          当日到达
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'ydz'">
        <div v-if="record.ydz >= 0">
          {{record.ydz}}<br/>
          {{record.ydzPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'edz'">
        <div v-if="record.edz >= 0">
          {{record.edz}}<br/>
          {{record.edzPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'rw'">
        <div v-if="record.rw >= 0">
          {{record.rw}}<br/>
          {{record.rwPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'yw'">
        <div v-if="record.yw >= 0">
          {{record.yw}}<br/>
          {{record.ywPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
    </template>
  </a-table>
</template>

<style scoped>

</style>