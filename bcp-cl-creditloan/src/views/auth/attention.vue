<template>
    <div id="attention_div">
      <x-img :src="attentionWebUrl"
             :webp-src="`${attentionWebUrl}?type=webp`"
             :default-src="attentionImageSrc"
             class="ximg-demo"
             container="#vux_view_box_body">
      </x-img>
    </div>
</template>

<style>
  #attention_div
    .ximg-demo {
      width: 100%;
      height: auto;
    }
</style>

<script>
  import { XImg } from 'vux'
  export default{
    name: 'attention',
    components: {
      XImg
    },
    data () {
      return {
        attentionWebUrl: 'http://wx.fuzefenqi.com/static/attention.jpg',
        attentionImageSrc: '/static/attention.jpg'
      }
    }
  }
</script>
