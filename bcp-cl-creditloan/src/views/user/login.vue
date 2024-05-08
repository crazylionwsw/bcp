<template>
  <div id="login_div">
    <divider>登录</divider>
    <group>
      <x-input class="cell_class" v-model="cell" placeholder="请输入手机号" type="number" keyboard="number" is-type="china-mobile" @on-blur="inputCell" required></x-input>
      <x-input class="code_class" v-model="verificationCode" placeholder="请输入验证码" type="number" keyboard="number" @on-blur="inputVerificationCode" required>
        <x-button class="vc_button_class" slot="right" plain @click.native="sendCode" :text="buttonText" :disabled="buttonDisabled" mini></x-button>
      </x-input>
    </group>

    <x-button class="login_btn" @click.native="login">登录</x-button>
    <x-button class="reg_btn" plain link="/user/reg">注册</x-button>

    <divider class="divider_class">更多登录方式</divider>
    <span @click="wechatLogin">
      <x-img class="wechat_img_class" src="/static/wechat.png"></x-img>
    </span>
  </div>
</template>

<style lang="less">
  #login_div {
    .cell_class {
      height: 32px;
    }
    .code_class {
      height: 32px;
    }
    .vc_button_class{
      font-size: 1em;
      color: #e66131;
      border-color: white;
    }
    .login_btn {
      margin-top: 5%;
      width: 95%;
      background-color: #e66131;
    }
    .reg_btn {
      width: 95%;
      border-color: #e66131;
    }
    .divider_class {
      margin-top: 45%;
    }
    .wechat_img_class {
      height: 15%;
      width: 15%;
      margin-left: 42%;
    }
  }
</style>

<script>
  import { Group, GroupTitle, XInput, XButton, Flexbox, FlexboxItem, Divider, Card, XImg } from 'vux'
  import isMobilePhone from 'validator/lib/isMobilePhone'

  //  API
  import { sendMsgCode } from '../../api/sysparam'
  import { loginByCellAndCode } from '../../api/user'

  export default{
    name: 'Login',
    components: {
      Group,
      GroupTitle,
      XInput,
      XButton,
      Flexbox,
      FlexboxItem,
      Divider,
      Card,
      XImg
    },
    created (){
      this.initLoginInfo()
    },
    methods: {
      initLoginInfo () {
        if (window.localStorage.getItem('LOGIN_INFO_OPID')) {
          console.log('从localStorage中获取保存的用户信息')
          this.$store.dispatch('SetUserInfo', {
            openid: window.localStorage.getItem('LOGIN_INFO_OPID'),
            subscribe: window.localStorage.getItem('LOGIN_INFO_SS'),
            headimgurl: window.localStorage.getItem('LOGIN_INFO_HIU'),
            nickname: window.localStorage.getItem('LOGIN_INFO_NN'),
            cell: window.localStorage.getItem('LOGIN_INFO_CL'),
          }).then(response => {
            console.log('[vuex][openid]===>' + this.$store.getters.openid)
            console.log('[vuex][subscribe]===>' + this.$store.getters.subscribe)
            console.log('[vuex][headimgurl]===>' + this.$store.getters.headimgurl)
            console.log('[vuex][nickname]===>' + this.$store.getters.nickname)
            console.log('[vuex][cell]===>' + this.$store.getters.cell)
            this.$router.push({path: '/user/info', query: {lt: 'WXL'}})
          })
        }
      },
      inputCell (){
        if (this.cell == null || this.cell === '') {
          this.$notify.showInfo('提示', '请输入手机号！')
          return;
        }
        if (this.originalCell != null && this.cell === this.originalCell) {
          this.$notify.showInfo('提示', '当前手机号与已绑定手机号一致，请重新输入！')
          return;
        }
        if (!isMobilePhone(this.cell, 'zh-CN')) {
          this.$notify.showInfo('提示', '手机号格式错误！')
          return;
        }
      },
      inputVerificationCode() {
        if (!this.buttonDisabled) {
          this.$notify.showInfo('提示', '请先获取验证码！')
          return;
        }
        if (this.buttonDisabled && (this.verificationCode == null || this.verificationCode === '')){
          this.$notify.showInfo('提示', '请输入验证码！')
          return;
        }
      },
      sendCode() {
        if (this.cell == null || this.cell === '') {
          this.$notify.showInfo('提示', '请输入手机号！')
          return;
        }
        if (this.originalCell != null && this.cell === this.originalCell) {
          this.$notify.showInfo('提示', '当前手机号与已绑定手机号一致，请重新输入！')
          return;
        }
        if (!isMobilePhone(this.cell, 'zh-CN')) {
          this.$notify.showInfo('提示', '手机号格式错误！')
          return;
        }
        this.buttonDisabled = true
        sendMsgCode(this.cell).then(response => {
          if (response.data && response.data.c === 0) {
            this.$notify.showInfo('提示', '发送成功！')
            const interval = setInterval(() => {
              if ((this.time--) <= 0) {
                this.buttonText = '重新获取验证码'
                this.buttonDisabled = false
                this.time = 60;
                clearInterval(interval);
                return
              }
              this.buttonText = this.time + '秒后重新获取'
            }, 1000);
          } else if (response.data && response.data.c === 9) {
            this.$notify.showInfo('提示', response.data.m)
          } else {
            this.$notify.showInfo('提示', '验证码发送失败！')
          }
        })
      },
      login (){
        if (this.cell == null || this.cell === '') {
          this.$notify.showInfo('提示', '请输入手机号！')
          return;
        }
        if (this.originalCell != null && this.cell === this.originalCell) {
          this.$notify.showInfo('提示', '当前手机号与已绑定手机号一致，请重新输入！')
          return;
        }
        if (!isMobilePhone(this.cell, 'zh-CN')) {
          this.$notify.showInfo('提示', '手机号格式错误！')
          return;
        }
        if (!this.buttonDisabled) {
          this.$notify.showInfo('提示', '请先获取验证码！')
          return;
        }
        if (this.buttonDisabled && (this.verificationCode == null || this.verificationCode === '')){
          this.$notify.showInfo('提示', '请输入验证码！')
          return;
        }
        loginByCellAndCode(this.cell, this.verificationCode).then(response => {
          if (response.data.c === 0) {
            this.$store.commit('SET_CELL',this.cell)
            this.$notify.showInfo('提示', '登录成功！')
            this.$router.push({path: '/user/info', query: {lt: 'CL'}})
            console.log('登录成功')
          } else if (response.data.c === 9) {
            this.$notify.showInfo('提示', response.data.m)
          } else {
            this.$notify.showInfo('提示', '登录失败！')
          }
        })
      },
      wechatLogin () {
        console.log('微信登录')
        this.$router.push('/user/auth')
      }
    },
    data () {
      return {
        buttonText: '获取验证码',
        time: 60,
        cell: null,
        verificationCode: null,
        buttonDisabled: false
      }
    }
  }
</script>
