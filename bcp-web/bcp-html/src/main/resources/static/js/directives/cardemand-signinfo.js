angular.module('app')
    .directive('cardemandSigninfo', function(workflowService) {
        return {
            restrict: 'E',
            scope: {id: '='},
            templateUrl: 'tpl/view/signinfo/cardemand.html',
            link: function(scope, element, attrs) {
                scope.signInfos2 = [];
                if (scope.id && scope.id != '') {
                    workflowService.getByBillIdAndCode(scope.id, function(data){
                        if (data) {
                            scope.signInfos2 = data.signInfos;
                        }
                    });

                }
            }
        };
    });