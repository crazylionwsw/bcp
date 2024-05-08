import request from '../utils/request'

export function getAllBusinessManagers () {
  return request({
    url: '/employee/businessmanager',
    method: 'get'
  })
}

export function getEmployeeById (id) {
  return request({
    url: '/employee/' + id,
    method: 'get'
  })
}
