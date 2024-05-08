<template>
  <div>
      <h1>Oops!</h1>
      gif来源<a href='https://zh.airbnb.com/' target='_blank'>airbnb</a> 页面
      <h2>你没有权限去该页面</h2>
      <h6>如有不满请联系你领导</h6>
      <ul>
        <li>或者你可以去:</li>
        <li>
          <router-link to="/dashboard">回首页</router-link>
        </li>
      </ul>
  </div>
</template>

<script>
  import errGif from '../../assets/401_images/401.gif'

  export default {
    name: 'page401',
    data () {
      return {
        errGif: errGif + '?' + +new Date(),
        ewizardClap: 'https://wpimg.wallstcn.com/007ef517-bafd-4066-aae4-6883632d9646',
        dialogVisible: false
      }
    },
    methods: {
      back () {
        if (this.$route.query.noGoBack) {
          this.$router.push({ path: '/dashboard' })
        } else {
          this.$router.go(-1)
        }
      }
    }
  }
</script>

<style>
</style>
