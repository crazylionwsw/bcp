import { getUserInfo, getUserInfoByCode } from '../../api/login'
import { getToken, setToken, removeToken } from '../../utils/auth'

const user = {
  state: {
    token: getToken(),
    userID: '',
    userName: '',
    isSystem: '',
    employeeId: '',
    employee: {}
  },
  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token.slice()
      setToken(token.slice())
    },
    SET_USERID: (state, userID) => {
      state.userID = userID.slice()
    },
    SET_USERNAME: (state, userName) => {
      state.userName = userName.slice()
    },
    SET_ISSYSTEM: (state, isSystem) => {
      state.isSystem = isSystem.slice()
    },
    SET_EMPLOYEE: (state, employee) => {
      state.employee = Object.assign({}, employee)
    },
    SET_EMPLOYEEID: (state, employeeId) => {
      state.employeeId = employeeId.slice()
    },
    FETCH_USER: (state, user) => {
      state.user = user
    }
  },
  actions: {
    // 获取用户信息
    GetUserInfo ({ commit, state }) {
      return new Promise((resolve, reject) => {
        getUserInfo(state.token).then(response => {
          if (!response.data) {
          }
          const data = response.data
          commit('SET_USERID', data.userID)
          commit('SET_USERNAME', data.userName)
          commit('SET_ISSYSTEM', data.isSystem)
          commit('SET_EMPLOYEE', data.employee)
          commit('SET_EMPLOYEEID', data.employee.id)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    GetUserInfoByCode ({ commit, state, code }) {
      return new Promise((resolve, reject) => {
        getUserInfoByCode(code).then(response => {
          if (!response.data) {
          }
          const data = response.data
          commit('SET_TOKEN', data.token)
          commit('SET_USERID', data.userID)
          commit('SET_USERNAME', data.userName)
          commit('SET_ISSYSTEM', data.isSystem)
          commit('SET_EMPLOYEE', data.employee)
          commit('SET_EMPLOYEEID', data.employee.id)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}

export default user
