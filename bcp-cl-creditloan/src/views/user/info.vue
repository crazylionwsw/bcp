<template>
  <div id="info_div">
    <divider>用户信息</divider>
    <p class="info_p_class">
      <x-img class="header_class" :src="headimgurl" :default-src="defaultUserImageSrc"></x-img>
      <span class="nick_class" v-if="loginType === 'WXL'"><br>{{nickname}}</span>
      <span class="cell_class" v-if="loginType === 'WXL' && cell != null && cell !== '' && cell !== 'null'"><br>已绑定手机号:  {{cell}}</span>
      <span class="cell_class" v-if="loginType === 'CL' && cell != null && cell !== '' && cell !== 'null'"><br>您的手机号:  {{cell}}</span>
    </p>
    <p class="button_p_class">
      <x-button v-if="loginType === 'WXL' && (cell == null || cell === 'null' || cell === '')" @click.native="bindUserCell" class="cell_button">绑定手机号</x-button>
      <x-button v-if="loginType === 'WXL' && cell != null && cell !== 'null' && cell !== ''" @click.native="bindUserCell" class="cell_button">更改手机号</x-button>
      <x-button v-if="loginType === 'WXL'" @click.native="exitLogin" class="cell_button">退出登录</x-button>
    </p>
  </div>
</template>

<style lang="less">
  #info_div {
    .info_p_class {
      margin-top: 5%;
      text-align: center;
      .header_class {
        width: 30%;
        height: 30%;
      }
      .nick_class {
        text-align: center;
      }
      .cell_class {
      }
    }
    .button_p_class {
      .cell_button {
        width: 95%;
        background-color: #e66131;
        margin-top: 10%;
      }
    }
  }
</style>

<script>
  import {XImg, Group, GroupTitle, XInput, XButton, Divider } from 'vux'

  //Filter
  import { getRequestParam } from '../../filter/filter'

  export default{
    name: 'UserInfo',
    components: {
      XImg,
      Group,
      GroupTitle,
      XInput,
      XButton,
      Divider
    },
    created (){
      this.initLoginType()
      this.getCodeFromUrl()
    },
    methods: {
      initLoginType(){
        if (this.$route.query.lt) {
          console.log('[query][lt]===>' + this.$route.query.lt)
          this.loginType = this.$route.query.lt
        }
      },
      getCodeFromUrl () {
        if (getRequestParam().code  && this.$store.getters.openid == null && !window.localStorage.getItem('LOGIN_INFO_OPID')) {
          console.log('[用户信息页面][URL][code]===>' + getRequestParam().code)
          console.log('[vuex][openid]===>' + this.$store.getters.openid)
          console.log('[vuex][headimgurl]===>' + this.$store.getters.headimgurl)
          console.log('[vuex][nickname]===>' + this.$store.getters.nickname)
          console.log('[vuex][cell]===>' + this.$store.getters.cell)
          this.$store.dispatch('GetUserInfoByCode',{code: getRequestParam().code}).then(response => {
            console.log('[openid]==>' + response.data.d.openid)
            console.log('[headimgurl]==>' + response.data.d.headimgurl)
            console.log('[nickname]==>' + response.data.d.nickname)
            console.log('[cell]==>' + response.data.d.cell)
            this.openId = response.data.d.openid
            this.headimgurl = response.data.d.headimgurl
            this.nickname = response.data.d.nickname
            this.cell = response.data.d.cell
            this.loginType = 'WXL'
          })
        } else {
          this.initUserInfo()
        }
      },
      initUserInfo(){
        if (this.$store.getters.openid == null && window.localStorage.getItem('LOGIN_INFO_OPID')){
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
        console.log('调用[initUserInfo][loginType]===>' + this.loginType)
        this.openId = this.$store.getters.openid
        console.log('调用[initUserInfo][openId]===>' + this.openId)
        this.headimgurl = this.loginType === 'WXL' ? this.$store.getters.headimgurl : '/static/cell.png'
        console.log('调用[initUserInfo][headimgurl]===>' + this.headimgurl)
        this.defaultUserImageSrc = this.loginType === 'WXL' ? this.$store.getters.headimgurl : '/static/cell.png'
        console.log('调用[initUserInfo][defaultUserImageSrc]===>' + this.defaultUserImageSrc)
        this.nickname = this.loginType === 'WXL' ? this.$store.getters.nickname : this.$store.getters.cell
        console.log('调用[initUserInfo][nickname]===>' + this.nickname)
        this.cell = this.$store.getters.cell
        console.log('调用[initUserInfo][cell]===>' + this.cell)
        console.log('调用[initUserInfo][cell]===>' + this.cell === null)
        console.log('调用[initUserInfo][cell]===>' + this.cell === 'null')
      },
      bindUserCell() {
        this.$router.push({path: 'bind', query: {'originalCell': this.cell}})
      },
      exitLogin() {
        window.localStorage.clear()
        this.$router.push('/user/login')
      },
    },
    data () {
      return {
        openId: null,
        headimgurl: null,
        defaultUserImageSrc: '/static/cell.png',
        nickname: null,
        cell: null,
        loginType: 'WXL'  //登录方式：微信登录(WXL)、手机号登录(CL)
      }
    }
  }
</script>
