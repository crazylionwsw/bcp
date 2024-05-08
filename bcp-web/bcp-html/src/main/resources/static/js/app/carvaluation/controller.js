/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('carvaluationController',function ($scope,carvaluationService,orderService,customerService,cardealerService,customerTransactionService,sysparamService,$modal,$localStorage,$stateParams,toaster,$loading,$rootScope){

    $scope.title = "车辆评估";

    $scope.carvaluations = [];//返回的列表数据
    $scope.carvaluation = {};

    $scope.customer = {};
    $scope.carValuationFilter = {};
    
    $scope.checked = false;
    
    $scope.currentPage = 1;
    $scope.approveStatus = 1;
    $scope.signModal = {};

    $scope.valuations = [];

    
    /**
     * 列表
     */
    $scope.init = function (){
        if ($localStorage.CarValuationFilter){
            $scope.carValuationFilter = $localStorage.CarValuationFilter;
        }
        if ($localStorage.CarRegistryApproveStatus){
            $scope.approveStatus = $localStorage.CarValuationApproveStatus;
        } else {
            $scope.approveStatus = 1;
        }
        $scope.changePage(1);
        sysparamService.getListByCode("ONLINE_VALUATION",function (data) {
            $scope.valuations = data;
        });
        
    };
    /**
     * 分页功能
     */
    $scope.changePage = function (pageIndex){
        if(pageIndex){
            $scope.currentPage = pageIndex;
        }
        if( $scope.carValuationFilter.carType || $scope.carValuationFilter.vin || $scope.carValuationFilter.licenceNumber){
            carvaluationService.search($scope.carValuationFilter,$scope.currentPage-1,function (data){
                $scope.carvaluations = data.result;
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1;//当前页
                $scope.totalPages=data.totalPages;//总页数
            })
        } else {
            carvaluationService.getDataByPage($scope.approveStatus,$scope.currentPage-1,function (data){
                $scope.carvaluations = data.result;//返回的数据
                $scope.totalItems = data.totalCount;//数据总条数
                $scope.pageSize = data.pageSize;//分页单位
                $scope.currentPage = data.currentPage + 1;//当前页
                $scope.totalPages = data.totalPages; //总页数
            });
        }
        $localStorage.CarValuationFilter = $scope.carValuationFilter;
        $localStorage.CarValuationApproveStatus = $scope.approveStatus;
    };

    //打开弹出层
    $scope.edit = function(carvaluation, index){
        if (!$scope.checked) {
            if ($scope.carvaluation){
                $scope.carvaluation = {};
            }
            carvaluationService.getCarValuationFinishOrder(carvaluation.id,function (dataOrder) {
                $scope.finishOrder = dataOrder;
            })
            carvaluationService.getOne(carvaluation.id,function (data) {
                $scope.carvaluation = data;
                $scope.carvaluation.businessTypeCode = 'OC';
                $scope.checked = true;
            });

        }
    };

    /*        关闭  右边框      */
    $scope.close = function () {
        //更新列表数据
        carvaluationService.getOne($scope.carvaluation.id,function (data) {
            angular.forEach($scope.carvaluations, function(item, index){
                if (item.id == data.id) {
                    if($scope.approveStatus == -1){
                        $scope.carvaluations[index] = data;
                    } else if ($scope.approveStatus != data.approveStatus){
                        $scope.carvaluations.splice(index,1);
                    }else {
                        $scope.carvaluations[index] = data;
                    }

                }
            });
        });
        $scope.checked = false;
    };

    $scope.onOpenSidePanel = function () {
    };
    $scope.onCloseSidePanel = function () {
    };

    $scope.init();

    /**
     * 查询
     */
    $scope.search = function() {
        $scope.carValuationFilter = {};
        $scope.changePage(1);
    };

    // $scope.carValuationEdit = function (carvaluation) { //评估确认弹框
    //     var modalInstance = $modal.open({
    //         scope: $scope,
    //         animation: true,
    //         size:'lg',
    //         templateUrl: "signForm.html",
    //         controller: function ($scope) {
    //             /*关闭模态框*/
    //             $scope.cancel = function () {
    //                 modalInstance.close();
    //             };
    //
    //             $scope.confirmSave = function (carvaluation) {
    //                 carvaluationService.savePass(carvaluation,function (data) {
    //                     $scope.carvaluation = data;
    //                     toaster.pop('success','评估信息','确认完成!');
    //                     $scope.carvaluation = data;
    //                     $scope.updateApproveStatus(data);
    //                     $scope.cancel();
    //                 })
    //             };
    //
    //             $scope.cancelSave = function (carvaluation) {
    //                 carvaluationService.cancelSave(carvaluation,function (data) {
    //                     $scope.carvaluation = data;
    //                     toaster.pop('danger','评估信息','取消完成!');
    //                     $scope.carvaluation = data;
    //                     $scope.updateApproveStatus(data);
    //                     $scope.cancel();
    //                 })
    //             };
    //
    //         }
    //     });
    // }

    /**
     * 查询窗口
     */
    $scope.showQuery = function () {
        $scope.queryModal = $modal.open({
            scope:$scope,
            animation:true,
            templateUrl:"query.html",
            controller:function($scope,$modalInstance){
                //we do nothing now!
            }
        })
    };

    /**
     * 清空查询
     */
    $scope.clearQuery = function() {
        $scope.carValuationFilter = {};
        $scope.changePage(1);
    };

    //评估确认
    $scope.confirmSave = function (carvaluation) {
        carvaluationService.savePass(carvaluation,function (data) {
            $scope.carvaluation = data;
            toaster.pop('success','评估信息','确认完成!');

        })
    };

    //取消确认
    $scope.cancelSave = function (carvaluation) {
        carvaluationService.cancelSave(carvaluation,function (data) {
            $scope.carvaluation = data;
            toaster.pop('danger','评估信息','取消完成!');
        })
    };

    //保存
    $scope.saveInfo = function (carvaluation) {
        carvaluationService.saveInfo(carvaluation,function (data) {
            $scope.carvaluation = data;
            toaster.pop('success','评估信息','保存成功!');
            $scope.close();
        })
    };

    $scope.updateApproveStatus = function (data) {
        $scope.carvaluation.approveStatus = data.approveStatus;
    }
    /**
     * 查询单据
     * @param customer
     */
    $scope.queryBill = function (carvaluation) {
        $scope.carValuationFilter = carvaluation;
        $scope.changePage(1);
    };

    $scope.delete = function (carvaluation,index) {
        var msg = "确定要作废此数据  ?";
        if (carvaluation.dataStatus == 9) {
            msg = "确定要删除此数据  ?";
        }
        if (confirm(msg)) {
            var id = carvaluation.id;
            carvaluationService.delete(id, function (data) {
                if (carvaluation.dataStatus == 9) {
                    $scope.carvaluations.splice(index, 1);
                } else {
                    $scope.carvaluations[index] = data;
                }
            });
        }
    }

});