const vux = {
  state: {
    loading: false,
    showBack: true,
    title: ''
  },
  mutations: {
    updateLoading: (state, loading) => {
      state.loading = loading
    },
    updateShowBack: (state, showBack) => {
      state.showBack = showBack
    },
    updateTitle: (state, title) => {
      state.title = title.slice()
    }
  },
  actions: {
    // 更新加载状态
    UpdateLoading ({commit, state}, loading) {
      commit('updateLoading', loading)
    },
    //  更新返回按钮的样式
    UpdateShowBack ({commit, state}, showBack) {
      commit('updateShowBack', showBack)
    }
  }
}

export default vux
