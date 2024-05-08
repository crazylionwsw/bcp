//  主要用于 支行、员工、经销商信息的选择框数据格式化
export function formatData(data, className) {
  if (data === null || data === undefined) {
    return
  }
  var list = []
  var i = 0
  for (i; i<data.length; i++){
    var formatData = {}
    formatData['value'] = data[i].id
    if (className === 'employee') {
      formatData['name'] = data[i].username
    } else if (className === 'cashsource'){
      formatData['name'] = data[i].shortName
    } else {
      formatData['name'] = data[i].name
    }
    list.push(formatData)
  }
  return list
}

export function getRequestParam() {
  var url = location.search; //获取url中"?"符后的字串
  var theRequest = new Object();
  if (url.indexOf("?") != -1) {
    var str = url.substr(1);
    var strs = str.split("&");
    for(var i = 0; i < strs.length; i ++) {
      theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
    }
  }
  return theRequest;
}
