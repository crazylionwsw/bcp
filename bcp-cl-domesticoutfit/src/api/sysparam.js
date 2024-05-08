import request from '../utils/request'

export function getSysParamByCode (code) {
  return request({
    url: '/sysparam/' + code,
    method: 'get'
  })
}
