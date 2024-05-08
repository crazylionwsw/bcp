<template>
  <div id="entity">
    <group label-width="7.5em" label-align="left">
      <group-title slot="title">个人信息<x-img src="/static/person.png" default-src="/static/person.png" style="float: left;" class="person_image_class"></x-img></group-title>
      <x-input title="姓名:" v-model="creditloan.customerName" placeholder="请输入姓名" type="text" :disabled="isEdit"></x-input>
      <popup-radio title="性别:" :options="GenderOptions" v-model="creditloan.gender" placeholder="请选择性别" :readonly="isEdit">
        <template slot-scope="props" slot="each-item">
          <p style="text-align: center;">
            <span style="color:#666;">{{ props.label }}</span>
          </p>
        </template>
      </popup-radio>
      <x-input title="手机号码:" mask="999 9999 9999" v-model="creditloan.cell" placeholder="请输入手机号码" keyboard="number" is-type="china-mobile" :disabled="isEdit"></x-input>
    </group>

    <group label-width="7.5em" label-align="left">
      <group-title slot="title">工资信息<x-img src="/static/salary.png" default-src="/static/salary.png" style="float: left;" class="person_image_class"></x-img></group-title>
      <x-input title="工资收入:" v-model="creditloan.workSalary" placeholder="请输入工资收入" keyboard="number" type="number" :disabled="isEdit">
        <span slot="right">元</span>
      </x-input>

      <x-switch title="社保" :value-map="[0, 1]" v-model="haveSocialInsurance"></x-switch>
      <div v-if="haveSocialInsurance == 1">
        <x-input title="月缴存额:" v-model="creditloan.socialInsuranceAmount" placeholder="请输入月缴存额" keyboard="number" type="number" :disabled="isEdit">
          <span slot="right">元</span>
        </x-input>
      </div>

      <x-switch title="公积金" :value-map="[0, 1]" v-model="haveAccumulationFund"></x-switch>
      <div v-if="haveAccumulationFund == 1">
        <x-input title="月缴存额:" v-model="creditloan.accumulationFundAmount" placeholder="请输入月缴存额" keyboard="number" type="number" :disabled="isEdit">
          <span slot="right">元</span>
        </x-input>
      </div>

      <popup-radio title="单位性质:" :options="CompanyTypeOptions" v-model="creditloan.companyType" placeholder="请选择" :readonly="isEdit">
        <template slot-scope="props" slot="each-item">
          <p style="text-align: center;">
            <span style="color:#666;">{{ props.label }}</span>
          </p>
        </template>
      </popup-radio>
      <popup-radio title="本单位工作年限:" :options="WorkDateOptions" v-model="creditloan.workDate" placeholder="请选择" :readonly="isEdit">
        <template slot-scope="props" slot="each-item">
          <p style="text-align: center;">
            <span style="color:#666;">{{ props.label }}</span>
          </p>
        </template>
      </popup-radio>
    </group>

    <group label-width="7.5em" label-align="left">
      <group-title slot="title">保险<x-img src="/static/insurance.png" default-src="/static/insurance.png" style="float: left;" class="person_image_class"></x-img></group-title>
      <x-switch title="商业保险(寿险)" :value-map="[0, 1]" v-model="haveInsurance"></x-switch>
      <div v-if="haveInsurance == 1">
        <popup-radio title="投保人:" :options="WhetherOptions" v-model="creditloan.isPolicyHolder" placeholder="请选择" :readonly="isEdit">
          <template slot-scope="props" slot="each-item">
            <p style="text-align: center;">
              <span style="color:#666;">{{ props.label }}</span>
            </p>
          </template>
        </popup-radio>
        <popup-radio title="年缴费金额:" :options="ApociOptions" v-model="creditloan.apoci" placeholder="请选择" :readonly="isEdit">
          <template slot-scope="props" slot="each-item">
            <p style="text-align: center;">
              <span style="color:#666;">{{ props.label }}</span>
            </p>
          </template>
        </popup-radio>
        <popup-radio title="已缴费时间:" :options="PayDateOptions" v-model="creditloan.payDate" placeholder="请选择" :readonly="isEdit">
          <template slot-scope="props" slot="each-item">
            <p style="text-align: center;">
              <span style="color:#666;">{{ props.label }}</span>
            </p>
          </template>
        </popup-radio>

        <popup-radio title="缴费方式:" :options="PayTypeOptions" v-model="creditloan.payType" placeholder="请选择" :readonly="isEdit">
          <template slot-scope="props" slot="each-item">
            <p style="text-align: center;">
              <span style="color:#666;">{{ props.label }}</span>
            </p>
          </template>
        </popup-radio>
      </div>
    </group>

    <group label-width="7.5em" label-align="left">
      <group-title slot="title">房贷信息<x-img src="/static/houseloan.png" default-src="/static/houseloan.png" style="float: left;" class="person_image_class"></x-img></group-title>
      <x-switch title="房贷月供" :value-map="[0, 1]" v-model="haveHouseLoan"></x-switch>
      <div v-if="haveHouseLoan == 1">
        <popup-radio title="房产位置:" :options="HouseAddressOptions" v-model="creditloan.houseAddress" placeholder="请选择" :readonly="isEdit">
          <template slot-scope="props" slot="each-item">
            <p style="text-align: center;">
              <span style="color:#666;">{{ props.label }}</span>
            </p>
          </template>
        </popup-radio>

        <x-input title="月供金额:" v-model="creditloan.monthlyAmount" placeholder="请输入月供金额" keyboard="number" type="number" :disabled="isEdit">
          <span slot="right">元</span>
        </x-input>

        <popup-radio title="已还时间:" :options="RepayDateOptions" v-model="creditloan.repayDate" placeholder="请选择" :readonly="isEdit">
          <template slot-scope="props" slot="each-item">
            <p style="text-align: center;">
              <span style="color:#666;">{{ props.label }}</span>
            </p>
          </template>
        </popup-radio>
      </div>
    </group>

    <group label-width="7.5em" label-align="left">
      <group-title slot="title">预计信息<x-img src="/static/expectinfo.png" default-src="/static/expectinfo.png" style="float: left;" class="person_image_class"></x-img></group-title>
      <x-input title="预计借款额度:" v-model="creditloan.expectedLoanAmount" @on-blur="setExpectedLoanAmount" placeholder="您想借多少" type="number" keyboard="number" :readonly="isEdit">
        <span slot="right">元</span>
      </x-input>
      <popup-radio title="预计借款期限:" placeholder="您想借多久" :options="LoanTimeOptions" v-model="creditloan.expectedLoanTime" :readonly="isEdit">
        <template slot-scope="props" slot="each-item">
          <p style="text-align: center;">
            <span style="color:#666;">{{ props.label }}</span>
          </p>
        </template>
      </popup-radio>
    </group>
    <group>
      <x-button v-if="!isEdit" style="background-color: #51a938;width: 80%;text-align: center;" @click.native="submit">测算</x-button>
    </group>
  </div>
