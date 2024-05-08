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
