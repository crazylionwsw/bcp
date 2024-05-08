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
      this.doOauth2()
    },
    methods: {
      doOauth2 (){
        console.log('调用doOauth2')
        oauth2('#/creditloan/active', 'snsapi_base').then(response => {
          if (response.data.d !== undefined) {
            console.log('后台返回的重定向的url ==> ' + response.data.d.replace('STATE', getRequestParam().sharedOpenId ? getRequestParam().sharedOpenId : 'STATE') + '  | 从url中获取state ==>' + getRequestParam().state)
            if (getRequestParam().sharedOpenId) {
              console.log('从分享链接进入  |  从url中获取sharedOpenId:' + getRequestParam().sharedOpenId)
              window.localStorage.setItem('sharedOpenId', getRequestParam().sharedOpenId)
              /*this.$store.dispatch('SetSharedOpenId', {sharedOpenId: getRequestParam().sharedOpenId}).then(res => {

              })*/
              window.location.replace(response.data.d.replace('STATE', getRequestParam().sharedOpenId))
            } else {
              if (window.localStorage.getItem('sharedOpenId')) {
                console.log('从localStorage中获取sharedOpenId:' + window.localStorage.getItem('sharedOpenId'))
                //this.$store.dispatch('SetSharedOpenId', {sharedOpenId: window.localStorage.getItem('sharedOpenId')}).then(res => {
                  window.location.replace(response.data.d.replace('STATE', window.localStorage.getItem('sharedOpenId')))
                //}
              } else {
                window.location.replace(response.data.d)
              }
            }
          } else {
            this.$notify.showInfo('错误', '微信服务器返回异常')
          }
        })
      }
    }
  }
</script>
