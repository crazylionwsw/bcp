import axios from 'axios'
import notify from '../utils/notice'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_URL, // api的base_url
  timeout: 10000                  // 请求超时时间
})

// request interceptor
service.interceptors.request.use(config => {
  // Do something before request is sent

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
    console.log('err' + response)// for debug
    notify.showInfo('错误', response.data.m)
    return Promise.reject(error)
  })

export default service
