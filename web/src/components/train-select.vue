<script setup>
const props = defineProps({
  modelValue: null,
  width: String
});
const emit = defineEmits(['update:modelValue', 'change']);

import {onMounted, ref, watch} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

const trainCode = ref();
const trains = ref([]);
const localWidth = ref(props.width);
if (Tool.isEmpty(props.width)) {
  localWidth.value = "100%";
}

// 利用watch，动态获取父组件的值，如果放在onMounted或其它方法里，则只有第一次有效
watch(() => props.modelValue, ()=>{
  console.log("props.modelValue", props.modelValue);
  trainCode.value = props.modelValue;
}, {immediate: true});

/**
 * 查询所有的车次，用于车次下拉框
 */
const queryAll = () => {
  let list = SessionStorage.get(SESSION_ALL_TRAIN);
  if(Tool.isNotEmpty(list)){
    console.log("queryAllTrain读取缓存");
    trains.value = list;
  } else {
    axios.get('/business/train/query-all').then(res => {
      let data = res.data;
      if (data.success) {
        trains.value= data.content;
        console.log("queryAllTrain保存缓存");
        SessionStorage.set(SESSION_ALL_TRAIN, trains.value);
      } else {
        notification.error({description: data.message});
      }
    });
  }
};

/**
 * 车次下拉框筛选
 */
const filterTrainCodeOption = (input, option) => {
  console.log(input, option);
  return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
};

/**
 * 将当前组件的值响应给父组件
 * @param value
 */
const onChange = (value) => {
  emit('update:modelValue', value);
  let train = trains.value.filter(item => item.code === value)[0];
  if (Tool.isEmpty(train)) {
    train = {};
  }
  emit('change', train);
};

onMounted(() => {
  queryAll();
});

</script>

<template>
  <a-select v-model:value="trainCode" showSearch allowClear
            :filterOption="filterTrainCodeOption"
            @change="onChange" placeholder="请选择车次"
            :style="'width: ' + localWidth">
    <a-select-option v-for="item in trains" :key="item.code" :value="item.code" :label="item.code + item.start + item.end">
      {{ item.code + ' | ' + item.start + ' ~ ' + item.end }}
    </a-select-option>
  </a-select>
</template>

