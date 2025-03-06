<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import TrainSelect from "@/components/train-select.vue";
import StationSelect from "@/components/station-select.vue";

const loading = ref(false);
const params = ref({
  date: null,
  trainCode:null,
  start: null,
  end: null
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
    title: '日期',
    dataIndex: 'date',
    key: 'date',
  },
  {
    title: '车次编号',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '出发站',
    dataIndex: 'start',
    key: 'start',
  },
  {
    title: '出发站拼音',
    dataIndex: 'startPinyin',
    key: 'startPinyin',
  },
  {
    title: '出发时间',
    dataIndex: 'startTime',
    key: 'startTime',
  },
  {
    title: '出发站序',
    dataIndex: 'startIndex',
    key: 'startIndex',
  },
  {
    title: '到达站',
    dataIndex: 'end',
    key: 'end',
  },
  {
    title: '到达站拼音',
    dataIndex: 'endPinyin',
    key: 'endPinyin',
  },
  {
    title: '到站时间',
    dataIndex: 'endTime',
    key: 'endTime',
  },
  {
    title: '到站站序',
    dataIndex: 'endIndex',
    key: 'endIndex',
  },
  {
    title: '一等座余票',
    dataIndex: 'ydz',
    key: 'ydz',
  },
  {
    title: '一等座票价',
    dataIndex: 'ydzPrice',
    key: 'ydzPrice',
  },
  {
    title: '二等座余票',
    dataIndex: 'edz',
    key: 'edz',
  },
  {
    title: '二等座票价',
    dataIndex: 'edzPrice',
    key: 'edzPrice',
  },
  {
    title: '软卧余票',
    dataIndex: 'rw',
    key: 'rw',
  },
  {
    title: '软卧票价',
    dataIndex: 'rwPrice',
    key: 'rwPrice',
  },
  {
    title: '硬卧余票',
    dataIndex: 'yw',
    key: 'yw',
  },
  {
    title: '硬卧票价',
    dataIndex: 'ywPrice',
    key: 'ywPrice',
  },
];

const handleQuery = (param) => {
  if(!param){
    param={
      page: 1,
      size: pagination.value.pageSize
    };
  }
  loading.value = true;
  axios.get('/business/admin/daily-train-ticket/query-list', {
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


onMounted(() =>{handleQuery({page: 1, size: pagination.value.pageSize});});
</script>

<template>
  <p>
    <a-space>
      <train-select v-model="params.trainCode" width="200px"/>
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
      </template>
    </template>
  </a-table>
</template>

<style scoped>

</style>