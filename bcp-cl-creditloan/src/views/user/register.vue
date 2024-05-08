<template>
  <div id="reg_div">
    <divider>注册</divider>
    <group>
      <x-input class="cell_class" v-model="cell" placeholder="请输入手机号" type="number" keyboard="number" is-type="china-mobile"  @on-blur="inputCell" required></x-input>
      <x-input class="code_class" v-model="verificationCode" placeholder="请输入验证码" type="number" keyboard="number" @on-blur="inputVerificationCode" required>
        <x-button class="vc_button_class" slot="right" plain @click.native="sendCode" :text="buttonText" :disabled="buttonDisabled" mini></x-button>
      </x-input>
    </group>
    <group>
      <x-button class="reg_button" plain @click.native="register">注册</x-button>
    </group>
  </div>
</template>

<style lang="less">
  #reg_div {
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
    .reg_button {
      width: 95%;
      border-color: #e66131;
      background-color: #e66131;
    }
  }
</style>

<script>
  import { Group, GroupTitle, XInput, XButton, Divider } from 'vux'
  import isMobilePhone from 'validator/lib/isMobilePhone'
  //  API
  import { sendMsgCode } from '../../api/sysparam'
  import { registerUserByCell } from '../../api/user'

  export default{
    name: 'Register',
    components: {
      Group,
      GroupTitle,
      XInput,
      XButton,
      Divider
    },
    methods: {
      inputCell(){
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
          if (response.data && response.data.c === 0) {
            this.$notify.showInfo('提示', '发送成功！')
            let interval = setInterval(() => {
              if ((this.time--) <= 0) {
                this.buttonText = '重新获取验证码！'
                this.buttonDisabled = false
                this.time = 60;
                clearInterval(interval);
                return
              }
              this.buttonText = this.time + '秒后重新获取！'
            }, 1000);
          } else if (response.data && response.data.c === 9) {
            this.$notify.showInfo('提示', response.data.m)
          } else {
            this.$notify.showInfo('提示', '验证码发送失败！')
          }
        })
      },
      register (){
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
        registerUserByCell(this.cell, this.verificationCode).then(response => {
          console.log('注册结果：' + response.data)
          console.log('注册结果：' + response.data.c === 0)
          if (response.data.c === 0) {
            this.$store.commit('SET_CELL',this.cell)
            this.$notify.showInfo('提示', '注册成功！')
            this.$router.push({path: '/user/info', query: {lt: 'CL'}})
          } else if (response.data.c === 9){
            this.$notify.showInfo('提示', response.data.m)
          } else {
            this.$notify.showInfo('提示', '注册失败！')
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
        buttonDisabled: false
      }
    }
  }
</script>
