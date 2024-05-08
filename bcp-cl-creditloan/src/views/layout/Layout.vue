<template>
  <div id="layout" style="height:100%;">
    <div v-transfer-dom>
      <actionsheet :menus="menus" :show-cancel="true" v-model="showMenu" @on-click-menu="clickOperation"></actionsheet>
    </div>

    <drawer
      width="200px;"
      :drawer-style="{'background-color':'#35495e', width: '200px'}">
      <!-- main content -->
      <view-box ref="viewBox">
        <router-view class="router-view"></router-view>
      </view-box>
    </drawer>
  </div>
</template>

<script>
  import { Tabbar, TabbarItem, Drawer, Actionsheet, ViewBox, XHeader, TransferDom } from 'vux'
  export default {
    directives: {
      TransferDom
    },
    components: {
      Tabbar,
      TabbarItem,
      Drawer,
      Actionsheet,
      ViewBox,
      XHeader,
      TransferDom
    },
    methods: {
      onClickMore () {
        this.showMenu = true
      },
      // header 【更多】点击操作方法
      clickOperation (operation) {
        if (operation === 'add') {
          this.$router.push('create')
        }
      }
    },
    data () {
      return {
        title: '家装贷款',
        showMenu: false,
        menus: {
          'operation.noop': '<span class="menu-title">操作</span>',
          'add': '添加'
        },
        drawerVisibility: false
      }
    },
    name: 'app'
  }
</script>

<style lang="less">
  @import '~vux/src/styles/reset.less';
  @import '~vux/src/styles/1px.less';
  @import '~vux/src/styles/tap.less';

  body {
    background-color: #fbf9fe;
  }
  html, body {
    height: 100%;
    width: 100%;
    overflow-x: hidden;
  }

  .demo-icon-22 {
    font-family: 'vux-demo';
    font-size: 22px;
    color: #888;
  }

  .vux-demo-tabbar .weui-bar__item_on .demo-icon-22 {
    color: black;
  }
  .vux-demo-tabbar .weui-tabbar_item.weui-bar__item_on .vux-demo-tabbar-icon-home {
    color: rgb(53, 73, 94);
  }
  .demo-icon-22:before {
    content: attr(icon);
  }

  .weui-tabbar__icon + .weui-tabbar__label {
    margin-top: 0!important;
  }

  @font-face {
    font-family: 'vux-demo';  /* project id 70323 */
    src: url('https://at.alicdn.com/t/font_h1fz4ogaj5cm1jor.eot');
    src: url('https://at.alicdn.com/t/font_h1fz4ogaj5cm1jor.eot?#iefix') format('embedded-opentype'),
    url('https://at.alicdn.com/t/font_h1fz4ogaj5cm1jor.woff') format('woff'),
    url('https://at.alicdn.com/t/font_h1fz4ogaj5cm1jor.ttf') format('truetype'),
    url('https://at.alicdn.com/t/font_h1fz4ogaj5cm1jor.svg#iconfont') format('svg');
  }

  .router-view {
    width: 100%;
    top: 46px;
  }

  .menu-title {
    color: #888;
  }
</style>
