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

export function checkEmployeeCell (cell) {
  return request({
    url: '/employee/check',
    method: 'get',
    params: { cell }
  })
}

export function bindEmployeeCell (cell, openId) {
  return request({
    url: '/employee/bind',
    method: 'get',
    params: { cell, openId }
  })
}
