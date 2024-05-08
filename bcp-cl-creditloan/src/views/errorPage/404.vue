<template>
  <div style="background:#f0f2f5;margin-top: -20px;height:100%;">
    <div>请检查您输入的网址是否正确，请点击以下按钮返回主页或者发送错误报告</div>
  </div>
</template>

<script>
import Img404 from '../../assets/404_images/404.png'
import Img404Cloud from '../../assets/404_images/404_cloud.png'

export default {
  name: 'page404',
  data () {
    return {
      Img404,
      Img404Cloud
    }
  },
  computed: {
    message () {
      return '特朗普说这个页面你不能进......'
    }
  }
}
</script>

<style>
</style>
