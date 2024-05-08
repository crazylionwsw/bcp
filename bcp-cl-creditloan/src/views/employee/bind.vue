<template>
  <div>
    <group>
      <x-input @on-blur="checkCell" title="手机号码" mask="999 9999 9999" v-model="cell" placeholder="请输入" is-type="china-mobile" required></x-input>
      <x-button style="margin-top: 5%;" :disable="!canBind" type="primary" @click.native="employeeBind">绑定</x-button>
    </group>
  </div>
</template>

<style>
</style>

<script>
  import { Group, XInput, XButton } from 'vux'

  //  API
  import { oauth2urlByCode } from '../../api/auth'
  import { checkEmployeeCell, bindEmployeeCell } from '../../api/employee'
  //Filter
  import { getRequestParam } from '../../filter/filter'

  export default{
    components: {
      Group,
      XInput,
      XButton
    },
    created (){
      this.getCodeFromUrl()
    },
    methods: {
      getCodeFromUrl () {
        console.log('执行[getCodeFromUrl]')
        if (getRequestParam().code) {
          console.log('[URL][code]===>' + getRequestParam().code)
          if (this.$store.getters.openid == null){
            console.log('[vuex]中没有[openid]')
            this.$store.dispatch('GetUserInfoByCode',{code: getRequestParam().code}).then(response => {
              this.openId = response.data.d.openid
              console.log('调用[vuex]的[Actions]，获取登陆的用户信息')
            })
          } else {
            console.log('从[vuex]获取登陆的用户信息')
            this.openId = this.$store.getters.openid
          }
        }
      },
      checkCell (){
        checkEmployeeCell(this.cell).then(response => {
          if (response.data && response.data.c == 9) {
            this.canBind = false
            this.$notify.showInfo('提示', '对不起！您不是分期经理！')
          } else {
            console.log("cell validate successful")
          }
        })
      },
      employeeBind () {
        const cell = this.cell
        bindEmployeeCell(cell, this.openId).then(response => {
          if (response.data && response.data.c == 0) {
            this.$notify.showInfo('绑定结果', '成功')
          } else if (response.data && response.data.c == 9) {
            this.$notify.showInfo('绑定结果', '失败：' + response.data.m)
          }
        })
      }
    },
    data () {
      return {
        cell: '',
        openId: null,
        canBind: true
      }
    }
  }
</script>
