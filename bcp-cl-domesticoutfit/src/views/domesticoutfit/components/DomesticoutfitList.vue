<template>
    <div>
        <ul>
          <li v-for="item in domestic_outfits">
            <grid :cols="3" :show-lr-borders="false">
              <grid-item :key="item.index" :link=" '/domesticoutfit/edit/' + item.id">
                <span class="grid-center">{{item.customerName}}</span>
              </grid-item>
              <grid-item :key="item.index" :link=" '/domesticoutfit/edit/' + item.id">
                <span class="grid-center">{{item.applyTime}}</span>
              </grid-item>
              <grid-item :key="item.index" :link=" '/domesticoutfit/edit/' + item.id">
                <popup-picker placeholder="请选择所属支行" :data="cashSourceData" :columns="1" v-model="item.cashSourceIds" value-text-align="left" show-name></popup-picker>
              </grid-item>
            </grid>
          </li>
        </ul>
    </div>
</template>

<style>
</style>

<script>
    import { Grid, GridItem, PopupPicker } from 'vux'
    //  API
    import { getPagesDomesticOutfit } from '../../../api/domesticOutfit'
    import { getCashSources } from '../../../api/cashsource'
    //  filter
    import { formatData } from '../../../filter/filter.js'

    export default{
      components: {
        Grid,
        GridItem,
        PopupPicker
      },
      props: {
        isHistory: {
          type: Boolean,
          default: false
        }
      },
      created () {
        this.getCashSources()
        this.getListData(0, 10)
      },
      methods: {
        getListData (currentPage, pageSize) {
          getPagesDomesticOutfit(currentPage, pageSize, this.isHistory).then(response => {
            if (!response.data) {
            }
            console.log('domesticoutfit response=' + response.data.d)
            this.domestic_outfits = response.data.d.result
          })
        },
        getCashSources () {
          getCashSources().then(response => {
            console.log('cashsource response=' + response.data.d)
            this.cashSourceData = formatData(response.data.d, 'cashsource')
          })
        }
      },
      data () {
        return {
          domestic_outfits: [],
          cashSourceData: []
        }
      }
    }
</script>
