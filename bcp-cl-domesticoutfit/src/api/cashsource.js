import request from '../utils/request'

export function getCashSources () {
  return request({
    url: '/cashsources',
    method: 'get'
  })
}
