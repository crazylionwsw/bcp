<template>
    <div id="foot">
      <tabbar>
        <tabbar-item selected>
          <img slot="icon" src="../../assets/image/domestic_outfit.jpg">
          <span slot="label">家装贷款</span>
        </tabbar-item>
        <tabbar-item>
          <img slot="icon" src="../../assets/image/historys.jpg">
          <span slot="label">历史</span>
        </tabbar-item>
      </tabbar>
    </div>
</template>

<style>

</style>

<script>
    import { Tabbar, TabbarItem } from 'vux'
    export default{
      name:'AppFooter',
      components:{
        Tabbar,
        TabbarItem
      },
      data () {
        return {msg: ''}
      }
    }
</script>
