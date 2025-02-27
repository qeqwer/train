<script setup>
import {onMounted, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

const open = ref(false);
const loading = ref(false);
<#list fieldList as field>
<#if field.enums>
const ${field.enumsConst}_ARRAY = window.${field.enumsConst}_ARRAY;
</#if>
</#list>

const ${domain} = ref({
<#list fieldList as field>
  ${field.nameHump}: undefined,
</#list>
});

// 分页的三个属性名是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 3,
});

const ${domain}list = ref([]);
// let passengerlist = reactive({list:[]});

const columns = [
<#list fieldList as field>
<#if field.name!="id" && field.nameHump!="createTime" && field.nameHump!="updateTime">
  {
    title: '${field.nameCn}',
    dataIndex: '${field.nameHump}',
    key: '${field.nameHump}',
  },
</#if>
</#list>
<#if !readOnly>
  {
    title: '操作',
    dataIndex: 'operation'
  }
</#if>
];

const handleQuery = (param) => {
  if(!param){
    param={
      page: 1,
      size: pagination.value.pageSize
    };
  }
  loading.value = true;
  axios.get('/${module}/admin/${do_main}/query-list', {
    params: {
      page: param.page,
      size: param.size,
    }
  }).then((res) => {
    loading.value = false;
    let data = res.data;
    if (data.success) {
      ${domain}list.value = data.content.list;
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

<#if !readOnly>
const onAdd = () => {
  ${domain}.value = {};
  open.value = true;
};

const onEdit = (record) => {
  ${domain}.value = window.Tool.copy(record);
  open.value = true;
};

const onDelete = (record) => {
  axios.delete('/${module}/admin/${do_main}/delete/' + record.id).then(res => {
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
  axios.post('/${module}/admin/${do_main}/save', ${domain}.value).then(res => {
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
</#if>

onMounted(() =>{handleQuery({page: 1, size: pagination.value.pageSize});});
</script>

<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <#if !readOnly><a-button type="primary" @click="onAdd">新增</a-button></#if>
    </a-space>
  </p>
  <a-table :dataSource="${domain}list"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{column, record}">
      <template v-if="column.dataIndex === 'operation'">
        <#if !readOnly>
        <a-space>
          <a-popconfirm
              title="删除后不可恢复，确认删除?"
              @confirm="onDelete(record)"
              ok-text="确认" cancel-text="取消">
            <a style="color: red">删除</a>
          </a-popconfirm>
          <a @click="onEdit(record)">编辑</a>
        </a-space>
        </#if>
      </template>
      <#list fieldList as field>
        <#if field.enums>
      <template v-else-if="column.dataIndex === '${field.nameHump}'">
        <span v-for="item in ${field.enumsConst}_ARRAY" :key="item.key">
          <span v-if="item.key === record.${field.nameHump}">
            {{item.value}}
          </span>
        </span>
      </template>
        </#if>
      </#list>
    </template>
  </a-table>
  <#if !readOnly>
  <a-modal v-model:open="open" title="${tableNameCn}" @ok="handleOk"
           ok-text="确认" cancel-text="取消">
    <a-form :model="${domain}" :label-col="{span: 4}" :wrapper-col="{span: 20}">
      <#list fieldList as field>
        <#if field.name!="id" && field.nameHump!="createTime" && field.nameHump!="updateTime">
      <a-form-item label="${field.nameCn}">
        <#if field.enums>
        <a-select v-model:value="${domain}.${field.nameHump}">
          <a-select-option v-for="item in ${field.enumsConst}_ARRAY" :key="item.key" :value="item.key">
            {{item.value}}
          </a-select-option>
        </a-select>
        <#elseif field.javaType=='Date'>
          <#if field.type=='time'>
        <a-time-picker v-model:value="${domain}.${field.nameHump}" valueFormat="HH:mm:ss" placeholder="请选择时间"/>
          <#elseif field.type=='date'>
        <a-date-picker v-model:value="${domain}.${field.nameHump}" valueFormat="YYYY-MM-DD" placeholder="请选择日期"/>
          <#else>
        <a-date-picker v-model:value="${domain}.${field.nameHump}" valueFormat="YYYY-MM-DD HH:mm:ss" show-time placeholder="请选择日期"/>
          </#if>
        <#else>
        <a-input v-model:value="${domain}.${field.nameHump}"/>
        </#if>
      </a-form-item>
        </#if>
      </#list>
    </a-form>
  </a-modal>
  </#if>
</template>

<style scoped>

</style>