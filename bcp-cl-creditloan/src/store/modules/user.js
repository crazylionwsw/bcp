import { oauth2urlByCode, getInfo } from '../../api/auth'

const user = {
  state: {
    openid: null,
    subscribe: 0,
    headimgurl: null,
    nickname: null,
    cell: null,
    sharedOpenId: null
  },
  mutations: {
    SET_OPENID: (state, openid) => {
      console.log('设置[mutations][SET_OPENID]' + openid)
      state.openid = openid
    },
    SET_SUBSCRIBE: (state, subscribe) => {
      console.log('设置[mutations][SET_SUBSCRIBE]' + subscribe)
      state.subscribe = subscribe
    },
    SET_HEADIMGURL: (state, headimgurl) => {
      console.log('设置[mutations][SET_HEADIMGURL]' + headimgurl)
      if (headimgurl) state.headimgurl = headimgurl
    },
    SET_NICKNAME: (state, nickname) => {
      console.log('设置[mutations][SET_NICKNAME]' + nickname)
      if (nickname) state.nickname = nickname
    },
    SET_CELL: (state, cell) => {
      console.log('设置[mutations][SET_CELL]' + cell)
      if (cell) state.cell = cell
    },
    SET_SHAREDOPENID: (state, data) => {
      state.sharedOpenId = data.sharedOpenId
    }
  },
  actions: {
    GetUserInfoByCode ({ commit, state}, data ) {
      return new Promise((resolve, reject) => {
        console.log('[向vuex中设置当前登录人的信息][code]==>' + data.code)
        oauth2urlByCode(data.code).then(response => {
          if (response.data && response.data.d && response.data.d.openid) {
            window.localStorage.setItem('LOGIN_INFO_OPID', response.data.d.openid)
            window.localStorage.setItem('LOGIN_INFO_SS', response.data.d.subscribe)
            window.localStorage.setItem('LOGIN_INFO_HIU', response.data.d.headimgurl)
            window.localStorage.setItem('LOGIN_INFO_NN', response.data.d.nickname)
            window.localStorage.setItem('LOGIN_INFO_CL', response.data.d.cell)
            console.log('[登录人的openid]==>' + response.data.d.openid)
            console.log('[登录人的subscribe]==>' + response.data.d.subscribe)
            console.log('[登录人的headimgurl]==>' + response.data.d.headimgurl)
            console.log('[登录人的nickname]==>' + response.data.d.nickname)
            console.log('[登录人的cell]==>' + response.data.d.cell)
            commit('SET_SUBSCRIBE', response.data.d.subscribe)
            commit('SET_OPENID', response.data.d.openid)
            commit('SET_HEADIMGURL', response.data.d.headimgurl)
            commit('SET_NICKNAME', response.data.d.nickname)
            commit('SET_CELL', response.data.d.cell)
            resolve(response)
          } else {
            console.log('获取用户信息失败')
            reject()
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    SetSharedOpenId ({ commit, state}, data) {
      commit('SET_SHAREDOPENID', data.sharedOpenId)
    },
    GetUserInfoByOpenId ({ commit, state }, data) {
      return new Promise((resolve, reject) => {
        getInfo(data.openId).then(response => {
          if (response.data && response.data.c === 0 && response.data.d){
            commit('SET_SUBSCRIBE', response.data.d.subscribe)
            resolve(response)
          } else {
            console.log('获取用户信息失败')
            reject()
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    SetUserInfo ({commit, state}, data) {
      return new Promise((resolve, reject) => {
        try {
          console.log('设置用户信息')
          commit('SET_SUBSCRIBE', data.subscribe)
          commit('SET_OPENID', data.openid)
          commit('SET_HEADIMGURL', data.headimgurl)
          commit('SET_NICKNAME', data.nickname)
          commit('SET_CELL', data.cell)
          resolve(data)
        } catch (e) {
          reject(e)
        }
      })
    }
  }
}

export default user
