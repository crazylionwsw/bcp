<template>
    <div id="entity">
      <group title="客户姓名">
        <x-input v-model="domesticoutfit.customerName" placeholder="请输入文本内容" is-type="china-name" required></x-input>
      </group>
      <group title="身份证号码">
        <x-input v-model="domesticoutfit.identifyNo" placeholder="请输入文本内容" required></x-input>
      </group>
      <group title="手机号码">
        <x-input mask="999 9999 9999" v-model="domesticoutfit.cell" placeholder="请输入文本内容" is-type="china-mobile" required></x-input>
      </group>
      <group title="单位名称">
        <x-input v-model="domesticoutfit.companyName" placeholder="请输入文本内容" required></x-input>
      </group>
      <group title="性别">
        <radio title="性别" :options="genderOptions" v-model="domesticoutfit.gender"></radio>
      </group>
      <group title="申请额度">
        <x-input v-model="domesticoutfit.applyAmount" placeholder="请输入数字" type="text" required></x-input>
      </group>
      <group title="申请日期">
        <datetime v-model="domesticoutfit.applyTime" value-text-align="left"></datetime>
      </group>
      <group title="所属支行">
        <popup-picker placeholder="请选择所属支行" :data="cashSourceData" :columns="1" v-model="domesticoutfit.cashSourceIds" value-text-align="left" show-name></popup-picker>
      </group>
      <group title="装修公司">
        <popup-picker placeholder="请选择装修公司" :data="channelData" :columns="1" v-model="domesticoutfit.channelIds" value-text-align="left" show-name></popup-picker>
      </group>
      <group title="房屋地址">
        <x-address v-model="domesticoutfit.houseAddress" :title="''" :list="addressData" placeholder="请选择地址" value-text-align="left" raw-value></x-address>
        <x-textarea v-model="domesticoutfit.houseDetailAddress" placeholder="详细地址" :show-counter="false" :rows="3" autosize></x-textarea>
      </group>
      <group title="资料附件">
        <vue-core-image-upload
          text="选择文件"
          :class="['btn', 'btn-primary']"
          :crop="false"
          @imageuploaded="imageuploaded"
          :max-file-size="5242880"
          :url="url" >
        </vue-core-image-upload>
      </group>
      <group title="客户情况说明">
        <x-textarea v-model="domesticoutfit.customerComment" placeholder="请输入文本内容" :show-counter="false" :rows="3" autosize></x-textarea>
      </group>
      <group title="批贷日期">
        <datetime v-model="domesticoutfit.approvedTime" value-text-align="left" placeholder="请选择批贷日期"></datetime>
      </group>
      <group title="批贷金额">
        <x-input v-model="domesticoutfit.approvedAmount" placeholder="请输入数字" type="text" required></x-input>
      </group>
      <group title="放款日期">
        <datetime v-model="domesticoutfit.loanTime" value-text-align="left" placeholder="请选择放款日期"></datetime>
      </group>
      <group title="放款金额">
        <x-input v-model="domesticoutfit.loanAmount" placeholder="请输入数字" type="text" required></x-input>
      </group>
      <group title="业务人员">
        <popup-picker placeholder="请选择业务人员" :data="employeeData" :columns="1" v-model="domesticoutfit.employeeIds" value-text-align="left" show-name></popup-picker>
      </group>
      <group>
        <x-button v-if="isEdit" type="primary" @click.native="submit">修改</x-button>
        <x-button v-if="!isEdit" type="primary" @click.native="submit">保存</x-button>
      </group>
    </div>
</template>

<style>

</style>

<script>
  import { Group, XInput, Radio, Datetime, PopupPicker, XAddress, ChinaAddressV4Data, XTextarea, XButton } from 'vux'
  import VueCoreImageUpload from 'vue-core-image-upload'
  //  API
  import { getDomesticOutfitById, saveDomesticOutfit } from '../../../api/domesticOutfit'
  import { getCashSources } from '../../../api/cashsource'
  import { getChannels } from '../../../api/channel'
  import { getAllBusinessManagers } from '../../../api/employee'

  //  filter
  import { formatDate } from '../../../filter/date.js'
  import { formatData } from '../../../filter/filter.js'

  const genderOptions = [
    {key: 0, value: '男'},
    {key: 1, value: '女'}
  ]

  const default_domesticoutfit = {
    id: '',
    customerName: '',
    identifyNo: '',
    cell: '',
    companyName: '',
    gender: '',
    houseAddress: [],
    houseDetailAddress: '',
    applyAmount: '',
    applyTime: formatDate(new Date(), 'yyyy-MM-dd'),
    cashSourceIds: [],
    channelIds: [],
    customerComment: '',
    approvedTime: formatDate(new Date(), 'yyyy-MM-dd'),
    approvedAmount: '',
    loanTime: '',
    loanAmount: '',
    employeeIds: []
  }

  export default{
    name: 'domesticoutfitDetail',
    components: {
      Group,
      XInput,
      Radio,
      Datetime,
      PopupPicker,
      XAddress,
      XTextarea,
      XButton,
      'vue-core-image-upload': VueCoreImageUpload
    },
    props: {
      isEdit: {
        type: Boolean,
        default: false
      }
    },
    created () {
      this.getCashSources()
      this.getChannels()
      this.getBusinessManagers()
      if (this.isEdit) {
        console.info(this.$route.params.id)
        this.getDomesticOutfit(this.$route.params.id)
      } else {
        this.domesticoutfit = Object.assign({}, default_domesticoutfit)
      }
    },
    methods: {
      getCashSources () {
        getCashSources().then(response => {
          console.log('cashsource response=' + response.data.d)
          this.cashSourceData = formatData(response.data.d, 'cashsource')
        })
      },
      getChannels () {
        getChannels().then(response => {
          console.log('channel response=' + response.data.d)
          this.channelData = formatData(response.data.d, 'channel')
        })
      },
      getBusinessManagers () {
        getAllBusinessManagers().then(response => {
          console.log('businessmanager response=' + response.data.d)
          this.employeeData = formatData(response.data.d, 'employee')
        })
      },
      getDomesticOutfit (id) {
        getDomesticOutfitById(id).then(response => {
          console.log(response.data.d)
          this.domesticoutfit = response.data.d
        }).catch(err => {
          console.log(err)
        })
      },
      imageuploaded (res) {
        if (res.errcode === 0) {
          this.src='http://img1.vued.vanthink.cn/vued751d13a9cb5376b89cb6719e86f591f3.png'
        }
      },
      submit () {
        console.log('方法调用')
        const temData = Object.assign({}, this.domesticoutfit)
        console.log('temData =' + temData)
        saveDomesticOutfit(temData).then(response => {
          if (!response.data) {
            console.log('保存失败')
          }
          console.log('保存成功')
        })
      }
    },
    data () {
      return {
        genderOptions: genderOptions,
        showAddress: false,
        addressData: ChinaAddressV4Data,
        cashSourceData: [],
        channelData: [],
        employeeData: [],
        url: process.env.BASE_URL + '/file/upload',
        domesticoutfit: Object.assign({}, default_domesticoutfit)
      }
    }
  }
</script>