</template>

<style>
  #entity
    .weui-cells__title {
      font-size: 20px;
    }
    .person_image_class {
      width: 8%;
      height: 8%;
      margin-right: 2%;
    }
</style>

<script>
  import { Group, GroupTitle, XInput, PopupRadio, XButton, Checker, CheckerItem, XSwitch, XImg  } from 'vux'
  //  API
  import { getLoanQueryById, saveLoanQuery } from '../../../api/loanquery'
  //Filter
  import { getRequestParam } from '../../../filter/filter'
  //  param datas
  import { PresenceOptions, WhetherOptions, GenderOptions, CompanyTypeOptions, WorkDateOptions, ApociOptions, PayDateOptions, PayTypeOptions, HouseAddressOptions, RepayDateOptions, LoanTimeOptions } from '../../../config/params'

  const default_creditloan = {
    id: null,
    customerName: null,
    gender: null,
    openId: null,
    cell: null,
    workSalary: null,
    socialInsuranceAmount: null,
    accumulationFundAmount: null,
    companyType: null,
    workDate: null,
    isPolicyHolder: null,
    apoci: null,
    payDate: null,
    payType: null,
    houseAddress: null,
    monthlyAmount: null,
    repayDate: null,
    expectedLoanTime: null,
    expectedLoanAmount: null,
    shareOpenId: null,
    loanAmount: null,
    createType: 'WP'
  }

  export default{
    name: 'creditLoanDetail',
    components: {
      Group,
      GroupTitle,
      XInput,
      PopupRadio,
      XButton,
      Checker,
      CheckerItem,
      XSwitch,
      XImg
    },
    props: {
      isEdit: {
        type: Boolean,
        default: false
      }
    },
    created () {
      if (!this.isEdit) {
        this.creditloan = Object.assign({}, default_creditloan)
      } else if (this.isEdit){
        this.haveSocialInsurance = 1
        this.haveAccumulationFund = 1
        this.haveInsurance =  1
        this.haveHouseLoan = 1
        if (window.location.search.indexOf('id=') > 0) {
          this.getCreditLoan(window.location.search.split('id=')[1])
        }
      }
      console.info('录入页面获取，[subscribe]:  ' + this.$route.query.subscribe)
      console.info('录入页面获取，[openId]:  ' + this.$route.query.openId)
      console.info('录入页面获取，[shareOpenId]:  ' + this.$route.query.shareOpenId)
      if (this.$route.query.subscribe == 1) {
        if (this.$route.query.openId) {
          console.info('录入页面获取，当前登录人的[OPENID]:  ' + this.$route.query.openId)
          this.creditloan.openId = this.$route.query.openId
        }
        if (this.$route.query.shareOpenId) {
          console.info('录入页面获取，分享人的[OPENID]:  ' + this.$route.query.shareOpenId)
          this.creditloan.shareOpenId = this.$route.query.shareOpenId
        }
      }
    },
    methods: {
      setExpectedLoanAmount (value) {
        console.log('设置预计借款信息')
        this.userSet = 1
      },
      getCreditLoan (id) {
        getLoanQueryById(id).then(response => {
          if (response.data && response.data.d) {
            this.creditloan = response.data.d
          } else {
            this.creditloan = Object.assign({}, default_creditloan)
          }
        })
      },
      submit () {
        if ((this.creditloan.workSalary == null || this.creditloan.workSalary == '') && this.haveInsurance === 0 && this.haveHouseLoan === 0){
          this.$notify.showInfo('提示', '工资收入、商业保险年缴费金额、房贷月供金额,必选一项填写')
          return;
        }
        if (this.haveInsurance === 1 && (this.creditloan.apoci == null || this.creditloan.apoci == '')){
          this.$notify.showInfo('提示', '请选择年缴费金额')
          return;
        }
        if (this.haveHouseLoan === 1 && (this.creditloan.monthlyAmount == null||this.creditloan.monthlyAmount == '') ){
          this.$notify.showInfo('提示', '请选择月供金额')
          return;
        }
        const temData = Object.assign({}, this.creditloan)
        if (this.haveInsurance === 0) temData.apoci == null
        if (this.haveHouseLoan === 0) temData.monthlyAmount == null
        saveLoanQuery(temData).then(response => {
          if (response.data && response.data.d && response.data.d.loanAmount) {
            if (!(this.userSet == 1 && temData.expectedLoanAmount && temData.expectedLoanAmount !== '' && temData.expectedLoanAmount != 0)){
              this.creditloan.expectedLoanAmount = response.data.d.loanAmount
              this.userSet = 0
            }
            this.$notify.showInfo('测算结果', '您可以申请的贷款金额为：' + response.data.d.loanAmount + '元')
          }
        })
      }
    },
    data () {
      return {
        PresenceOptions: PresenceOptions,
        WhetherOptions: WhetherOptions,
        GenderOptions: GenderOptions,
        CompanyTypeOptions: CompanyTypeOptions,
        WorkDateOptions: WorkDateOptions,
        ApociOptions: ApociOptions,
        PayDateOptions: PayDateOptions,
        PayTypeOptions: PayTypeOptions,
        HouseAddressOptions: HouseAddressOptions,
        RepayDateOptions: RepayDateOptions,
        LoanTimeOptions: LoanTimeOptions,
        haveSocialInsurance: 0,
        haveAccumulationFund: 0,
        haveInsurance: 0,
        haveHouseLoan: 0,
        userSet: -1,
        creditloan: Object.assign({}, default_creditloan)
      }
    }
  }
</script>
