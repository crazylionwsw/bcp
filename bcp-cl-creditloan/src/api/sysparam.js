import request from '../utils/request'

export function getSysParamByCode (code) {
  return request({
    url: '/sysparam/' + code,
    method: 'get'
  })
}

export function sendMsgCode (cell) {
  return request({
    url: '/code',
    method: 'get',
    params: { cell }
  })
}
