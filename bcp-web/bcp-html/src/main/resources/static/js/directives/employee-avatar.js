angular.module('app')
  .directive('employeeAvatar', function($localStorage, employeeService) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {id: '='},
        template: '<img ng-src="{{imgurl}}"/>',
        link: function(scope, element, attrs) {
            if (scope.id && scope.id != '') {
                //查询$localStorage, 如不存在再调用json接口
                scope.imgurl = 'img/a3.jpg';
                if ($localStorage.employees && $localStorage.employees[scope.id]) {
                    var avatarFileId = $localStorage.employees[scope.id].avatarFileId;
                    if (avatarFileId) {
                        scope.imgurl = '/json/file/download/' + avatarFileId;
                    }
                } else {
                    employeeService.getOne(scope.id, function (data) {
                        if (data) {
                            var avatarFileId = data.avatarFileId;
                            if (avatarFileId) {
                                scope.imgurl = '/json/file/download/' + avatarFileId;
                            }
                            //把员工信息存入$localStorage
                            if (!$localStorage.employees) $localStorage.employees = [];
                            $localStorage.employees[data.id] = data;
                        } else {
                            // do nothing
                        }
                    })
                }
            }    
        }
    };
  });