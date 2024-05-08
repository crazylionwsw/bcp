/**
 *      根据  合同信息中的 templateObjectId  获取模板对象的名称
 */
angular.module('app')
  .directive('documentTemplateobjectInfo', function(msgTemplatelService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{fileName}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                scope.$watch('id',function (value) {
                    msgTemplatelService.getOne(value,function (data) {
                        if (data){
                            scope.fileName = data.fileBean.fileName;
                        }
                    })
                })
            }
        }
    };
  });