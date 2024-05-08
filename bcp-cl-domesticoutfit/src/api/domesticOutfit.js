import request from '../utils/request'

export function getPagesDomesticOutfit (currentPage, pageSize, isPass) {
  const data = {
    currentPage,
    pageSize,
    isPass
  }
  return request({
    url: '/domesticoutfits',
    method: 'get',
    params: data
  })
}

export function getDomesticOutfitById (id) {
  return request({
    url: '/domesticoutfit/' + id,
    method: 'get'
  })
}

export function saveDomesticOutfit (domesticoutfit) {
  return request({
    url: '/domesticoutfit',
    method: 'post',
    data: domesticoutfit
  })
}
