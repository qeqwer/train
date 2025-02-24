<script setup>
import {reactive, ref} from 'vue';
import axios from "axios";
import {notification} from "ant-design-vue";

const open = ref(false);

// reactive仅适用于对象
const passenger = reactive({
  id: undefined,
  memberID: undefined,
  name: undefined,
  idCard: undefined,
  type: undefined,
  createTime: undefined,
  updateTime: undefined,
});

const showModal = () => {
  open.value = true;
};

const handleOk = () => {
  axios.post('/member/passenger/save', passenger).then(res => {
    let data = res.data;
    if (data.success) {
      notification.success({description: '保存成功！'});
      open.value = false;
    } else {
      notification.error({description: data.message});
    }
  })
};
</script>

<template>
  <div>
    <a-button type="primary" @click="showModal">新增</a-button>
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