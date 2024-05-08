angular.module('app')
    .directive('chargeplanSigninfoList', function(workflowService) {
        return {
            restrict: 'E',
            scope: {signinfos: '='},
            templateUrl: 'tpl/view/chargefeeplan/list2.html',
            link: function(scope, element, attrs) {
                if (scope.signinfos && scope.signinfos != '') {
                    scope.signInfos = scope.signinfos;
                }
            }
        };
    });