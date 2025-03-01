<script setup>
import {onMounted, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";
import {pinyin} from "pinyin-pro";

const open = ref(false);
const loading = ref(false);

const station = ref({
  id: undefined,
  name: undefined,
  namePinyin: undefined,
  namePy: undefined,
  createTime: undefined,
  updateTime: undefined,
});

// 分页的三个属性名是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 10,
});

const stationlist = ref([]);
// let passengerlist = reactive({list:[]});

const columns = [
  {
    title: '站名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '站名拼音',
    dataIndex: 'namePinyin',
    key: 'namePinyin',
  },
  {
    title: '站名拼音首字母',
    dataIndex: 'namePy',
    key: 'namePy',
  },
  {
    title: '操作',
    dataIndex: 'operation'
  }
];

watch(()=>station.value.name, ()=>{
  if(Tool.isNotEmpty(station.value.name)){
    station.value.namePinyin = pinyin(station.value.name,{toneType:'none'})
        .replace(" ", "");
    station.value.namePy = pinyin(station.value.name,{pattern: "first",toneType:'none'})
        .replace(" ", "");
  } else {
    station.value.namePinyin = "";
    station.value.namePy = "";
  }
}, {immediate: true});

const handleQuery = (param) => {
  if(!param){
    param={
      page: 1,
      size: pagination.value.pageSize
    };
  }
  loading.value = true;
  axios.get('/business/admin/station/query-list', {
    params: {
      page: param.page,
      size: param.size,
    }
  }).then((res) => {
    loading.value = false;
    let data = res.data;
    if (data.success) {
      stationlist.value = data.content.list;
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
  station.value = {};
  open.value = true;
};

const onEdit = (record) => {
  station.value = window.Tool.copy(record);
  open.value = true;
};

const onDelete = (record) => {
  axios.delete('/business/admin/station/delete/' + record.id).then(res => {
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
  axios.post('/business/admin/station/save', station.value).then(res => {
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
  <a-table :dataSource="stationlist"
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
  <a-modal v-model:open="open" title="车站" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="station" :label-col="{span: 4}" :wrapper-col="{span: 20}">
      <a-form-item label="站名">
        <a-input v-model:value="station.name"/>
      </a-form-item>
      <a-form-item label="站名拼音">
        <a-input v-model:value="station.namePinyin" disabled/>
      </a-form-item>
      <a-form-item label="拼音首字母">
        <a-input v-model:value="station.namePy" disabled/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<style scoped>

</style>