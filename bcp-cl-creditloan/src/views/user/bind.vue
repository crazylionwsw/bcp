<template>
  <div id="bind_div">
    <divider>手机号绑定</divider>
    <group>
      <x-input class="cell_class" v-model="cell" placeholder="请输入手机号" type="number" keyboard="number" is-type="china-mobile" @on-blur="inputCell" required></x-input>
      <x-input class="code_class" v-model="verificationCode" placeholder="请输入验证码" type="number" keyboard="number" @on-blur="inputVerificationCode" required>
        <x-button class="vc_button_class" slot="right" plain @click.native="sendCode" :text="buttonText" :disabled="buttonDisabled" mini></x-button>
      </x-input>
    </group>
    <x-button class="validate_btn" @click.native="validateCell" :text="validateText"></x-button>
  </div>
</template>

<style lang="less">
  #bind_div {
    .cell_class {
      height: 32px;
    }
    .code_class {
      height: 32px;
    }
    .vc_button_class {
      color: #e66131;
      font-size: 1em;
      border-color: white;
    }
    .validate_btn {
      width: 95%;
      background-color: #e66131;
    }
  }
</style>

<script>
  import { Group, GroupTitle, XInput, XButton, Divider } from 'vux'
  import isMobilePhone from 'validator/lib/isMobilePhone'

  //  API
  import { sendMsgCode } from '../../api/sysparam'
  import { bindUserCell } from '../../api/user'

  export default{
    name: 'Bind',
    components: {
      Group,
      GroupTitle,
      XInput,
      XButton,
      Divider
    },
    created (){
      if (this.$route.query.originalCell) {
        this.originalCell = this.$route.query.originalCell
        this.validateText = '更换手机号'
      }
    },
    methods: {
      inputCell (){
        if (this.cell == null || this.cell === '') {
          this.$notify.showInfo('提示', '请输入手机号！')
          return;
        }
        if (this.originalCell != null && this.cell === this.originalCell) {
          this.$notify.showInfo('提示', '当前手机号与已绑定手机号一致，请重新输入！')
          return;
        }
        if (!isMobilePhone(this.cell, 'zh-CN')) {
          this.$notify.showInfo('提示', '手机号格式错误！')
          return;
        }
      },
      inputVerificationCode() {
        if (!this.buttonDisabled) {
          this.$notify.showInfo('提示', '请先获取验证码！')
          return;
        }
        if (this.buttonDisabled && (this.verificationCode == null || this.verificationCode === '')){
          this.$notify.showInfo('提示', '请输入验证码！')
          return;
        }
      },
      sendCode() {
        if (this.cell == null || this.cell === '') {
          this.$notify.showInfo('提示', '请输入手机号！')
          return;
        }
        if (this.originalCell != null && this.cell === this.originalCell) {
          this.$notify.showInfo('提示', '当前手机号与已绑定手机号一致，请重新输入！')
          return;
        }
        if (!isMobilePhone(this.cell, 'zh-CN')) {
          this.$notify.showInfo('提示', '手机号格式错误！')
          return;
        }
        this.buttonDisabled = true
        sendMsgCode(this.cell).then(response => {
          if (response.data.c === 0) {
            this.$notify.showInfo('提示', '发送成功')
            let interval = setInterval(() => {
              if ((this.time--) <= 0) {
                this.buttonText = '重新获取验证码'
                this.buttonDisabled = false
                this.time = 60;
                clearInterval(interval);
                return
              }
              this.buttonText = this.time + '秒后重新获取'
            }, 1000);
          } else if (response.data.c === 9) {
            this.$notify.showInfo('提示', '发送失败！')
          } else {
            this.$notify.showInfo('提示', '验证码发送失败！')
          }
        })
      },
      validateCell (){
        if (this.cell == null || this.cell === '') {
          this.$notify.showInfo('提示', '请输入手机号！')
          return;
        }
        if (this.originalCell != null && this.cell === this.originalCell) {
          this.$notify.showInfo('提示', '当前手机号与已绑定手机号一致，请重新输入！')
          return;
        }
        if (!isMobilePhone(this.cell, 'zh-CN')) {
          this.$notify.showInfo('提示', '手机号格式错误！')
          return;
        }
        if (!this.buttonDisabled) {
          this.$notify.showInfo('提示', '请先获取验证码！')
          return;
        }
        if (this.buttonDisabled && (this.verificationCode == null || this.verificationCode === '')){
          this.$notify.showInfo('提示', '请输入验证码！')
          return;
        }
        bindUserCell(this.$store.getters.openid, this.cell, this.verificationCode).then(response => {
          if (response.data && response.data.c === 0) {
            this.$store.commit('SET_CELL', this.cell)
            this.$notify.showInfo('提示', '绑定成功！')
            this.$router.push({path: '/user/info', query: {lt: 'WXL'}})
          } else if (response.data && response.data.c === 9) {
            this.$notify.showInfo('提示', response.data.m)
          }
        })
      }
    },
    data () {
      return {
        buttonText: '获取验证码',
        time: 60,
        cell: null,
        verificationCode: null,
        buttonDisabled: false,
        originalCell: null,
        validateText: '绑定手机号'
      }
    }
  }
</script>
