<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import TrainSelectView from "@/components/train-select.vue";

const loading = ref(false);
const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;
let params = ref({
  trainCode: null
});


// 分页的三个属性名是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 10,
});

const trainSeatlist = ref([]);
// let passengerlist = reactive({list:[]});

const columns = [
  {
    title: '车次编号',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '厢序',
    dataIndex: 'carriageIndex',
    key: 'carriageIndex',
  },
  {
    title: '排号',
    dataIndex: 'row',
    key: 'row',
  },
  {
    title: '列号',
    dataIndex: 'col',
    key: 'col',
  },
  {
    title: '座位类型',
    dataIndex: 'seatType',
    key: 'seatType',
  },
  {
    title: '同车厢座序',
    dataIndex: 'carriageSeatIndex',
    key: 'carriageSeatIndex',
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
  axios.get('/business/admin/train-seat/query-list', {
    params: {
      page: param.page,
      size: param.size,
      trainCode: params.value.trainCode
    }
  }).then((res) => {
    loading.value = false;
    let data = res.data;
    if (data.success) {
      trainSeatlist.value = data.content.list;
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
      <train-select-view v-model:value="params.trainCode" width="200px"/>
      <a-button type="primary" @click="handleQuery()">查询</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trainSeatlist"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{column, record}">
      <template v-if="column.dataIndex === 'operation'">
      </template>
      <template v-else-if="column.dataIndex === 'col'">
        <span v-for="item in SEAT_COL_ARRAY" :key="item.code">
          <span v-if="item.code === record.col && item.type === record.seatType">
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