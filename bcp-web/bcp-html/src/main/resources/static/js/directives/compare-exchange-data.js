/**
 * Created by admin on 2018/3/6.
 */
angular.module('app')
    .directive('compareExchangeData', function(businessexchangeService,$filter) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {proname: '=', transactionid:'=', bid:'='},
            template: '<i ng-show="canshow != 0" class="text-danger" tooltip="{{clues}}"></i>',
            link: function($scope, element, attrs) {

                /*    根据客户交易ID  查询该笔交易的经销商信息      */
                $scope.$watch('transactionid',function (transactionId) {
                    if (transactionId && transactionId != '' && $scope.proname && $scope.proname != '') {
                        $scope.canshow = 0;
                        businessexchangeService.compareExchangeData($scope.proname, transactionId , $scope.bid,function (data) {
                            $scope.canshow = data.result;
                            if (data && data.result == 2) {//
                                element.addClass('icon-star');
                                $scope.clues = "已更换";
                            } else if (data && data.result == -1) {//
                                element.addClass('glyphicon  glyphicon-arrow-down');
                                $scope.clues = "降低  " +  Math.abs(data.difference);
                            } else if (data && data.result == 1) {//
                                element.addClass('glyphicon  glyphicon-arrow-up');
                                $scope.clues = "增加  " + Math.abs(data.difference);
                            }
                        })
                    }
                });
            }
        };
    });
