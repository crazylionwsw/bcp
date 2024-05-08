import Vue from 'vue'

import { WechatPlugin } from 'vux'

import { getSignatureInfo } from '../api/auth'

Vue.use(WechatPlugin)

//微信分享
const wxShare = (obj, callback) => {
  // console.log(obj,callback);
  function getUrl(){
    var url = window.location.href;
    //alert(url);
    var locationurl = url.split('#')[0];
    //alert(locationurl);
    return locationurl;
  }
  if (obj) {
    var title = obj.title==undefined||obj.title==null?'富择金服分享标题':obj.title;
    var link = obj.link==undefined||obj.link==null?window.location.origin + '/#/auth':obj.link;
    var desc = obj.desc==undefined||obj.desc==null?'富择金服分享描述':obj.desc;
    var imgUrl = obj.imgUrl==undefined||obj.imgUrl==null?'/static/attention.png':obj.imgUrl;
    var debug = obj.debug==true?true:false;
    console.log('设置的分享参数[link]:' + obj.link)
  } else {
    console.log('请传分享参数')
  }
  //微信分享
  getSignatureInfo(getUrl()).then(response => {

    if (response.data && response.data.c == 0 && response.data.d) {

      var wxdata = Object.assign({}, response.data.d);
      wxdata.debug = debug;
      wxdata.jsApiList= [
        'onMenuShareTimeline',
        'onMenuShareAppMessage'
      ];

      Vue.wechat.config(wxdata);

      Vue.wechat.ready(function () {
        //分享到朋友圈
        Vue.wechat.onMenuShareTimeline({
          title:title, // 分享标题
          link: link, // 分享链接
          desc: desc, // 分享描述
          imgUrl:imgUrl, // 分享图标
          success: function () {
            callback && callback();
            // 用户确认分享后执行的回调函数
            console.log('分享成功')
          },
          cancel: function () {
            // 用户取消分享后执行的回调函数
            console.log('用户取消分享')
          }
        });
        //分享到朋友
        Vue.wechat.onMenuShareAppMessage({
          title: title, // 分享标题
          desc: desc, // 分享描述
          link: link, // 分享链接
          imgUrl: imgUrl, // 分享图标
          type: '', // 分享类型,music、video或link，不填默认为link
          dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
          success: function () {
            // 用户确认分享后执行的回调函数
            callback && callback();
            console.log('分享成功')
          },
          cancel: function () {
            // 用户取消分享后执行的回调函数
            console.log('用户取消分享')
          }
        });

      })
    } else {
      this.$notify.showInfo('错误', '获取Jsapi_Ticket异常')
    }
  })
}

export {wxShare}
