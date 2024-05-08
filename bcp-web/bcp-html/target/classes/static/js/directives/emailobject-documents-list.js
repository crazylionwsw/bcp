/**
 * Created by user on 2017/9/14.
 */
/**
 * 邮件模版所用展示页面
 */
angular.module('app')
    .directive('emailobjectDocumentsList', function() {
        return {
            restrict: 'E',
            scope: {ids:'='},
            templateUrl: 'tpl/view/document/elist.html',
            controller: function($scope, $element, $filter, documentService){
                if ($scope.ids && $scope.ids != '') {
                    $scope.documents = [];

                    $scope.$watch('ids', function (value) {
                        if ($scope.ids !== undefined) {
                            documentService.getTypesByIds($scope.ids, function (data) {
                                $scope.documents = data;
                            });
                        }
                    });
                }
            }
        };
    });
