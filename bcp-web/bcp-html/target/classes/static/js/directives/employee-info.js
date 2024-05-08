angular.module('app')
  .directive('employeeInfo', function($localStorage, orginfoService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<a href="">{{namecell}}</a>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                //查询$localStorage, 如不存在再调用json接口
                if ($localStorage.employees && $localStorage.employees[scope.id]) {
                    scope.namecell = $localStorage.employees[scope.id].username  + '【' +  $localStorage.employees[scope.id].cell+'】';
                } else {
                    orginfoService.getEmployee(scope.id, function (data) {
                        if (data) {
                            scope.namecell = data.username + '【' + data.cell+'】';
                            //scope.employeeNo = data.employeeNo;

                            //把员工信息存入$localStorage
                            if (!$localStorage.employees) $localStorage.employees = [];
                            $localStorage.employees[data.id] = data;
                        } else if (scope.id == '-1') {
                            scope.namecell = '系统管理员';
                        } else {
                            scope.namecell = 'N/A';
                        }
                    })
                }
            }    
        }
    };
  });