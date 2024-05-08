/**
 * Created by admin on 2018/3/20.
 */
angular.module('app')
    .directive('decompressInfo', function(decompressService,sysparamService) {
        return {
            restrict: 'AE',
            scope: {decompress: '='},
            templateUrl: 'tpl/view/decompress/info.html',
            link: function(scope, element, attrs) {

                //解压类型
                sysparamService.getListByCode("DECOMPRESS_TYPE",function (data) {
                    scope.decompressTypes = data;
                });
                //收款账户
                sysparamService.getListByCode("PAYMENTBILL_ACCOUNT",function (data) {
                    scope.receiptAccounts = data;
                });
                //支付方式
                sysparamService.getListByCode("PAYMENT_TYPE",function (data) {
                    scope.paymentTypes = data;
                });
            }
        };
    });