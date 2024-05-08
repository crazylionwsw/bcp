/**
 * Created by user on 2018/1/9.
 */
/**
 * Created by user on 2018/1/4.
 */
angular.module('app')
    .directive('dealersharingStatus', function(dealersharingService) {
        return {
            restrict: 'AE',
            replace: true,
            scope: {id: '='},
            template: '<span>{{status | sharingConfirmStatus}}</span>',
            link: function(scope, element, attrs) {
                if (scope.id && scope.id != '') {
                    /*    根据集团iD和月份查询集团分成信息     */
                    dealersharingService.getOne(scope.id,function(data){
                        if (data) {
                            scope.status = data.status;
                        }
                    })
                }
            }
        };
    });