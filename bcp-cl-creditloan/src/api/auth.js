import request from '../utils/request'

export function oauth2 (url, sab) {
  return request({
    url: '/oauth2',
    method: 'get',
    params: { url, sab }
  })
}

export function oauth2urlByCode (code) {
  return request({
    url: '/oauth2url/' + code,
    method: 'get'
  })
}

export function getInfo(openId) {
  return request({
    url: '/getInfo',
    method: 'get',
    params: { openId }
  })
}

export function getSignatureInfo (url) {
  return request({
    url: '/getSignatureInfo',
    method: 'get',
    params: { url }
  })
}
