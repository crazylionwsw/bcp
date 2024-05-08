angular.module('app')
  .directive('declarationStatus', function(declarationService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<span>{{status |DeclarationStatusFilter}}</span>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                declarationService.getOneByCustomerTransactionId(scope.id, function (declaration) {
                    if (declaration) {
                        scope.status = declaration.status;
                    }
                })
            }    
        }
    };
  });