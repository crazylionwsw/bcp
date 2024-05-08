/**
 * Created by LB on 2016-10-24.
 */

'use strict';

app.controller('creditproductController',function ($scope,creditproductService,repaymentwayService,guaranteewayService,provinceService,cashsourceService,$state,$stateParams, toaster,$q){
    
    $scope.title = '分期产品管理';
    $scope.formTitle = "分期产品";

    //分期产品列表
    $scope.creditproducts = [];
    $scope.creditproduct = {};
    $scope.currentPage = 1;
    
    /*禁用*/
    $scope.disForm = false;
    //选中的内容
    $scope.selectedProduct = {};


    $scope.init = function () {
        $scope.changePageCreditProduct();
        
        //还款方式
        repaymentwayService.lookup(function (data){
            $scope.repaymentways = data;//返回的数据
        })
        
        //担保方式
        guaranteewayService.lookup(function (data){
            $scope.guaranteeways = data;//返回的数据
        })

        //地区列表
        provinceService.lookup(function (data) {
            $scope.regions = data;
        });

        cashsourceService.looupSourceRates(function (data) {
            $scope.sourcerates = data;
        });

        cashsourceService.getCashTypes(function (data) {
            $scope.cashSourceTypes = data;
        })
        
        
    }

    $scope.changePageCreditProduct = function () {
        creditproductService.getPageData($scope.currentPage-1,function (data) {
            $scope.creditproducts = data.result;
            $scope.totalItemsCreditProduct = data.totalCount;//数据总条数
            $scope.pageSizeCreditProduct = data.pageSize;//分页单位
            $scope.currentPageCreditProduct = data.currentPage + 1;//当前页
            $scope.totalPages=data.totalPages;//总页数
        });
    }

    /**
     * 选中
     * @param product
     */
    $scope.selectProduct = function(product) {

        if($scope.selectedProduct){
            $scope.selectedProduct.selected = false;
        }
        product.selected = true;
        $scope.selectedProduct = product;
        $scope.creditproduct = angular.copy(product);

        if(product.dataStatus == 9){
            $scope.disForm = true;
        }else{
            $scope.disForm = false;
        }
    }

    /**
     * 添加分期产品
     * @param flag
     */
    $scope.add = function(){
        $scope.disForm = false;
        if($scope.selectedProduct){
            $scope.selectedProduct.selected = false;
        }
        $scope.creditproduct = {dataStatus:1};
        $scope.userForm.$setPristine();
    };



    /* 删除 */
    $scope.delete = function () {
        var creditproduct = $scope.selectedProduct;

            if (creditproduct.dataStatus != 9 ){
                if (confirm("确定要作废数据  " + creditproduct.name + "  ?")) {
                    creditproductService.delete(creditproduct, function (data) {
                        var index = $scope.creditproducts.indexOf(creditproduct);
                        $scope.creditproducts[index] = data;
                        $scope.creditproduct = data;
                        /*选中*/
                        $scope.selectProduct($scope.creditproduct);
                        toaster.pop('success','分期产品','作废成功');
                    })
                }
            }else if(creditproduct.dataStatus == 9){
                if (confirm("确定要删除数据  " + creditproduct.name + "  ?")) {
                    creditproductService.delete(creditproduct, function (data) {
                        //  如果 返回的数据状态为 作废
                        if (creditproduct.dataStatus == 9){
                            var index = $scope.creditproducts.indexOf(creditproduct);
                            $scope.creditproducts.splice(index, 1);
                            $scope.creditproduct = [];
                            $scope.add();
                        }
                        toaster.pop('success','分期产品','删除成功');
                    })
                }
            }
    };

    /*恢复数据*/
    $scope.renew = function () {
        var creditproduct = $scope.selectedProduct;
        if(confirm("确定要恢复该条数据?")){
            creditproduct.dataStatus = 1;
            creditproductService.save(creditproduct,function (data) {
                if(creditproduct.dataStatus != 1){
                    alert("恢复数据出错!")
                }
                $scope.selectProduct(creditproduct);
                toaster.pop('success','分期产品','恢复成功');
            })
        }
    };

    /**
     * 执行初始化
     */
    $scope.init();

    /*验证唯一*/
    $scope.checkUniqueCreditProduct = function(propname,propval) {
        return $q(function(resolve, reject){
            var entity = $scope.creditproduct;
            creditproductService.checkUniqueCreditProduct(entity,propname,propval,function(data){
                if(data=="false"){
                    reject();
                }else{
                    resolve();
                }
            });
        });
    };

    $scope.checkMaxCreditAmount = function(value) {

        if(value){
            if (parseInt(value) > parseInt($scope.creditproduct.minCreditAmount)) {
                return true;
            } else {
                return false;
            }
        }else {
            return true;
        }
    };


    $scope.checkRatio = function(value) {
        if ($scope.creditproduct.compensatoryInterest) {
            //parseFloat($scope.creditproduct.compensatory.ratio)
            if($scope.creditproduct.compensatory.ratio + $scope.creditproduct.salePrice.downPaymentRatio + value == 100) {
                return true;
            } else {
                return false;
            }
        } else {
            if ($scope.creditproduct.salePrice) {
                if($scope.creditproduct.salePrice.downPaymentRatio + value == 100) {
                    return true;
                } else {
                    return false;
                }
            }

            return true;
        }
    };


    //开始日期小于结束日期
    $scope.checkEndDate = function(value) {
        if (value){
            if (moment(value).isAfter($scope.creditproduct.startDate)) {
                return true;
            } else {
                return false;
            }
        } else{
            return true;
        }
    };

    /**
     * 编辑保存
     */
    $scope.save = function (){
            var creditproduct = $scope.creditproduct;
            creditproductService.save(creditproduct,function (data){
                $scope.saveed = true;
                $scope.creditproduct = data;
                $scope.creditproduct.selected = true;

                $scope.selectedProduct = $scope.creditproduct;

                if(creditproduct.id == "" || creditproduct.id == null){ //添加
                    $scope.creditproducts.push($scope.creditproduct);
                }else {
                    angular.forEach($scope.creditproducts, function(item, index){
                        if (item.id == $scope.creditproduct.id) {
                            $scope.creditproducts[index] = $scope.creditproduct;
                        }
                    });
                    $scope.selectProduct($scope.creditproduct);
                }
                toaster.pop('success','分期产品','保存成功！');
            });
    }
});
