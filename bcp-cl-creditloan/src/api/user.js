import request from '../utils/request'

export function bindUserCell (openId, cell, verificationCode) {
  return request({
    url: '/publicuser/bind',
    method: 'get',
    params: { openId, cell, verificationCode }
  })
}

export function loginByCellAndCode (cell, verificationCode) {
  return request({
    url: '/publicuser/login',
    method: 'get',
    params: { cell, verificationCode }
  })
}

export function loginByWechat (openId) {
  return request({
    url: '/publicuser/wlogin',
    method: 'get',
    params: { openId }
  })
}

export function registerUserByCell (cell, verificationCode) {
  return request({
    url: '/publicuser/reg',
    method: 'get',
    params: { cell, verificationCode }
  })
}
