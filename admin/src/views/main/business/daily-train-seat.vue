<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

const open = ref(false);
const loading = ref(false);
const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY;
const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY;

const dailyTrainSeat = ref({
  id: undefined,
  date: undefined,
  trainCode: undefined,
  carriageIndex: undefined,
  row: undefined,
  col: undefined,
  seatType: undefined,
  carriageSeatIndex: undefined,
  sell: undefined,
  createTime: undefined,
  updateTime: undefined,
});

// 分页的三个属性名是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 3,
});

const dailyTrainSeatlist = ref([]);
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
    title: '箱序',
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
    title: '同车箱座序',
    dataIndex: 'carriageSeatIndex',
    key: 'carriageSeatIndex',
  },
  {
    title: '售卖情况',
    dataIndex: 'sell',
    key: 'sell',
  },
  {
    title: '操作',
    dataIndex: 'operation'
  }
];

const handleQuery = (param) => {
  if(!param){
    param={
      page: 1,
      size: pagination.value.pageSize
    };
  }
  loading.value = true;
  axios.get('/business/admin/daily-train-seat/query-list', {
    params: {
      page: param.page,
      size: param.size,
    }
  }).then((res) => {
    loading.value = false;
    let data = res.data;
    if (data.success) {
      dailyTrainSeatlist.value = data.content.list;
      // 设置分页控件的值
      pagination.value.current = param.page;
      pagination.value.total = data.content.total;
    } else {
      notification.error({description: data.message});
    }
  });
};

const handleTableChange = (page) => {
  console.log("看看自带的分页参数都有啥：" + JSON.stringify(page));
  handleQuery({
    page: page.current,
    size: page.pageSize
  });
};

const onAdd = () => {
  dailyTrainSeat.value = {};
  open.value = true;
};

const onEdit = (record) => {
  dailyTrainSeat.value = window.Tool.copy(record);
  open.value = true;
};

const onDelete = (record) => {
  axios.delete('/business/admin/daily-train-seat/delete/' + record.id).then(res => {
    let data = res.data;
    if(data.success){
      notification.success({description: '删除成功！'});
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize,
      });
    } else {
      notification.error({description: data.message});
    }
  });
};

const handleOk = () => {
  axios.post('/business/admin/daily-train-seat/save', dailyTrainSeat.value).then(res => {
    let data = res.data;
    if (data.success) {
      notification.success({description: '保存成功！'});
      open.value = false;
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize
      });
    } else {
      notification.error({description: data.message});
    }
  });
};

onMounted(() =>{handleQuery({page: 1, size: pagination.value.pageSize});});
</script>

<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainSeatlist"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{column, record}">
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm
              title="删除后不可恢复，确认删除?"
              @confirm="onDelete(record)"
              ok-text="确认" cancel-text="取消">
            <a style="color: red">删除</a>
          </a-popconfirm>
          <a @click="onEdit(record)">编辑</a>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'col'">
        <span v-for="item in SEAT_COL_ARRAY" :key="item.code">
          <span v-if="item.code === record.col">
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
  <a-modal v-model:open="open" title="每日座位" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="dailyTrainSeat" :label-col="{span: 4}" :wrapper-col="{span: 20}">
      <a-form-item label="日期">
        <a-date-picker v-model:value="dailyTrainSeat.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期"/>
      </a-form-item>
      <a-form-item label="车次编号">
        <a-input v-model:value="dailyTrainSeat.trainCode"/>
      </a-form-item>
      <a-form-item label="箱序">
        <a-input v-model:value="dailyTrainSeat.carriageIndex"/>
      </a-form-item>
      <a-form-item label="排号">
        <a-input v-model:value="dailyTrainSeat.row"/>
      </a-form-item>
      <a-form-item label="列号">
        <a-select v-model:value="dailyTrainSeat.col">
          <a-select-option v-for="item in SEAT_COL_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="座位类型">
        <a-select v-model:value="dailyTrainSeat.seatType">
          <a-select-option v-for="item in SEAT_TYPE_ARRAY" :key="item.code" :value="item.code">
            {{item.desc}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="同车箱座序">
        <a-input v-model:value="dailyTrainSeat.carriageSeatIndex"/>
      </a-form-item>
      <a-form-item label="售卖情况">
        <a-input v-model:value="dailyTrainSeat.sell"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<style scoped>

</style>