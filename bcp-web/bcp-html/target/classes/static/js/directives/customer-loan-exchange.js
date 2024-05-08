/**
 * Created by admin on 2018/3/5.
 */
angular.module('app')
    .directive('customerLoanExchange', function(businessexchangeService,customerService) {
        return {
            restrict: 'AE',
            scope: {transactionid: '=',billtypecode:'=',businesstypecode:'=',bid:'='},
            templateUrl: 'tpl/view/customer/exchangeloan.html',
            link: function(scope, element, attrs) {
                if (scope.transactionid && scope.transactionid != '' && scope.bid != '') {
                    businessexchangeService.getOne(scope.bid,function (data) {
                        scope.businessexchange = data;

                        /*   查询  客户业务调整中的购车信息   */
                        if (data && data.customerExchangeCarId) {
                            customerService.getBusinessExchangeCar(data.customerExchangeCarId, function (car) {
                                scope.businessexchange.car = car;

                                // scope.businessexchange.car.vin = 'vin';
                            })
                        }

                        if (data && data.customerExchangeLoanId) {
                            customerService.getBusinessExchangeLoan(data.customerExchangeLoanId, function (loan) {
                                scope.businessexchange.loan = loan;
                                scope.businessexchange.loan.applyAmountProname = 'applyAmount';
                                scope.businessexchange.loan.creditAmountProname = 'creditAmount';
                                scope.businessexchange.loan.downPaymentRatioProname = 'downPaymentRatio';
                                scope.businessexchange.loan.chargePaymentWayProname = 'chargePaymentWay';
                                scope.businessexchange.loan.monthsProname = 'months';
                                scope.businessexchange.loan.loanServiceFeeProname = 'loanServiceFee';

                                scope.businessexchange.loan.receiptPriceProname = 'receiptPrice';
                                scope.businessexchange.loan.creditRatioProname = 'creditRatio';
                                scope.businessexchange.loan.ratioProname = 'ratio';
                                scope.businessexchange.loan.downPaymentProname = 'downPayment';
                                scope.businessexchange.loan.bankFeeAmountProname = 'bankFeeAmount';
                                scope.businessexchange.loan.compensatoryInterestProname = 'compensatoryInterest'; //是否贴息
                                scope.businessexchange.loan.compensatoryAmountProname = 'compensatoryAmount';
                                scope.businessexchange.loan.approvedCreditAmountProname = 'approvedCreditAmount';
                                scope.feeItemCode = 'code';
                            })
                        }
                    });
                }
            }
        };
    });