<script setup>
import {onMounted, reactive, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

const open = ref(false);
const loading = ref(false);

// reactive仅适用于对象
let passenger = reactive({
  id: undefined,
  memberId: undefined,
  name: undefined,
  idCard: undefined,
  type: undefined,
  createTime: undefined,
  updateTime: undefined,
});

// 分页的三个属性名是固定的
const pagination = reactive({
  total: 0,
  current: 1,
  pageSize: 3,
});

const passengerlist = ref([]);
// let passengerlist = reactive({list:[]});

const columns = [
  {
    title: '会员id',
    dataIndex: 'memberId',
    key: 'memberId',
  },
  {
    title: '姓名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '身份证',
    dataIndex: 'idCard',
    key: 'idCard',
  },
  {
    title: '旅客类型',
    dataIndex: 'type',
    key: 'type',
  },
  {
    title: '操作',
    dataIndex: 'operation'
  }
];

const onADD = () => {
  open.value = true;
};

const onEdit = (record) => {
  passenger.id = record.id;
  passenger.memberId = record.memberId;
  passenger.name = record.name;
  passenger.idCard = record.idCard;
  passenger.type = record.type;
  passenger.createTime = record.createTime;
  passenger.updateTime = record.updateTime;
  open.value = true;
};

const handleOk = () => {
  axios.post('/member/passenger/save', passenger).then(res => {
    let data = res.data;
    if (data.success) {
      notification.success({description: '保存成功！'});
      open.value = false;
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      })
    } else {
      notification.error({description: data.message});
    }
  })
};

const handleQuery = (param) => {
  if(!param){
    param={
      page: 1,
      size: pagination.pageSize
    };
  }
  loading.value = true;
  axios.get('/member/passenger/query-list',{
    params: {
      page: param.page,
      size: param.size,
    }
  }).then((res) => {
    loading.value = false;
    let data = res.data;
    if (data.success) {
      passengerlist.value = data.content.list;
      // 设置分页控件的值
      pagination.current = param.page;
      pagination.total = data.content.total;
    } else {
      notification.error({description: data.message});
    }
  })
};

const handleTableChange = (page) => {
  console.log("看看自带的分页参数都有啥：" + JSON.stringify(page));
  handleQuery({
    page: page.current,
    size: page.pageSize
  });
};


onMounted(() =>{handleQuery({page: 1, size: pagination.pageSize})});
</script>

<template>
  <div>
    <p>
      <a-space>
        <a-button type="primary" @click="handleQuery()">刷新</a-button>
        <a-button type="primary" @click="onADD">新增</a-button>
      </a-space>
    </p>
    <a-table :dataSource="passengerlist"
             :columns="columns"
             :pagination="pagination"
             @change="handleTableChange"
             :loading="loading"
    >
      <template #bodyCell="{column, record}">
        <template v-if="column.dataIndex === 'operation'">
          <a-space>
            <a @click="onEdit(record)">编辑</a>
          </a-space>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="open" title="乘车人" @ok="handleOk"
    okText="确认" cancelText="取消">
     <a-form  :model="passenger" :label-col="{span: 4}" :wrapper-col="{span: 14}">
       <a-form-item label="姓名">
         <a-input v-model:value="passenger.name"/>
       </a-form-item>
       <a-form-item label="身份证">
         <a-input v-model:value="passenger.idCard"/>
       </a-form-item>
       <a-form-item label="类型">
         <a-select v-model:value="passenger.type">
           <a-select-option value="1">成人</a-select-option>
           <a-select-option value="2">儿童</a-select-option>
           <a-select-option value="3">学生</a-select-option>
         </a-select>
       </a-form-item>
     </a-form>
    </a-modal>
  </div>

</template>

<style scoped>

</style>