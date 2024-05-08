/**
 * Created by admin on 2018/3/10.
 */
angular.module('app')
    .directive('customerOrderexchangeLoan', function(orderService,customerService) {
        return {
            restrict: 'AE',
            scope: {transactionid: '=',billtypecode:'=',businesstypecode:'='},
            templateUrl: 'tpl/view/customer/orderexchangeloan.html',
            link: function(scope, element, attrs) {
                if (scope.transactionid && scope.transactionid != '') {
                    orderService.getOneByCustomerTransactionId(scope.transactionid,function (data) {
                        scope.order = data;
                        /*   查询  客户信息   */
                        if (data && data.customerId) {
                            customerService.getOne(data.customerId, function (customer) {
                                scope.order.customer = customer;
                            })
                        }
                        /*   查询  客户购车信息   */
                        if (data && data.customerCarId) {
                            customerService.getCar(data.customerCarId, function (car) {
                                scope.order.car = car;
                            })
                        }
                        /*    查询    客户交易信息  */
                        if (data && data.customerLoanId) {
                            customerService.getLoan(data.customerLoanId, function (loan) {
                                scope.order.loan = loan;
                                scope.order.loan.applyAmountProname = 'applyAmount';
                                scope.order.loan.creditAmountProname = 'creditAmount';
                                scope.order.loan.downPaymentRatioProname = 'downPaymentRatio';
                                scope.order.loan.chargePaymentWayProname = 'chargePaymentWay';
                                scope.order.loan.monthsProname = 'months';
                                scope.order.loan.loanServiceFeeProname = 'loanServiceFee';
                            })
                        }
                    });
                }
            },
            controller: function($scope, $element, $filter, $location, $state) {

                $scope.updateApprovedAmount = function () {
                    var loan = $scope.order.loan;
                    customerService.updateApprovedAmount(loan,function (data) {
                        if(data && data.approvedCreditAmount){
                            alert("银行报批额度设置成功!")
                        }else{
                            alert("银行报批额度设置失败!");
                        }
                    })
                }
            }
        };
    });