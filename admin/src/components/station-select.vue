<script setup>
const props = defineProps({
  modelValue: null,
  width: String
});
const emit = defineEmits(['update:modelValue', 'change']);

import {onMounted, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

const name = ref();
const stations = ref([]);
const localWidth = ref(props.width);
if (Tool.isEmpty(props.width)) {
  localWidth.value = "100%";
}

// 利用watch，动态获取父组件的值，如果放在onMounted或其它方法里，则只有第一次有效
watch(() => props.modelValue, ()=>{
  console.log("props.modelValue", props.modelValue);
  name.value = props.modelValue;
}, {immediate: true});

/**
 * 查询所有的车站，用于车次下拉框
 */
const queryAll = () => {
  let list = SessionStorage.get(SESSION_ALL_STATION);
  if (Tool.isNotEmpty(list)){
    console.log("queryAllStation读取缓存")
    stations.value = list;
  } else {
    axios.get('/business/admin/station/query-all').then(res => {
      let data = res.data;
      if (data.success) {
        stations.value= data.content;
        console.log("queryAllStation保存缓存");
        SessionStorage.set(SESSION_ALL_STATION, stations.value);
      } else {
        notification.error({description: data.message});
      }
    });
  }
};

/**
 * 车次下拉框筛选
 */
const filterNameOption = (input, option) => {
  console.log(input, option);
  return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};

/**
 * 将当前组件的值响应给父组件
 * @param value
 */
const onChange = (value) => {
  emit('update:modelValue', value);
  let station = stations.value.filter(item => item.name === value)[0];
  if (Tool.isEmpty(station)) {
    station = {};
  }
  emit('change', station);
};

onMounted(() => {
  queryAll();
});

</script>

<template>
  <a-select v-model:value="name" showSearch allowClear
            :filterOption="filterNameOption"
            @change="onChange" placeholder="请选择车站"
            :style="'width: ' + localWidth">
    <a-select-option v-for="item in stations" :key="item.name" :value="item.name" :label="item.name + item.namePinyin + item.namePy">
      {{ item.name + ' | ' + item.namePinyin + ' | ' + item.namePy }}
    </a-select-option>
  </a-select>
</template>

