angular.module('app')
  .directive('fixedColumns', function() {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            element.DataTable({
                scrollY:        "300px",
                scrollX:        true,
                scrollCollapse: true,
                paging:         false,
                fixedColumns: {
                    leftColumns: 6
                }
            });
        }
    };
  });