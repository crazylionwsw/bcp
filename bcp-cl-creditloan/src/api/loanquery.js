import request from '../utils/request'

export function getLoanQueryById (id) {
  return request({
    url: '/loanQuery/' + id,
    method: 'get'
  })
}

export function saveLoanQuery (creditloan) {
  return request({
    url: '/loanQuery/save',
    method: 'post',
    data: creditloan
  })
}
