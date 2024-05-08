import request from '../utils/request'

export function getUserInfo (token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}

export function getUserInfoByCode (code) {
  return request({
    url: '/oauth2url',
    method: 'get',
    params: { code }
  })
}
