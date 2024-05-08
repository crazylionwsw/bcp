import axios from 'axios'
import store from '../store'
import { getToken } from '../utils/auth'
import notify from '../utils/notice'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_URL, // api的base_url
  timeout: 10000                  // 请求超时时间
})

// request interceptor
service.interceptors.request.use(config => {
  // Do something before request is sent
  if (store.getters.token) {
    //  "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNzYwMDUzMDgwNyIsImF1ZGllbmNlIjoiUEMiLCJjcmVhdGVkIjoxNTIxNzExMDgxNTgzfQ.n110dDkoIfthmvT3K1PXWl91ILXWgxA5pQGOBlox9AFDQNeRGTgngz3a48UURm5_rmgpxJUuT-AxgXUw4IsyUg";
    config.headers['userToken'] = getToken()
  }
  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
})

// respone interceptor
service.interceptors.response.use(
  response => response,
  error => {
    console.log('err' + error)// for debug
    notify.showInfo('错误', error.msg,
      onShow => {
        console.log('Plugin: I\'m showing')
      },
      onHide => {
        console.log('Plugin: I\'m hiding')
      }
    )
    return Promise.reject(error)
  })

export default service
