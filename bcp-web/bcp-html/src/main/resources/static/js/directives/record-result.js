angular.module('app')
    .directive('recordResult', function() {
        return {
            restrict: 'E',
            scope: {resultId:'='},
            template: '<span></span>',
            controller: function($scope, $element, $filter, tasksService,msgService){
                
            }
        };
    });