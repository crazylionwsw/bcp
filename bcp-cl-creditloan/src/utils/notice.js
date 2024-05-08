import Vue from 'vue'

const notify = {
  showInfo: (title = '提示', content, showFn, hideFn) => {
    return Vue.$vux.alert.show({
      title: title,
      content: content,
      onShow () {
        if (typeof  showFn === 'function') {
          showFn()
        }
      },
      onHide () {
        if (typeof  hideFn === 'function') {
          hideFn()
        }
      }
    })
  },
  loading: (text = 'Loading') => {
    return Vue.$vux.loading.show({
      text: text
    })
  },
  closeLoading: () => {
    Vue.$vux.loading.hide()
  }
}

export default notify
