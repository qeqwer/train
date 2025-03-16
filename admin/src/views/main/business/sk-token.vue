<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

const open = ref(false);
const loading = ref(false);

const skToken = ref({
  id: undefined,
  date: undefined,
  trainCode: undefined,
  count: undefined,
  createTime: undefined,
  updateTime: undefined,
});

// 分页的三个属性名是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 3,
});

const skTokenlist = ref([]);
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
    title: '令牌余量',
    dataIndex: 'count',
    key: 'count',
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
  axios.get('/business/admin/sk-token/query-list', {
    params: {
      page: param.page,
      size: param.size,
    }
  }).then((res) => {
    loading.value = false;
    let data = res.data;
    if (data.success) {
      skTokenlist.value = data.content.list;
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

const onAdd = () => {
  skToken.value = {};
  open.value = true;
};

const onEdit = (record) => {
  skToken.value = window.Tool.copy(record);
  open.value = true;
};

const onDelete = (record) => {
  axios.delete('/business/admin/sk-token/delete/' + record.id).then(res => {
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
  axios.post('/business/admin/sk-token/save', skToken.value).then(res => {
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
  <a-table :dataSource="skTokenlist"
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
    </template>
  </a-table>
  <a-modal v-model:open="open" title="秒杀令牌" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="skToken" :label-col="{span: 4}" :wrapper-col="{span: 20}">
      <a-form-item label="日期">
        <a-date-picker v-model:value="skToken.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期"/>
      </a-form-item>
      <a-form-item label="车次编号">
        <a-input v-model:value="skToken.trainCode"/>
      </a-form-item>
      <a-form-item label="令牌余量">
        <a-input v-model:value="skToken.count"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<style scoped>

</style>