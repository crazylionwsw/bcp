import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import vux from './modules/vux'
import getters from './getters'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    user,
    vux
  },
  getters,
  strict: process.env.NODE_ENV !== 'production'
})
