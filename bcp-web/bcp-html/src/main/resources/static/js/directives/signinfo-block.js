angular.module('app')
  .directive('signinfoBlock', function() {
    return {
        restrict: 'E',
        scope: {sign: '&'}, //调用传入父级scope中的签批方法
        templateUrl: 'tpl/view/signinfo/block.html',
        controller: function($scope, $element,$rootScope){
            $scope.modalTitle = "审查/审批";
            $scope.signinfo = {comment: null, result: 0, userId: $rootScope.user.userID, employeeId: $rootScope.user.employee.id};

            /**
             * 通过
             */
            $scope.pass = function() {
                $scope.signinfo.result = 2;
                $scope.sign({signinfo: $scope.signinfo});
            };

            /**
             * 驳回
             */
            $scope.reapply = function() {

                if ($scope.signinfo.comment.trim() == "" || $scope.signinfo.comment == undefined) {
                    alert("请输入驳回理由");
                } else {
                    $scope.signinfo.result = 8;
                    $scope.sign({signinfo: $scope.signinfo});
                }
            };

            /**
             * 拒绝
             */
            $scope.reject = function() {
                if ($scope.signinfo.comment.trim() == "" || $scope.signinfo.comment == undefined) {
                    alert("请输入拒绝理由");
                } else {
                    $scope.signinfo.result = 9;
                    $scope.sign({signinfo: $scope.signinfo});
                }
            };

            $scope.close = function() {
                $scope.$parent.$close();
            }
        }
    };
  });