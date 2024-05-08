/**
 * @buttonshow		签批对话框中控制按钮显示的参数(通过、驳回、拒绝)(1：显示，0：隐藏)(eg. 111--都显示 000--都不显示)
 */
angular.module('app')
    .directive('signButton', function($rootScope, workflowService, customerTransactionService, paymentbillService ,customerService) {
        return {
            restrict: 'E',
            scope: {billid: '=', billtypecode: '=',buttonshow:'=',transaction:'='},//调用传入父级scope中的签批方法
            templateUrl: 'tpl/view/signinfo/sign.html',
            link: function(scope, element, attrs) {
                scope.canSign = false; //默认隐藏按钮
				scope.canAudit= false; //默认不禁用
				
				scope.userid = $rootScope.user.userID;
				scope.bizKey = scope.billtypecode + '.'+ scope.billid;
				scope.buttonText = "审核";
                scope.passButton = "通  过";
                scope.reapplyButton = "驳  回";
                scope.rejectButton = "拒  绝";
                scope.tipComment = '';

                scope.commmentTip = "输入审批备注，驳回的请输入理由！";
                if(scope.billtypecode == 'A001' || scope.billtypecode == 'A002'){
                    scope.commmentTip = "输入审批备注，驳回或拒绝的请输入理由！";
                }else if(scope.billtypecode == 'A012'){
                    scope.commmentTip = "输入审批备注，拒绝的请输入理由！";
                }

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
                        if ( scope.transaction &&(scope.transaction.status == 11 || scope.transaction.status == 16) && scope.billtypecode != 'A013'){
                            //  业务已取消\取消中，审批按钮不展示
                            scope.canSign = false;
                        }
                    } else {
                        scope.canSign = false;
                    }
                });
                if (scope.transaction){
                    customerTransactionService.checkBillRequiredImageType(scope.billtypecode,scope.transaction.id,function (data) {
                        if (data){
                            scope.tipComment = data;
                        }
                    })
                }
            },
			controller: function($scope, $element, $rootScope, workflowService, $modal){
                $scope.canAudit= false; //默认不禁用
				//获取
				$scope.signinfo = {comment: "", result: 0, flag:2, userId: $rootScope.user.userID, employeeId: $rootScope.user.employee.id, fromSalesman:false};

                //  接收广播，进行任务名称更新
                $scope.$on('ShowTask',function (e) {
                    workflowService.getMyTasks($scope.userid, $scope.bizKey, function(data) { //判断当前用户是否有审核权限
                        if(data){
                            $scope.canSign = true;
                            $scope.buttonText = data.name;
                            if ($scope.billtypecode =="A001" && data.name == "部门审批"){
                                $scope.passshow = 0;
                                $scope.reapplyButton = "重新审批";
                                $scope.rejectButton = "同意拒绝";
                            }
                        } else {
                            $scope.canSign = false;
                        }
                    });
                    $scope.signModal.close();
                });
				
				$scope.showSignModal = function() {
					if ($scope.signinfo.comment){
                        $scope.signinfo.comment = "";
					}

                    if ($scope.billtypecode == 'A026' && $scope.transaction.id){
                        workflowService.checkBankCardApplyStatus($scope.transaction.id,function (data) {
                            if (data && data == 9){
                                $scope.canAudit= true;
                                alert("该客户已经销卡！");
                            }else if (data && data < 4){
                                $scope.canAudit= true;
                                alert("卡业务处理调额未完成，不能进行审批！");
                            } else {
                                $scope.canAudit= false;
                            }
                        })
                    }
					if ($scope.billtypecode == 'A019' && $scope.transaction.id){
						workflowService.checkBankCardApplyStatus($scope.transaction.id,function (data) {
							 if(data && data == 9){
                                $scope.canAudit= true;
                                alert("该客户已经销卡！");
                            }else if (data && data != 6){
                                $scope.canAudit= true;
                                alert("卡业务处理未领卡，不能进行审批！");
                            }  else {
								$scope.canAudit= false;
							}
						})
					}

                    //判断垫资阶段该客户的缴费单是否已经核对完成
					if ($scope.billtypecode == "A004" && $scope.transaction.id && ($scope.buttonText == "财务核对" || $scope.buttonText == "垫资支付")){
                        paymentbillService.getPaymentsByCustomerTransactionId($scope.transaction.id,function (payments) {
                            if(payments != null){
                                angular.forEach(payments,function (item, index) {
                                    if(item.approveStatus == 2 || item.approveStatus == 8){ //缴费单审核未通过

                                    }else {
                                        //通过item.customerId查询出客户姓名
                                        customerService.getOne(item.customerId,function (cus) {
                                            if(cus) {
                                                alert("客户"+cus.name+"的缴费单还未核对完成，请及时核对！");
                                            }
                                        });
                                    }
                                })
                            }
                        });
                    }

                    //渠道还款该客户的缴费单是否已经核对完成
                    if ($scope.billtypecode == "A020" && $scope.transaction.id && $scope.buttonText == "渠道还款确认"){
                        paymentbillService.getPaymentsByCustomerTransactionId($scope.transaction.id,function (payments) {
                            if(payments != null){
                                angular.forEach(payments,function (item, index) {
                                    if(item.approveStatus == 2 || item.approveStatus == 8){ //缴费单(2通过，8驳回),
                                       
                                    }else {
                                        customerService.getOne(item.customerId,function (cus) {
                                            if(cus) {
                                                alert("客户"+cus.name+"的缴费单还未核对完成，请及时核对！");
                                            }
                                        });
                                    }
                                })
                            }
                        });
                    }


					$scope.modalTitle = $scope.buttonText;
					$scope.signModal = $modal.open({
						scope: $scope,
						animation: true,
						templateUrl: 'signForm.html',
						controller:function ($scope, $modalInstance, $q){

							$scope.$watch('canAudit',function (value) {
                                if (value){
                                    $scope.signModal.close();
                                }
                            });

                            $scope.close = function (){
                                $scope.signModal.close();
                            };

                            $scope.signBill = function (signinfo) {
                                $rootScope.$broadcast("BillSign", $scope.billid,signinfo,$scope.buttonText);
                            };

							/**
							 * 通过
							 */
							$scope.pass = function() {
								$scope.signinfo.result = 2;
								$scope.signBill($scope.signinfo);
							};
							
							/**
							 * 驳回
							 */
							$scope.reapply = function() {

								if ($scope.signinfo.comment.trim() == "" || $scope.signinfo.comment == undefined) {
									alert("请输入驳回理由！");
								} else {
									$scope.signinfo.result = 8;
                                    $scope.signBill($scope.signinfo);
								}
							};

							/**
							 * 拒绝
							 */
							$scope.reject = function() {
								if ($scope.signinfo.comment.trim() == "" || $scope.signinfo.comment == undefined) {
									alert("请输入拒绝理由！");
								} else {
									$scope.signinfo.result = 9;
                                    $scope.signBill($scope.signinfo);
								}
							};
						}
					});
				};
			}
        };
    });