angular.module('app')
  .directive('hideOnClickOutside', function($document) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs, ctrl) {
            element.bind('click', function(e){
                e.stopPropagation();
            });

            $document.bind('click', function(e){
                element.removeClass('active');
            });
        }
    };
  });