/**
 *  有无
 * @type {[*]}
 */
const PresenceOptions = [
  {key: 1, value: '有'},
  {key: 0, value: '无'}
]

/**
 *  是否
 * @type {[*]}
 */
const WhetherOptions = [
  {key: 1, value: '是'},
  {key: 0, value: '否'}
]

/**
 *  男/女
 * @type {[*]}
 */
const GenderOptions = [
  {key: 1, value: '男'},
  {key: 0, value: '女'}
]

/**
 *  单位性质
 * @type {[*]}
 */
const CompanyTypeOptions = [
  {key: 'ICS', value: '公务员/事业单位'},
  {key: 'LC', value: '上市公司'},
  {key: 'PE', value: '民营企业'}
]

/**
 *    工作年限
 * @type {[*]}
 */
const WorkDateOptions = [
  {key: 'HALFYEARUP', value: '半年以上'},
  {key: 'ONEYEARUP', value: '1年以上'},
  {key: 'FIVEYEARUP', value: '5年以上'}
]

/**
 * 商业保险年缴额
 * Annual payment of commercial insurance
 * @type {[*]}
 */
const ApociOptions = [
  {key: 2400, value: '2400以上'},
  {key: 5000, value: '5000以上'},
  {key: 10000, value: '10000以上'}
]

/**
 *  商业保险缴费时间
 * @type {[*]}
 */
const PayDateOptions = [
  {key: 'ONEYEARUP', value: '1年以上'},
  {key: 'TWOYEARUP', value: '2年以上'},
  {key: 'THREEYEARUP', value: '3年以上'}
]

/**
 *  缴费方式
 * @type {[*]}
 */
const PayTypeOptions = [
  {key: 'STAGES', value: '期交'},
  {key: 'WHOLE', value: '趸交'}
]

/**
 *  房产位置
 * @type {[*]}
 */
const HouseAddressOptions = [
  {key: 'BEIJING', value: '北京'},
  {key: 'OTHER', value: '其他地区'}
]

/**
 *  已还时间
 * @type {[*]}
 */
const RepayDateOptions = [
  {key: 'ONEMONTHUP', value: '1个月以上'},
  {key: 'ONEYEARUP', value: '1年以上'},
  {key: 'TWOYEARUP', value: '2年以上'},
  {key: 'THREEYEARUP', value: '3年以上'}
]

/**
 *  借款期限
 * @type {[*]}
 */
const LoanTimeOptions = [
  {key: 1, value: '1年'},
  {key: 2, value: '2年'},
  {key: 3, value: '3年'},
  {key: 4, value: '4年'}
]

export {
  PresenceOptions,
  WhetherOptions,
  GenderOptions,
  CompanyTypeOptions,
  WorkDateOptions,
  ApociOptions,
  PayDateOptions,
  PayTypeOptions,
  HouseAddressOptions,
  RepayDateOptions,
  LoanTimeOptions
}
