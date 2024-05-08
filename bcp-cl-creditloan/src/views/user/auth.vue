<template>
    <div>
    </div>
</template>

<style>
</style>

<script>
  //  API
  import { oauth2 } from '../../api/auth'
  //Filter
  import { getRequestParam } from '../../filter/filter'

  export default{
    created () {
      this.doEmployeeOauth2()
    },
    methods: {
      doEmployeeOauth2 (){
        console.log('方法调用：doEmployeeOauth2')
        oauth2('#/user/info', 'snsapi_userinfo').then(response => {
          if (response.data.d !== undefined ) {
            console.log('response.data.d ====' + response.data.d )
            window.location.replace(response.data.d)
          } else {
            this.$notify.showInfo('错误', '微信服务器返回异常')
          }
        })
      }
    }
  }
</script>
