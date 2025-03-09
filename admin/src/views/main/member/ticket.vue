<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

const loading = ref(false);
const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;


// 分页的三个属性名是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 3,
});

const ticketlist = ref([]);
// let passengerlist = reactive({list:[]});

const columns = [
  {
    title: '会员id',
    dataIndex: 'memberId',
    key: 'memberId',
  },
  {
    title: '乘客id',
    dataIndex: 'passengerId',
    key: 'passengerId',
  },
  {
    title: '乘客姓名',
    dataIndex: 'passengerName',
    key: 'passengerName',
  },
  {
    title: '日期',
    dataIndex: 'trainDate',
    key: 'trainDate',
  },
  {
    title: '车次编号',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '箱序',
    dataIndex: 'carriageIndex',
    key: 'carriageIndex',
  },
  {
    title: '排号',
    dataIndex: 'seatRow',
    key: 'seatRow',
  },
  {
    title: '列号',
    dataIndex: 'seatCol',
    key: 'seatCol',
  },
  {
    title: '出发站',
    dataIndex: 'startStation',
    key: 'startStation',
  },
  {
    title: '出发时间',
    dataIndex: 'startTime',
    key: 'startTime',
  },
  {
    title: '到达站',
    dataIndex: 'endStation',
    key: 'endStation',
  },
  {
    title: '到站时间',
    dataIndex: 'endTime',
    key: 'endTime',
  },
  {
    title: '座位类型',
    dataIndex: 'seatType',
    key: 'seatType',
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
  axios.get('/member/admin/ticket/query-list', {
    params: {
      page: param.page,
      size: param.size,
    }
  }).then((res) => {
    loading.value = false;
    let data = res.data;
    if (data.success) {
      ticketlist.value = data.content.list;
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
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      
    </a-space>
  </p>
  <a-table :dataSource="ticketlist"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{column, record}">
      <template v-if="column.dataIndex === 'operation'">
      </template>
      <template v-else-if="column.dataIndex === 'seatCol'">
        <span v-for="item in SEAT_COL_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatCol">
            {{item.desc}}
          </span>
        </span>
      </template>
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in SEAT_TYPE_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatType">
            {{item.desc}}
          </span>
        </span>
      </template>
    </template>
  </a-table>
</template>

<style scoped>

</style>