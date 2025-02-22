import { createRouter, createWebHistory } from 'vue-router'
import store from "@/store";
import {notification} from "ant-design-vue";

const routes = [
  {
    path: '/login',
    component: () => import( '../views/login.vue')
  },
  {
    path: '/',
    component: () => import( '../views/main.vue'),
    meta:{requireAuth:true}
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.matched.some(function (record) {
    console.log(record, "是否需要登录校验：", record.meta.requireAuth || false);
    return record.meta.requireAuth;
  })) {
    const _member = store.state.member;
    console.log("页面登录校验开始：",_member);
    if (!_member.token) {
      console.log("用户未登录或登录超时！");
      notification.error({ description: "未登录或登录超时" });
      next('/login');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router

