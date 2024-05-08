const getters = {
  token: state => state.user.token,
  loginStatus: state => state.user.loginStatus,
  userID: state => state.user.userID,
  userName: state => state.user.userName,
  isSystem: state => state.user.isSystem,
  employee: state => state.user.employee,
  employeeId: state => state.user.employee.id,
  loading: state => state.vux.loading,
  showBack: state => state.vux.showBack,
  title: state => state.vux.title
}
export default getters
