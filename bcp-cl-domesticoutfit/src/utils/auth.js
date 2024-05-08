import {cookie} from 'vux'

const TokenKey = 'User-Token'

export function getToken () {
  return cookie.get(TokenKey)
}

export function setToken (token) {
  return cookie.set(TokenKey, token)
}

export function removeToken () {
  return cookie.remove(TokenKey)
}
