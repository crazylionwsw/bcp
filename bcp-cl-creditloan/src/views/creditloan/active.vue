<template>
    <div id="active_div">
      <x-img :src="activeOneSrc" class="image-class" container="#vux_view_box_body"></x-img>
      <x-img :src="activeTwoSrc" class="image-class" container="#vux_view_box_body"></x-img>
      <!--<x-img :src="codeSrc" class="image-class" container="#vux_view_box_body"></x-img>-->
      <x-button class="apply-button" @click.native="apply">开始薅羊毛</x-button>
    </div>
</template>

<style lang="less">
  #active_div
    .apply-button {
      width: 80%;
      height: 10%;
      margin-left: 10%;
      margin-top: -20%;
      background-color: #fae54e;
      border-color: white;
      text-align: center;
      font-weight: 600;
      letter-spacing: 0.2em;
      font-family: 'Microsoft YaHei';
      color: #29250d;
    }
</style>

<script>
  import { XButton, XImg } from 'vux'

  import { wxShare } from '../../utils/wxShare'

  //Filter
  import { getRequestParam } from '../../filter/filter'

  export default{
    components: {
      XButton,
      XImg
    },
    created () {
      this.setShareOpenId()
      this.getCodeFromUrl()
    },
    methods: {
      setShareOpenId () {
        console.log('调用[initShareOpenId]')
        if (getRequestParam().state !== 'STATE') {
          console.log('[URL][state]==>' + getRequestParam().state)
          this.$store.commit('SET_SHAREDOPENID', {sharedOpenId: getRequestParam().state})
          this.shareOpenId = getRequestParam().state
        } else {
          this.shareOpenId = 'STATE'
        }
      },
      getCodeFromUrl () {
        console.log('调用[getCodeFromUrl]')

        if (getRequestParam().code) {
          console.log('[URL][code]===>' + getRequestParam().code)
          if (this.$store.getters.openid == null){
            this.$store.dispatch('GetUserInfoByCode',{code: getRequestParam().code}).then(response => {
              console.log('[subscribe]==>' + response.data.d.subscribe)
              console.log('[openid]==>' + response.data.d.openid)
              if (this.shareOpenId === 'STATE') {
                this.shareOpenId = response.data.d.openid
              }
              this.openId = response.data.d.openid
              this.subscribe = response.data.d.subscribe
              console.log('[判断][sharedOpenId]:' + this.shareOpenId)
              this.setWxConfig()
            })
          } else {
            console.log('[vuex][getters][openid]:' + this.$store.getters.openid)
            console.log('[vuex][getters][subscribe]:' + this.$store.getters.subscribe)
            console.log('[vuex][getters][sharedOpenId]:' + this.$store.getters.sharedOpenId)
            this.openId = this.$store.getters.openid
            this.$store.dispatch('GetUserInfoByOpenId',{openId: this.$store.getters.openid}).then(response => {
              if (response.data && response.data.d ){
                this.subscribe = response.data.d.subscribe
                this.shareOpenId = getRequestParam().state === 'STATE' ? this.$store.getters.openid : getRequestParam().state
                console.log('[判断][sharedOpenId]:' + this.shareOpenId)
                this.setWxConfig()
              }
            })
          }
        }
      },
      setWxConfig () {
        console.log('执行[setWxConfig]')
        wxShare({
          title: '[惠民贷款]国有大银行誓要剿灭高利贷！贷款年息最低3.8%！',
          desc: '最高300万，最长10年！没有比较就没有伤害，看看客户是如何薅银行羊毛？',
          link: window.location.origin + '/?sharedOpenId='+ this.shareOpenId + '#/auth',
          imgUrl: 'http://wx.fuzefenqi.com/static/rate.jpg'
        },function () {
          //  分享成功的回调函数
          this.$notify.showInfo('消息', '分享成功')
        })
      },
      apply () {
        if (this.subscribe == 0 ) {
          this.$router.push({ path: '/attention'})
        } else if (this.subscribe == 1) {
          this.$router.push({ path: 'create', query: { openId: this.openId, subscribe: this.subscribe, shareOpenId: this.shareOpenId }})
        }
      }
    },
    data () {
      return {
        openId: null,
        subscribe: 0,
        shareOpenId: null,
        activeOneSrc: '/static/active/11.jpg',
        activeTwoSrc: '/static/active/12.jpg',
        codeSrc: '/static/active/13.jpg'
      }
    }
  }
</script>
