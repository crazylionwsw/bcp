const getters = {
  openid: state => state.user.openid,
  subscribe: state => state.user.subscribe,
  headimgurl: state => state.user.headimgurl,
  nickname: state => state.user.nickname,
  cell: state => state.user.cell,
  sharedOpenId: state => state.user.sharedOpenId
}
export default getters
