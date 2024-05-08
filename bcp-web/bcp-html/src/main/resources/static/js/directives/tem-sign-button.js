/**
 * @buttonshow		签批对话框中控制按钮显示的参数(通过、驳回、拒绝)(1：显示，0：隐藏)(eg. 111--都显示 000--都不显示)
 */
angular.module('app')
    .directive('temSignButton', function($rootScope, workflowService,sysroleService, customerTransactionService) {
        return {
            restrict: 'E',
            scope: {billid: '=', billtypecode: '=',buttonshow:'=',transaction:'=',customer:'=',customerloan:'='},//调用传入父级scope中的签批方法
            templateUrl: 'tpl/view/signinfo/temsign.html',
            link: function(scope, element, attrs) {

                //获取
                scope.temsigninfo = {comment: "", result: 0, flag:2, userId: $rootScope.user.userID, employeeId: $rootScope.user.employee.id, fromSalesman:false};

                scope.canSign = false; //默认隐藏按钮
				scope.canAudit= false; //默认不禁用
				
				scope.userid = $rootScope.user.userID;
				scope.bizKey = scope.billtypecode + '.'+ scope.billid;
				scope.buttonText = "审核";
                scope.passButton = "通  过";
                scope.reapplyButton = "驳  回";
                scope.rejectButton = "拒  绝";
                scope.tipComment = '';

                scope.commmentTip = "输入审批备注，驳回或拒绝的请输入理由！";

				if(scope.buttonshow) {
                    scope.passshow = 0;
                    scope.reapplyshow = 0;
                    scope.rejectshow = 0;
                    var str= scope.buttonshow.toString();
                    if(str.length>0)
	                    scope.passshow =str.substr(0,1);
                    if(str.length>1)
                        scope.reapplyshow = str.substr(1, 1);
                    if(str.length>2)
                    	scope.rejectshow =  str.substr(2,1);
                }else{
                    scope.passshow = 1;
                    scope.reapplyshow = 1;
                    scope.rejectshow = 1;
				}

                // 判断是否需要显示/隐藏按钮，并更改按钮文字
                workflowService.getMyTasks(scope.userid, scope.bizKey, function(data) { //判断当前用户是否有审核权限
                    if(data){
                        scope.canSign = true;
                        scope.buttonText = data.name;
                        if (scope.billtypecode =="A001" && data.name == "部门审批"){
                            scope.passshow = 0;
                            scope.reapplyButton = "重新审批";
                            scope.rejectButton = "同意拒绝";
                        }
                        if ((scope.transaction.status == 11 || scope.transaction.status == 16) && scope.billtypecode != 'A013'){
                            //  业务已取消\取消中，审批按钮不展示
                            scope.canSign = false;
                        }
                        if(scope.buttonText == "资质审核" || scope.buttonText == "签约审核"){
                            scope.temsigninfo.downPaymentRatio = scope.customerloan.downPaymentRatio;
                            scope.temsigninfo.creditAmount = scope.customerloan.creditAmount;
                        }
                    } else {
                        scope.canSign = false;
                    }
                });
                customerTransactionService.checkBillRequiredImageType(scope.billtypecode,scope.transaction.id,function (data) {
                    if (data){
                        scope.tipComment = data;
                    }
                })
            },
			controller: function($scope, $element, $rootScope, workflowService, $modal){
                $scope.canAudit= false; //默认不禁用

                //  接收广播，进行任务名称更新
                $scope.$on('ShowTemTask',function (e) {
                    workflowService.getMyTasks($scope.userid, $scope.bizKey, function(data) { //判断当前用户是否有审核权限
                        if(data){
                            $scope.canSign = true;
                            $scope.buttonText = data.name;
                            if ($scope.billtypecode =="A001" && data.name == "部门审批"){
                                $scope.passshow = 0;
                                $scope.reapplyButton = "重新审批";
                                $scope.rejectButton = "同意拒绝";
                            }
                            if($scope.buttonText == "资质审核" || $scope.buttonText == "签约审核"){
                                $scope.temsigninfo.downPaymentRatio = $scope.customerloan.downPaymentRatio;
                                $scope.temsigninfo.creditAmount = $scope.customerloan.creditAmount;
                            }
                        } else {
                            $scope.canSign = false;
                        }
                    });
                    $scope.temSignModal.close();
                });
				
				$scope.showTemSignModal = function() {
                    if($scope.customer.isSelfEmployed != 0 && $scope.customer.isSelfEmployed != 1){
                        alert("请先选择客户是否为自雇人士，再进行审批！");
                        return;
                    }
					if ($scope.temsigninfo.comment){
                        $scope.temsigninfo.comment = "";
					}

					$scope.modalTitle = $scope.buttonText;
					$scope.temSignModal = $modal.open({
						scope: $scope,
						animation: true,
						templateUrl: 'temSignForm.html',
						controller:function ($scope, $modalInstance, $q){

							$scope.$watch('canAudit',function (value) {
                                if (value){
                                    $scope.temSignModal.close();
                                }
                            });

                            $scope.close = function (){
                                $scope.temSignModal.close();
                            };

                            $scope.signBill = function (temsigninfo) {
                                $rootScope.$broadcast("BillTemSign", $scope.billid,temsigninfo,$scope.buttonText);
                            };

							/**
							 * 通过
							 */
							$scope.pass = function() {
								$scope.temsigninfo.result = 2;
								$scope.signBill($scope.temsigninfo);
							};
							
							/**
							 * 驳回
							 */
							$scope.reapply = function() {

								if ($scope.temsigninfo.comment.trim() == "" || $scope.temsigninfo.comment == undefined) {
									alert("请输入驳回理由！");
								} else {
									$scope.temsigninfo.result = 8;
                                    $scope.signBill($scope.temsigninfo);
								}
							};

							/**
							 * 拒绝
							 */
							$scope.reject = function() {
								if ($scope.temsigninfo.comment.trim() == "" || $scope.temsigninfo.comment == undefined) {
									alert("请输入拒绝理由！");
								} else {
									$scope.temsigninfo.result = 9;
                                    $scope.signBill($scope.temsigninfo);
								}
							};
						}
					});
				};
			}
        };
    });