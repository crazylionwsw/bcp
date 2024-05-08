<template>
    <div class="head">
      <tabbar class="tabbar">
        <div class="title">{{title}}</div>
        <tabbar-item class="search">
          <img slot="icon" src="../../assets/image/search.png">
        </tabbar-item>
      </tabbar>
    </div>
</template>

<style>
  .head
  .tabbar{
    background-color: #058010;
    height:50px;
    position:relative;
  }
  .head
  .search{
    position:absolute;right:5px;top:5px;z-index:999;
  }
  head.
  .title{
    text-align:center;
    margin:auto;
    color:white;
    line-height:50px;
    font-size:18px;
  }
</style>

<script>

  import { Tabbar, TabbarItem } from 'vux'
  export default{
      name:'AppHeader',
      components:{
        Tabbar,
        TabbarItem
      },
      data () {
          return {
              title : '家装贷款'
          }
      }
  }
</script>
