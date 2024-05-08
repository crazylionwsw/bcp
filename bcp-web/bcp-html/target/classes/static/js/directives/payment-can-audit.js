
angular.module('app')
    .directive('paymentCanAudit', function($localStorage,paymentService) {
    return {
        restrict: 'A',
        link: function(scope, element, attr) {
            scope.$watch(attr.paymentCanAudit, function paymentCanAuditWatchAction(bill) {
                if (bill) {
                    if ($localStorage.employee.orgInfoId != null){
                        var employeeId = $localStorage.employee.id;
                        var activitiUserRoles = $localStorage.employee.activitiUserRoles;
                        //判断当前用户的审核组
                        if (activitiUserRoles.indexOf('8') > -1) {
                            //if (bill.orginfoId == $localStorage.employee.orgInfoId) {
                                paymentService.getOrgInfo(bill.orginfoId, function (data) {
                                    if (employeeId != data.leaderId) {
                                        element.addClass('hidden');
                                    } else {
                                        element.removeClass('hidden');
                                    }
                                });
                            //} else {
                            //    element.addClass('hidden');
                            //}
                        } else {
                            element.removeClass('hidden');
                        }
                    }
                }
            });
        }
    };
});