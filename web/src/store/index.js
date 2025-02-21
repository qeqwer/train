import { createStore } from 'vuex'

export default createStore({
  state: {
    member:{}
  },
  getters: {
  },
  mutations: {
    setMember(state,_member){
      state.member = _member
    }
  },
  // 异步任务
  actions: {
  },
  modules: {
  }
})
